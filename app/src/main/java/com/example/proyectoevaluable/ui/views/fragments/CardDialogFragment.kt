package com.example.proyectoevaluable.ui.views.fragments

import android.Manifest
import android.app.AlertDialog
import android.app.Dialog
import android.os.Bundle
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import androidx.appcompat.app.AppCompatActivity
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Matrix
import android.media.MediaScannerConnection
import android.net.Uri
import android.os.Environment
import android.provider.MediaStore
import android.util.Base64
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import androidx.exifinterface.media.ExifInterface
import androidx.fragment.app.DialogFragment
import com.example.proyectoevaluable.R
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.Date

class CardDialogFragment(
    private val initialTitle: String? = null,
    private val initialDescription: String? = null,
    private val initialWeight: String? = null,
    // Para el modo edición, la imagen viene en Base64
    private val initialPhotoUri: String? = null,
    // NUEVOS parámetros: coordenadas que se hayan almacenado previamente en la card
    private val initialLatitude: Double? = null,
    private val initialLongitude: Double? = null,
    private val onSubmit: (String, String, String, String, Double?, Double?) -> Unit
) : DialogFragment() {

    private var photoUri: Uri? = null
    private lateinit var currentPhotoPath: String

    // Variables para almacenar las coordenadas extraídas o iniciales
    private var extractedLatitude: Double? = null
    private var extractedLongitude: Double? = null

    private lateinit var rootView: View

    private lateinit var cameraPermissionLauncher: ActivityResultLauncher<String>
    private lateinit var cameraActivityLauncher: ActivityResultLauncher<Intent>
    private lateinit var galleryActivityLauncher: ActivityResultLauncher<Intent>

    override fun onAttach(context: Context) {
        super.onAttach(context)
        cameraPermissionLauncher = registerForActivityResult(ActivityResultContracts.RequestPermission()) { granted ->
            if (granted) {
                openCamera()
            } else {
                Toast.makeText(requireContext(), "Permiso de cámara denegado", Toast.LENGTH_SHORT).show()
            }
        }
        cameraActivityLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == AppCompatActivity.RESULT_OK) {
                photoUri?.let {
                    MediaScannerConnection.scanFile(requireContext(), arrayOf(currentPhotoPath), null, null)
                }
                val bitmap = getScaledCorrectlyOrientedBitmap(currentPhotoPath, 1200, 1200)
                val imageView = rootView.findViewById<ImageView>(R.id.selectPhotoImageView)
                if (bitmap != null) {
                    imageView.setImageBitmap(bitmap)
                    imageView.invalidate()
                    photoUri = saveBitmapToInternalStorage(bitmap)
                } else {
                    Toast.makeText(requireContext(), "Error al procesar la imagen de la cámara", Toast.LENGTH_SHORT).show()
                }
            }
        }
        galleryActivityLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == AppCompatActivity.RESULT_OK && result.data != null) {
                val selectedUri = result.data?.data
                if (selectedUri == null) {
                    Toast.makeText(requireContext(), "No se seleccionó ninguna imagen", Toast.LENGTH_SHORT).show()
                    return@registerForActivityResult
                }
                var imageLatitude: Double? = null
                var imageLongitude: Double? = null
                try {
                    requireContext().contentResolver.openInputStream(selectedUri)?.use { inputStream ->
                        val exif = ExifInterface(inputStream)
                        val latLong = FloatArray(2)
                        if (exif.getLatLong(latLong)) {
                            imageLatitude = latLong[0].toDouble()
                            imageLongitude = latLong[1].toDouble()
                            Log.d("CardDialogFragment", "EXIF (galería): lat=$imageLatitude, lon=$imageLongitude")
                        }
                    }
                } catch (e: IOException) {
                    Toast.makeText(requireContext(), "Error al leer EXIF: ${e.message}", Toast.LENGTH_SHORT).show()
                }
                val tempUri = saveImageToInternalStorage(selectedUri)
                val filePath = tempUri?.path
                val bitmap = filePath?.let { getScaledCorrectlyOrientedBitmap(it, 1200, 1200) }
                val imageView = rootView.findViewById<ImageView>(R.id.selectPhotoImageView)
                if (bitmap != null) {
                    imageView.setImageBitmap(bitmap)
                    photoUri = saveBitmapToInternalStorage(bitmap)
                    extractedLatitude = imageLatitude
                    extractedLongitude = imageLongitude
                    Log.d("CardDialogFragment", "Coordenadas asignadas: lat=$extractedLatitude, lon=$extractedLongitude")
                } else {
                    Toast.makeText(requireContext(), "Error al procesar la imagen de la galería", Toast.LENGTH_SHORT).show()
                }
                imageView.invalidate()
            }
        }
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val builder = AlertDialog.Builder(requireContext())
        rootView = LayoutInflater.from(context).inflate(R.layout.dialog_card, null)

        val titleEditText = rootView.findViewById<EditText>(R.id.titleEditText)
        val descriptionEditText = rootView.findViewById<EditText>(R.id.descriptionEditText)
        val weightEditText = rootView.findViewById<EditText>(R.id.weightEditText)
        val selectPhotoImageView = rootView.findViewById<ImageView>(R.id.selectPhotoImageView)
        val mapsButton: Button? = rootView.findViewById(R.id.maps_button)

        titleEditText.setText(initialTitle)
        descriptionEditText.setText(initialDescription)
        weightEditText.setText(initialWeight)

        // Si hay imagen inicial (modo edición), decodificarla y asignar sus coordenadas
        if (!initialPhotoUri.isNullOrEmpty()) {
            val bitmap = decodeBase64ToBitmap(initialPhotoUri)
            if (bitmap != null) {
                selectPhotoImageView.setImageBitmap(bitmap)
                photoUri = saveBitmapToInternalStorage(bitmap)
            } else {
                selectPhotoImageView.setImageResource(R.drawable.logo)
            }
            // Asignamos las coordenadas iniciales
            extractedLatitude = initialLatitude
            extractedLongitude = initialLongitude
            Log.d("CardDialogFragment", "Imagen editada: usando coordenadas iniciales: lat=$extractedLatitude, lon=$extractedLongitude")
        }

        selectPhotoImageView.setOnClickListener { showImagePickerOptions() }
        mapsButton?.setOnClickListener {
            Log.d("CardDialogFragment", "Maps button clicked")
            openMapFromImage()
        }

        builder.setView(rootView)
            .setTitle(if (initialTitle == null) "Añadir Tarjeta" else "Editar Tarjeta")
            .setNegativeButton("Cancelar") { dialog, _ -> dialog.dismiss() }
            .setPositiveButton("Guardar", null)

        val dialog = builder.create()
        dialog.setOnShowListener {
            val positiveButton = (dialog as AlertDialog).getButton(AlertDialog.BUTTON_POSITIVE)
            positiveButton.setOnClickListener {
                val title = titleEditText.text.toString().trim()
                if (title.isEmpty()) {
                    Toast.makeText(requireContext(), "El título es obligatorio", Toast.LENGTH_SHORT).show()
                } else {
                    val base64Image: String = photoUri?.let { uri ->
                        try {
                            val bitmap = MediaStore.Images.Media.getBitmap(requireContext().contentResolver, uri)
                            bitmapToBase64(bitmap)
                        } catch (e: Exception) {
                            ""
                        }
                    } ?: ""
                    Log.d("CardDialogFragment", "Envío onSubmit con coordenadas: lat=$extractedLatitude, lon=$extractedLongitude")
                    onSubmit(
                        title,
                        descriptionEditText.text.toString(),
                        weightEditText.text.toString(),
                        base64Image,
                        extractedLatitude,
                        extractedLongitude
                    )
                    dialog.dismiss()
                }
            }
        }
        dialog.window?.setBackgroundDrawableResource(R.drawable.dialog_card_border)
        return dialog
    }

    private fun showImagePickerOptions() {
        val options = arrayOf("Abrir cámara", "Seleccionar de la galería")
        AlertDialog.Builder(requireContext())
            .setTitle("Seleccionar imagen")
            .setItems(options) { _, which ->
                when (which) {
                    0 -> checkCameraPermissions()
                    1 -> openGallery()
                }
            }
            .show()
    }

    private fun checkCameraPermissions() {
        if (ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.CAMERA)
            == PackageManager.PERMISSION_GRANTED
        ) {
            openCamera()
        } else {
            cameraPermissionLauncher.launch(Manifest.permission.CAMERA)
        }
    }

    private fun openCamera() {
        val cameraIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        if (cameraIntent.resolveActivity(requireActivity().packageManager) != null) {
            val photoFile = createImageFile()
            photoFile?.let { file ->
                photoUri = FileProvider.getUriForFile(
                    requireContext(),
                    "${requireActivity().packageName}.fileprovider",
                    file
                )
                cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoUri)
                cameraActivityLauncher.launch(cameraIntent)
            }
        }
    }

    private fun createImageFile(): File? {
        return try {
            val timeStamp = SimpleDateFormat("yyyyMMdd_HHmmss").format(Date())
            val storageDir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES)
            File.createTempFile("JPEG_${timeStamp}_", ".jpg", storageDir).apply {
                currentPhotoPath = absolutePath
            }
        } catch (ex: IOException) {
            Toast.makeText(requireContext(), "Error al crear el archivo de imagen", Toast.LENGTH_SHORT).show()
            null
        }
    }

    private fun openGallery() {
        val galleryIntent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
        galleryActivityLauncher.launch(galleryIntent)
    }

    private fun getScaledCorrectlyOrientedBitmap(filePath: String, maxWidth: Int, maxHeight: Int): Bitmap? {
        val options = BitmapFactory.Options().apply { inJustDecodeBounds = true }
        BitmapFactory.decodeFile(filePath, options)
        val origWidth = options.outWidth
        val origHeight = options.outHeight
        if (origWidth <= 0 || origHeight <= 0) return null

        val scaleFactor = minOf(maxWidth / origWidth.toFloat(), maxHeight / origHeight.toFloat())
        val targetWidth = (origWidth * scaleFactor).toInt()
        val targetHeight = (origHeight * scaleFactor).toInt()

        fun calculateSampleSize(): Int {
            var inSampleSize = 1
            if (origHeight > targetHeight || origWidth > targetWidth) {
                val halfHeight = origHeight / 2
                val halfWidth = origWidth / 2
                while ((halfHeight / inSampleSize) >= targetHeight && (halfWidth / inSampleSize) >= targetWidth) {
                    inSampleSize *= 2
                }
            }
            return inSampleSize
        }
        options.apply {
            inJustDecodeBounds = false
            inSampleSize = calculateSampleSize()
        }
        val decodedBitmap = BitmapFactory.decodeFile(filePath, options) ?: return null
        val scaledBitmap = Bitmap.createScaledBitmap(decodedBitmap, targetWidth, targetHeight, true)
        val exif = ExifInterface(filePath)
        val orientation = exif.getAttributeInt(ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_NORMAL)
        val rotationAngle = when (orientation) {
            ExifInterface.ORIENTATION_ROTATE_90 -> 90f
            ExifInterface.ORIENTATION_ROTATE_180 -> 180f
            ExifInterface.ORIENTATION_ROTATE_270 -> 270f
            else -> 0f
        }
        return if (rotationAngle != 0f) {
            val matrix = Matrix().apply { postRotate(rotationAngle) }
            Bitmap.createBitmap(scaledBitmap, 0, 0, scaledBitmap.width, scaledBitmap.height, matrix, true)
        } else {
            scaledBitmap
        }
    }

    private fun saveBitmapToInternalStorage(bitmap: Bitmap): Uri {
        val fileName = "image_scaled_${System.currentTimeMillis()}.jpg"
        val file = File(requireContext().filesDir, fileName)
        try {
            FileOutputStream(file).use { fos ->
                bitmap.compress(Bitmap.CompressFormat.JPEG, 100, fos)
            }
        } catch (e: IOException) {
            Toast.makeText(requireContext(), "Error al guardar la imagen escalada: ${e.message}", Toast.LENGTH_SHORT).show()
        }
        return Uri.fromFile(file)
    }

    private fun saveImageToInternalStorage(imageUri: Uri): Uri {
        val contentResolver = requireContext().contentResolver
        val fileName = "image_${System.currentTimeMillis()}.jpg"
        val file = File(requireContext().filesDir, fileName)
        try {
            contentResolver.openInputStream(imageUri)?.use { inputStream ->
                file.outputStream().use { outputStream ->
                    inputStream.copyTo(outputStream)
                }
            } ?: run {
                Toast.makeText(requireContext(), "No se pudo abrir la imagen", Toast.LENGTH_SHORT).show()
                return imageUri
            }
        } catch (e: IOException) {
            Toast.makeText(requireContext(), "Error al guardar imagen: ${e.message}", Toast.LENGTH_SHORT).show()
            return imageUri
        }
        currentPhotoPath = file.absolutePath
        return Uri.fromFile(file)
    }

    /**
     * Convierte un Bitmap a una cadena Base64 sin saltos de línea.
     */
    private fun bitmapToBase64(bitmap: Bitmap): String {
        val byteArrayOutputStream = ByteArrayOutputStream()
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, byteArrayOutputStream)
        val byteArray = byteArrayOutputStream.toByteArray()
        return Base64.encodeToString(byteArray, Base64.NO_WRAP)
    }

    private fun decodeBase64ToBitmap(base64Str: String): Bitmap? {
        return try {
            val decodedBytes = Base64.decode(base64Str, Base64.NO_WRAP)
            BitmapFactory.decodeByteArray(decodedBytes, 0, decodedBytes.size)
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }

    private fun openMapFromImage() {
        Log.d("CardDialogFragment", "openMapFromImage called. extractedLatitude: $extractedLatitude, extractedLongitude: $extractedLongitude")
        if (extractedLatitude != null && extractedLongitude != null) {
            openMapWithCoordinates(extractedLatitude!!, extractedLongitude!!)
        } else {
            Toast.makeText(requireContext(), "No se encontraron coordenadas en la imagen", Toast.LENGTH_SHORT).show()
        }
    }

    private fun openMapWithCoordinates(latitude: Double, longitude: Double) {
        val geoUri = Uri.parse("https://www.google.com/maps/search/?api=1&query=$latitude,$longitude")
        val mapIntent = Intent(Intent.ACTION_VIEW, geoUri)
        val chooser = Intent.createChooser(mapIntent, "Elige una aplicación de mapas")
        if (chooser.resolveActivity(requireContext().packageManager) != null) {
            startActivity(chooser)
        } else {
            Toast.makeText(requireContext(), "No se encontró una aplicación de mapas", Toast.LENGTH_SHORT).show()
        }
    }
}
