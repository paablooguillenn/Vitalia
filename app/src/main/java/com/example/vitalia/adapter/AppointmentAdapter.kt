package com.example.vitalia.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.vitalia.databinding.ItemAppointmentBinding
import com.example.vitalia.model.Appointment
import java.text.SimpleDateFormat
import java.util.Locale

class AppointmentAdapter(
    private var appointments: List<Appointment>,
    private val onItemClicked: (String) -> Unit // Función lambda para manejar clics
) :
    RecyclerView.Adapter<AppointmentAdapter.AppointmentViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AppointmentViewHolder {
        val binding = ItemAppointmentBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return AppointmentViewHolder(binding)
    }

    override fun onBindViewHolder(holder: AppointmentViewHolder, position: Int) {
        val appointment = appointments[position]
        holder.bind(appointment)
        // Al hacer clic, invocamos la función lambda pasándole el ID de la cita
        holder.itemView.setOnClickListener { onItemClicked(appointment.id) }
    }

    override fun getItemCount() = appointments.size

    @SuppressLint("NotifyDataSetChanged")
    fun updateAppointments(newAppointments: List<Appointment>) {
        this.appointments = newAppointments
        notifyDataSetChanged()
    }

    class AppointmentViewHolder(private val binding: ItemAppointmentBinding) :
        RecyclerView.ViewHolder(binding.root) {

        private val dateFormat = SimpleDateFormat("dd 'de' MMMM, yyyy - HH:mm", Locale.getDefault())

        fun bind(appointment: Appointment) {
            binding.doctorNameText.text = appointment.doctorName
            binding.appointmentDateText.text = dateFormat.format(appointment.date)
            binding.statusChip.text = appointment.status

            // Aquí se podría añadir lógica para cambiar el color del chip según el estado
        }
    }
}