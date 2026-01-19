package com.example.vitalia

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.example.vitalia.databinding.FragmentRegisterBinding
import com.example.vitalia.network.RetrofitClient
import com.example.vitalia.network.request.RegisterRequest
import kotlinx.coroutines.launch

class RegisterFragment : Fragment() {

    private var _binding: FragmentRegisterBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentRegisterBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.backButton.setOnClickListener {
            findNavController().popBackStack()
        }

        binding.goToLoginText.setOnClickListener {
            findNavController().popBackStack()
        }

        binding.registerButton.setOnClickListener {
            handleRegister()
        }
    }

    private fun handleRegister() {
        val name = binding.nameEditText.text.toString().trim()
        val email = binding.emailEditTextRegister.text.toString().trim()
        val password = binding.passwordEditTextRegister.text.toString().trim()

        if (name.isEmpty() || email.isEmpty() || password.isEmpty()) {
            Toast.makeText(requireContext(), "Todos los campos son obligatorios", Toast.LENGTH_SHORT).show()
            return
        }
        
        setLoading(true)

        viewLifecycleOwner.lifecycleScope.launch {
            try {
                val response = RetrofitClient.authApi.register(RegisterRequest(name, email, password))

                if (response.isSuccessful) {
                    Toast.makeText(requireContext(), "Registro completado. Por favor, inicia sesión.", Toast.LENGTH_LONG).show()
                    findNavController().popBackStack() // Volvemos al Login
                } else {
                    // Aquí podrías leer el cuerpo del error para un mensaje más específico
                    Toast.makeText(requireContext(), "Error en el registro. El email podría estar ya en uso.", Toast.LENGTH_LONG).show()
                }

            } catch (e: Exception) {
                Toast.makeText(requireContext(), "Error de conexión: ${e.message}", Toast.LENGTH_LONG).show()
            } finally {
                setLoading(false)
            }
        }
    }

    private fun setLoading(isLoading: Boolean) {
        binding.registerProgressBar.isVisible = isLoading
        binding.registerButton.isEnabled = !isLoading
        binding.goToLoginText.isEnabled = !isLoading
        binding.backButton.isEnabled = !isLoading
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}