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
import com.example.vitalia.adapter.AppointmentAdapter
import com.example.vitalia.databinding.FragmentAppointmentListBinding
import com.example.vitalia.model.Appointment
import com.example.vitalia.network.RetrofitClient
import kotlinx.coroutines.launch
import java.util.Date

class AppointmentListFragment : Fragment() {

    private var _binding: FragmentAppointmentListBinding? = null
    private val binding get() = _binding!!

    private lateinit var adapter: AppointmentAdapter
    private var allAppointments: List<Appointment> = listOf() // Empezamos con una lista vacía

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentAppointmentListBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Inicializamos el adapter con una lista vacía
        adapter = AppointmentAdapter(listOf()) { appointmentId ->
            // Acción al hacer clic en una cita: navegar al detalle
            val action = AppointmentListFragmentDirections.actionAppointmentListFragmentToAppointmentDetailFragment(appointmentId)
            findNavController().navigate(action)
        }
        binding.appointmentsRecyclerView.adapter = adapter

        binding.backButton.setOnClickListener {
            findNavController().popBackStack()
        }

        setupFilters()
        fetchAppointments()
    }

    private fun fetchAppointments() {
        setLoading(true)
        viewLifecycleOwner.lifecycleScope.launch {
            try {
                val response = RetrofitClient.appointmentApi.getAppointments()
                if (response.isSuccessful && response.body() != null) {
                    allAppointments = response.body()!!.sortedBy { it.date } // Guardamos y ordenamos
                    adapter.updateAppointments(allAppointments)
                    binding.filterChipGroup.check(R.id.chip_all) // Reseteamos el filtro a "Todas"
                } else {
                    Toast.makeText(requireContext(), "Error al cargar las citas", Toast.LENGTH_LONG).show()
                }
            } catch (e: Exception) {
                Toast.makeText(requireContext(), "Error de conexión: ${e.message}", Toast.LENGTH_LONG).show()
            } finally {
                setLoading(false)
            }
        }
    }

    private fun setupFilters() {
        binding.filterChipGroup.setOnCheckedChangeListener { _, checkedId ->
            val filteredList = when (checkedId) {
                R.id.chip_confirmed -> allAppointments.filter { it.status.equals("CONFIRMADA", ignoreCase = true) }
                R.id.chip_pending -> allAppointments.filter { it.status.equals("PENDIENTE", ignoreCase = true) }
                R.id.chip_finished -> allAppointments.filter { it.status.equals("FINALIZADA", ignoreCase = true) }
                R.id.chip_cancelled -> allAppointments.filter { it.status.equals("CANCELADA", ignoreCase = true) }
                else -> allAppointments
            }
            adapter.updateAppointments(filteredList)
        }
    }

    private fun setLoading(isLoading: Boolean) {
        binding.progressBar.isVisible = isLoading
        binding.appointmentsRecyclerView.isVisible = !isLoading
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}