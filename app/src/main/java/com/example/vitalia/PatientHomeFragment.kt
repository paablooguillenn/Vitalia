package com.example.vitalia

import android.net.Uri
import android.os.Bundle
import android.provider.OpenableColumns
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.example.vitalia.databinding.FragmentPatientHomeBinding
import com.example.vitalia.network.RetrofitClient
import kotlinx.coroutines.launch
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import java.io.File
import java.io.FileOutputStream

class PatientHomeFragment : Fragment() {

    private var _binding: FragmentPatientHomeBinding? = null
    private val binding get() = _binding!!

    private val selectFileLauncher = registerForActivityResult(ActivityResultContracts.GetContent()) { uri ->
        uri?.let { uploadFile(it) }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentPatientHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.buttonAgenda.setOnClickListener {
            findNavController().navigate(R.id.action_PatientHomeFragment_to_AppointmentListFragment)
        }

        binding.buttonUploadFile.setOnClickListener {
            selectFileLauncher.launch("*/*")
        }

        binding.buttonEditProfile.setOnClickListener {
            findNavController().navigate(R.id.action_PatientHomeFragment_to_EditProfileFragment)
        }
    }

    private fun uploadFile(fileUri: Uri) {
        viewLifecycleOwner.lifecycleScope.launch {
            try {
                val filePart = uriToMultipartBody(fileUri)
                val appointmentId = "1".toRequestBody("text/plain".toMediaTypeOrNull())
                val response = RetrofitClient.instance.uploadFile(filePart, appointmentId)

                if (response.isSuccessful) {
                    Toast.makeText(requireContext(), "Subida exitosa", Toast.LENGTH_SHORT).show()
                } else {
                    Toast.makeText(requireContext(), "Error en la subida: ${response.code()}", Toast.LENGTH_LONG).show()
                }
            } catch (e: Exception) {
                Toast.makeText(requireContext(), "Excepción: ${e.message}", Toast.LENGTH_LONG).show()
            }
        }
    }

    private fun uriToMultipartBody(uri: Uri): MultipartBody.Part {
        val context = requireContext()
        val contentResolver = context.contentResolver
        val fileName = contentResolver.query(uri, null, null, null, null)?.use { cursor ->
            val nameIndex = cursor.getColumnIndex(OpenableColumns.DISPLAY_NAME)
            cursor.moveToFirst()
            cursor.getString(nameIndex)
        } ?: "file"

        val inputStream = contentResolver.openInputStream(uri)
        val file = File(context.cacheDir, fileName)
        val outputStream = FileOutputStream(file)
        inputStream?.copyTo(outputStream)
        outputStream.close()

        val requestFile = file.asRequestBody(contentResolver.getType(uri)?.toMediaTypeOrNull())
        return MultipartBody.Part.createFormData("file", file.name, requestFile)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}