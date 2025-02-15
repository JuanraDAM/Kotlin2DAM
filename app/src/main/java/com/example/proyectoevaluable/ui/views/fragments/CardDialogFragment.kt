package com.example.proyectoevaluable.ui.views.fragments

import android.Manifest
import android.app.AlertDialog
import android.app.Dialog
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Matrix
import android.media.MediaScannerConnection
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.util.Base64
import android.view.LayoutInflater
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
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

// Ahora el onSubmit recibe: title, description, weight, photoUri, latitude, longitude
class CardDialogFragment(
    private val initialTitle: String? = null,
    private val initialDescription: String? = null,
    private val initialWeight: String? = null,
    private val initialPhotoUri: String? = null,
    private val onSubmit: (String, String, String?, Uri?, Double?, Double?) -> Unit
) : DialogFragment() {

    private var photoUri: Uri? = null
    private val CAMERA_REQUEST_CODE = 100
    private val GALLERY_REQUEST_CODE = 101
    private val CAMERA_PERMISSION_CODE = 102
    private lateinit var currentPhotoPath: String

    // Variables para almacenar la ubicación extraída
    private var extractedLatitude: Double? = null
    private var extractedLongitude: Double? = null

    private lateinit var rootView: View

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val builder = AlertDialog.Builder(requireContext())
        rootView = LayoutInflater.from(context).inflate(R.layout.dialog_card, null)

        val titleEditText = rootView.findViewById<EditText>(R.id.titleEditText)
        val descriptionEditText = rootView.findViewById<EditText>(R.id.descriptionEditText)
        val weightEditText = rootView.findViewById<EditText>(R.id.weightEditText)
        val selectPhotoImageView = rootView.findViewById<ImageView>(R.id.selectPhotoImageView)
        // Botón opcional para Maps (si lo incluyes en el layout dialog_card.xml)
        val mapsButton: Button? = rootView.findViewById(R.id.maps_button)

        titleEditText.setText(initialTitle)
        descriptionEditText.setText(initialDescription)
        weightEditText.setText(initialWeight)

        if (!initialPhotoUri.isNullOrEmpty()) {
            photoUri = Uri.parse(initialPhotoUri)
            selectPhotoImageView.setImageURI(photoUri)
        }

        selectPhotoImageView.setOnClickListener { showImagePickerOptions() }
        mapsButton?.setOnClickListener { openMapFromImage() }

        builder.setView(rootView)
            .setTitle(if (initialTitle == null) "Añadir Tarjeta" else "Editar Tarjeta")
            .setNegativeButton("Cancelar") { dialog, _ ->
                dialog.dismiss()
            }
            .setPositiveButton("Guardar", null)

        val dialog = builder.create()
        dialog.setOnShowListener {
            val positiveButton = (dialog as AlertDialog).getButton(AlertDialog.BUTTON_POSITIVE)
            positiveButton.setOnClickListener {
                val title = titleEditText.text.toString().trim()
                if (title.isEmpty()) {
                    Toast.makeText(requireContext(), "El título es obligatorio", Toast.LENGTH_SHORT).show()
                } else {
                    onSubmit(
                        title,
                        descriptionEditText.text.toString(),
                        weightEditText.text.toString(),
                        photoUri,
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
            == PackageManager.PERMISSION_GRANTED) {
            openCamera()
        } else {
            requestPermissions(arrayOf(Manifest.permission.CAMERA), CAMERA_PERMISSION_CODE)
        }
    }

    private fun openCamera() {
        val cameraIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        if (cameraIntent.resolveActivity(requireActivity().packageManager) != null) {
            val photoFile = createImageFile()
            photoFile?.let { file ->
                // Asegúrate de que file_paths.xml incluya una entrada para "Pictures/"
                photoUri = FileProvider.getUriForFile(requireContext(), "${requireActivity().packageName}.fileprovider", file)
                cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoUri)
                startActivityForResult(cameraIntent, CAMERA_REQUEST_CODE)
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
        startActivityForResult(galleryIntent, GALLERY_REQUEST_CODE)
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        if (requestCode == CAMERA_PERMISSION_CODE) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                openCamera()
            } else {
                Toast.makeText(requireContext(), "Permiso de cámara denegado", Toast.LENGTH_SHORT).show()
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == AppCompatActivity.RESULT_OK) {
            when (requestCode) {
                CAMERA_REQUEST_CODE -> {
                    photoUri?.let {
                        MediaScannerConnection.scanFile(requireContext(), arrayOf(currentPhotoPath), null, null)
                    }
                    val bitmap = getScaledCorrectlyOrientedBitmap(currentPhotoPath, 600, 600)
                    val imageView = rootView.findViewById<ImageView>(R.id.selectPhotoImageView)
                    if (bitmap != null) {
                        imageView.setImageBitmap(bitmap)
                        imageView.invalidate()
                        // Guarda la versión escalada y actualiza photoUri
                        photoUri = saveBitmapToInternalStorage(bitmap)
                    } else {
                        Toast.makeText(requireContext(), "Error al procesar la imagen de la cámara", Toast.LENGTH_SHORT).show()
                    }
                }
                GALLERY_REQUEST_CODE -> {
                    val selectedUri = data?.data
                    if (selectedUri == null) {
                        Toast.makeText(requireContext(), "No se seleccionó ninguna imagen", Toast.LENGTH_SHORT).show()
                        return
                    }
                    // Extraer ubicación de la imagen original antes de copiarla
                    var imageLatitude: Double? = null
                    var imageLongitude: Double? = null
                    try {
                        requireContext().contentResolver.openInputStream(selectedUri)?.use { inputStream ->
                            val exif = ExifInterface(inputStream)
                            val latLong = FloatArray(2)
                            if (exif.getLatLong(latLong)) {
                                imageLatitude = latLong[0].toDouble()
                                imageLongitude = latLong[1].toDouble()
                            }
                        }
                    } catch (e: IOException) {
                        Toast.makeText(requireContext(), "Error al leer EXIF: ${e.message}", Toast.LENGTH_SHORT).show()
                    }
                    // Copia la imagen original al almacenamiento interno
                    val tempUri = saveImageToInternalStorage(selectedUri)
                    val filePath = tempUri?.path
                    val bitmap = filePath?.let { getScaledCorrectlyOrientedBitmap(it, 600, 600) }
                    val imageView = rootView.findViewById<ImageView>(R.id.selectPhotoImageView)
                    if (bitmap != null) {
                        imageView.setImageBitmap(bitmap)
                        // Guarda la imagen escalada y actualiza photoUri
                        photoUri = saveBitmapToInternalStorage(bitmap)
                        // Conserva la ubicación extraída
                        extractedLatitude = imageLatitude
                        extractedLongitude = imageLongitude
                    } else {
                        Toast.makeText(requireContext(), "Error al procesar la imagen de la galería", Toast.LENGTH_SHORT).show()
                    }
                    imageView.invalidate()
                }
            }
        }
    }

    private fun calculateInSampleSize(options: BitmapFactory.Options, reqWidth: Int, reqHeight: Int): Int {
        val (height, width) = options.run { outHeight to outWidth }
        var inSampleSize = 1
        if (height > reqHeight || width > reqWidth) {
            val halfHeight = height / 2
            val halfWidth = width / 2
            while ((halfHeight / inSampleSize) >= reqHeight && (halfWidth / inSampleSize) >= reqWidth) {
                inSampleSize *= 2
            }
            while ((width / inSampleSize) * (height / inSampleSize) > 720000) {
                inSampleSize *= 2
            }
        }
        return inSampleSize
    }

    private fun decodeSampledBitmapFromFile(filePath: String, reqWidth: Int, reqHeight: Int): Bitmap? {
        val options = BitmapFactory.Options().apply { inJustDecodeBounds = true }
        BitmapFactory.decodeFile(filePath, options)
        options.inSampleSize = calculateInSampleSize(options, reqWidth, reqHeight)
        options.inJustDecodeBounds = false
        return BitmapFactory.decodeFile(filePath, options)
    }

    private fun getScaledCorrectlyOrientedBitmap(filePath: String, reqWidth: Int, reqHeight: Int): Bitmap? {
        val decodedBitmap = decodeSampledBitmapFromFile(filePath, reqWidth, reqHeight) ?: return null
        val exif = ExifInterface(filePath)
        val orientation = exif.getAttributeInt(ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_NORMAL)
        val rotationAngle = when (orientation) {
            ExifInterface.ORIENTATION_ROTATE_90 -> 90f
            ExifInterface.ORIENTATION_ROTATE_180 -> 180f
            ExifInterface.ORIENTATION_ROTATE_270 -> 270f
            else -> 0f
        }
        val scaledBitmap = Bitmap.createScaledBitmap(decodedBitmap, reqWidth, reqHeight, true)
        return if (rotationAngle != 0f) {
            val matrix = Matrix()
            matrix.postRotate(rotationAngle)
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
                bitmap.compress(Bitmap.CompressFormat.JPEG, 80, fos)
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

    private fun bitmapToBase64(bitmap: Bitmap): String {
        val byteArrayOutputStream = ByteArrayOutputStream()
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, byteArrayOutputStream)
        val byteArray = byteArrayOutputStream.toByteArray()
        return Base64.encodeToString(byteArray, Base64.DEFAULT)
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

    private fun openMapFromImage() {
        if (this::currentPhotoPath.isInitialized && currentPhotoPath.isNotEmpty()) {
            try {
                val exif = ExifInterface(currentPhotoPath)
                val latLong = FloatArray(2)
                if (exif.getLatLong(latLong)) {
                    val latitude = latLong[0].toDouble()
                    val longitude = latLong[1].toDouble()
                    openMapWithCoordinates(latitude, longitude)
                } else {
                    Toast.makeText(requireContext(), "La imagen no contiene datos de ubicación", Toast.LENGTH_SHORT).show()
                }
            } catch (e: Exception) {
                Toast.makeText(requireContext(), "Error al obtener ubicación: ${e.message}", Toast.LENGTH_SHORT).show()
            }
        } else {
            Toast.makeText(requireContext(), "No hay imagen para extraer ubicación", Toast.LENGTH_SHORT).show()
        }
    }
}
