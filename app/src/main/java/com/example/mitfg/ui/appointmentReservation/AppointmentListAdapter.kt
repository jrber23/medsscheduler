package com.example.mitfg.ui.appointmentReservation

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.mitfg.databinding.AppointmentItemBinding
import com.example.mitfg.domain.model.Appointment

class AppointmentListAdapter : androidx.recyclerview.widget.ListAdapter<Appointment, AppointmentListAdapter.ViewHolder>(
    AppointmentDiff
) {

    object AppointmentDiff : DiffUtil.ItemCallback<Appointment>() {
        override fun areItemsTheSame(oldItem: Appointment, newItem: Appointment): Boolean {
            return (oldItem.emailPatient == newItem.emailPatient) &&
                    (oldItem.emailDoctor == newItem.emailDoctor) &&
                    (oldItem.day == newItem.day) &&
                    (oldItem.month == newItem.month) &&
                    (oldItem.year == newItem.year) &&
                    (oldItem.hour == newItem.hour) &&
                    (oldItem.minute == newItem.minute)
        }

        override fun areContentsTheSame(oldItem: Appointment, newItem: Appointment): Boolean {
            return (oldItem.emailPatient == newItem.emailPatient) &&
                    (oldItem.emailDoctor == newItem.emailDoctor) &&
                    (oldItem.day == newItem.day) &&
                    (oldItem.month == newItem.month) &&
                    (oldItem.year == newItem.year) &&
                    (oldItem.hour == newItem.hour) &&
                    (oldItem.minute == newItem.minute)
        }

    }

    class ViewHolder(val binding: AppointmentItemBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(appointment: Appointment) {
            if (appointment.minute < 10) {
                binding.tvDateAppointment.text = "${appointment.day}/${appointment.month}/${appointment.year} - ${appointment.hour}:0${appointment.minute}"
            } else {
                binding.tvDateAppointment.text = "${appointment.day}/${appointment.month}/${appointment.year} - ${appointment.hour}:${appointment.minute}"
            }

            binding.tvDoctorData.text = appointment.emailDoctor
        }
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ViewHolder {
        return ViewHolder(
            AppointmentItemBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(getItem(position))
    }
}