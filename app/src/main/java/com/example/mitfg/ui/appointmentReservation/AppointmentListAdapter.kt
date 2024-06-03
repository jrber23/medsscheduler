/*
 * Copyright (c) 2022-2024 Universitat Politècnica de València
 * Authors: José Ramón Bermejo Canet
 *                jrber222@gmail.com
 *
 * Distributed under MIT license
 * (See accompanying file LICENSE.txt)
 */

package com.example.mitfg.ui.appointmentReservation

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.mitfg.databinding.AppointmentItemBinding
import com.example.mitfg.domain.model.Appointment

/**
 * The Appointment list adapter that binds every appointment data to the appointment item view
 */
class AppointmentListAdapter : androidx.recyclerview.widget.ListAdapter<Appointment, AppointmentListAdapter.ViewHolder>(
    AppointmentDiff
) {

    // Object for calculating the difference between two lists of Appointments
    object AppointmentDiff : DiffUtil.ItemCallback<Appointment>() {

        /**
         * Check if two items represent the same object
         * @param oldItem the old appointment
         * @param newItem the new appointment
         * @return true if both objects are the same and false otherwise
         */
        override fun areItemsTheSame(oldItem: Appointment, newItem: Appointment): Boolean {
            return (oldItem.emailPatient == newItem.emailPatient) &&
                    (oldItem.emailDoctor == newItem.emailDoctor) &&
                    (oldItem.day == newItem.day) &&
                    (oldItem.month == newItem.month) &&
                    (oldItem.year == newItem.year) &&
                    (oldItem.hour == newItem.hour) &&
                    (oldItem.minute == newItem.minute)
        }

        /**
         * Check if the contents of two items are the same
         * @param oldItem the old appointment
         * @param newItem the new appointment
         * @return true if both objects contents are the same and false otherwise
         */
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

    /**
     * ViewHolder class for holding references to the views
     */
    class ViewHolder(val binding: AppointmentItemBinding) : RecyclerView.ViewHolder(binding.root) {

        /**
         * Method to bind Appointment data to the views
         * @param appointment The object which data must be binded to the views
         */
        fun bind(appointment: Appointment) {
            // Format appointment time and set it to the TextView
            val formattedTime = "${appointment.day}/${appointment.month}/${appointment.year} - " +
                    if (appointment.minute < 10) "${appointment.hour}:0${appointment.minute}"
                    else "${appointment.hour}:${appointment.minute}"
            binding.tvDateAppointment.text = formattedTime

            // Set doctor email to the TextView
            binding.tvDoctorData.text = appointment.emailDoctor


            /* if (appointment.minute < 10) {
                binding.tvDateAppointment.text = "${appointment.day}/${appointment.month}/${appointment.year} - ${appointment.hour}:0${appointment.minute}"
            } else {
                binding.tvDateAppointment.text = "${appointment.day}/${appointment.month}/${appointment.year} - ${appointment.hour}:${appointment.minute}"
            }

            binding.tvDoctorData.text = appointment.emailDoctor */
        }
    }

    /**
     * Create ViewHolder instances
     */
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

    /**
     * Bind data to ViewHolder
     */
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(getItem(position))
    }
}