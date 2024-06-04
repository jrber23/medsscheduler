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

/**
 * Adapter for displaying a list of appointments in a RecyclerView.
 * Uses ListAdapter to handle the differences between list items efficiently.
 */
class DoctorAppointmentsListAdapter : ListAdapter<Appointment, DoctorAppointmentsListAdapter.ViewHolder>(
    DoctorAppointmentDiff
) {

    /**
     * DiffUtil.ItemCallback implementation for calculating the difference between two appointments items.
     */
    object DoctorAppointmentDiff : DiffUtil.ItemCallback<Appointment>() {
        /**
         * Checks if two items are the same
         *
         * @param oldItem the old item
         * @param newItem the new item
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
         * Checks if two items has the same content
         *
         * @param oldItem the old item
         * @param newItem the new item
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
     * ViewHolder class for binding appointment data to the item view.
     *
     * @param binding View binding for the appointment item layout.
     */
    class ViewHolder(val binding: AppointmentItemBinding) : RecyclerView.ViewHolder(binding.root) {

        /**
         * Binds the appointment data to the item view.
         *
         * @param appointment The appointment data to be displayed.
         */
        fun bind(appointment: Appointment) {
            if (appointment.minute < 10) {
                binding.tvDateAppointment.text = "${appointment.day}/${appointment.month}/${appointment.year} - ${appointment.hour}:0${appointment.minute}"
            } else {
                binding.tvDateAppointment.text = "${appointment.day}/${appointment.month}/${appointment.year} - ${appointment.hour}:${appointment.minute}"
            }

            binding.tvDoctorData.text = appointment.emailPatient
        }
    }

    /**
     * Creates a new ViewHolder when there are no existing view holders that the RecyclerView can reuse.
     *
     * @param parent The ViewGroup into which the new View will be added after it is bound to an adapter position.
     * @param viewType The view type of the new View.
     * @return A new ViewHolder that holds a View of the given view type.
     */
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            AppointmentItemBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    /**
     * Replaces the contents of a view (invoked by the layout manager).
     *
     * @param holder The ViewHolder which should be updated to represent the contents of the item at the given position in the data set.
     * @param position The position of the item within the adapter's data set.
     */
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

}