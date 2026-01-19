package com.example.vitalia

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.example.vitalia.databinding.FragmentAppointmentDetailBinding
import com.example.vitalia.model.Appointment
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class AppointmentDetailFragment : Fragment() {

    private var _binding: FragmentAppointmentDetailBinding? = null
    private val binding get() = _binding!!

    private val args: AppointmentDetailFragmentArgs by navArgs()

    // Usaremos los mismos datos de prueba que en la lista
    private val testAppointments = listOf(
        Appointment("1", "Dr. Alvaro Morte", Date(), "CONFIRMADA"),
        Appointment("2", "Dra. Ursula Corberó", Date(System.currentTimeMillis() + 86400000), "PENDIENTE"),
        Appointment("3", "Dr. Pedro Alonso", Date(System.currentTimeMillis() - 172800000), "FINALIZADA"),
        Appointment("4", "Dra. Itziar Ituño", Date(System.currentTimeMillis() + 604800000), "CANCELADA")
    )

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentAppointmentDetailBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.backButton.setOnClickListener { 
            findNavController().popBackStack()
        }

        val appointmentId = args.appointmentId
        val appointment = testAppointments.find { it.id == appointmentId }

        appointment?.let { bindAppointmentData(it) }
    }

    private fun bindAppointmentData(appointment: Appointment) {
        val dateFormat = SimpleDateFormat("dd 'de' MMMM, yyyy - HH:mm", Locale.getDefault())

        binding.detailDoctorName.text = appointment.doctorName
        binding.detailDateText.text = dateFormat.format(appointment.date)
        binding.detailStatusChip.text = appointment.status
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}