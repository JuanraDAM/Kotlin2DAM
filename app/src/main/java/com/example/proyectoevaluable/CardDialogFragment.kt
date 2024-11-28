package com.example.proyectoevaluable

import android.Manifest
import android.app.AlertDialog
import android.app.Dialog
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.view.LayoutInflater
import android.view.View
import android.widget.EditText
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import androidx.fragment.app.DialogFragment
import java.io.File
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.Date

class CardDialogFragment(
    private val initialTitle: String? = null,
    private val initialDescription: String? = null,
    private val initialWeight: String? = null,
    private val initialPhotoUri: String? = null,
    private val onSubmit: (String, String, String?, Uri?) -> Unit
) : DialogFragment() {

    private var photoUri: Uri? = null
    private val CAMERA_REQUEST_CODE = 100
    private val GALLERY_REQUEST_CODE = 101
    private val CAMERA_PERMISSION_CODE = 102
    private lateinit var currentPhotoPath: String

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val builder = AlertDialog.Builder(requireContext())
        val inflater = requireActivity().layoutInflater
        val view = inflater.inflate(R.layout.dialog_card, null)

        val titleEditText: EditText = view.findViewById(R.id.titleEditText)
        val descriptionEditText: EditText = view.findViewById(R.id.descriptionEditText)
        val weightEditText: EditText = view.findViewById(R.id.weightEditText)
        val selectPhotoImageView: ImageView = view.findViewById(R.id.selectPhotoImageView)

        // Inicializar valores
        titleEditText.setText(initialTitle)
        descriptionEditText.setText(initialDescription)
        weightEditText.setText(initialWeight)

        if (!initialPhotoUri.isNullOrEmpty()) {
            photoUri = Uri.parse(initialPhotoUri)
            selectPhotoImageView.setImageURI(photoUri)
        }

        selectPhotoImageView.setOnClickListener {
            showImagePickerOptions()
        }

        builder.setView(view)
            .setTitle(if (initialTitle == null) "Añadir Tarjeta" else "Editar Tarjeta")
            .setPositiveButton("Guardar") { _, _ ->
                val title = titleEditText.text.toString()
                val description = descriptionEditText.text.toString()
                val weight = weightEditText.text.toString()
                onSubmit(title, description, weight, photoUri)
            }
            .setNegativeButton("Cancelar") { dialog, _ ->
                dialog.dismiss()
            }

        return builder.create()
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
        if (ContextCompat.checkSelfPermission(
                requireContext(),
                Manifest.permission.CAMERA
            ) == PackageManager.PERMISSION_GRANTED
        ) {
            openCamera()
        } else {
            ActivityCompat.requestPermissions(
                requireActivity(),
                arrayOf(Manifest.permission.CAMERA),
                CAMERA_PERMISSION_CODE
            )
        }
    }

    private fun openCamera() {
        val cameraIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        if (cameraIntent.resolveActivity(requireActivity().packageManager) != null) {
            val photoFile = createImageFile()
            if (photoFile != null) {
                val photoUri = FileProvider.getUriForFile(
                    requireContext(),
                    "${requireActivity().packageName}.fileprovider",
                    photoFile
                )
                this.photoUri = photoUri
                cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoUri)
                startActivityForResult(cameraIntent, CAMERA_REQUEST_CODE)
            }
        }
    }

    private fun createImageFile(): File? {
        return try {
            val timeStamp: String = SimpleDateFormat("yyyyMMdd_HHmmss").format(Date())
            val storageDir: File? = requireContext().getExternalFilesDir(Environment.DIRECTORY_PICTURES)
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

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
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
                    view?.findViewById<ImageView>(R.id.selectPhotoImageView)?.setImageURI(photoUri)
                }
                GALLERY_REQUEST_CODE -> {
                    photoUri = data?.data?.let { saveImageToInternalStorage(it) }
                    view?.findViewById<ImageView>(R.id.selectPhotoImageView)?.setImageURI(photoUri)
                }
            }
        }
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
            }
        } catch (e: IOException) {
            Toast.makeText(requireContext(), "Error al guardar imagen", Toast.LENGTH_SHORT).show()
        }

        return Uri.fromFile(file)
    }
}
