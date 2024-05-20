/*
 * Copyright (c) 2022-2024 Universitat Politècnica de València
 * Authors: José Ramón Bermejo Canet
 *                jrber222@gmail.com
 *
 * Distributed under MIT license
 * (See accompanying file LICENSE.txt)
 */

package com.example.mitfg.ui.doctor

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.mitfg.databinding.AppointmentItemBinding
import com.example.mitfg.domain.model.Appointment

class DoctorAppointmentsListAdapter : ListAdapter<Appointment, DoctorAppointmentsListAdapter.ViewHolder>(
    DoctorAppointmentDiff
) {


    object DoctorAppointmentDiff : DiffUtil.ItemCallback<Appointment>() {
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

            binding.tvDoctorData.text = appointment.emailPatient
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
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