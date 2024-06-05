/*
 * Copyright (c) 2022-2024 Universitat Politècnica de València
 * Authors: José Ramón Bermejo Canet
 *                jrber222@gmail.com
 *
 * Distributed under MIT license
 * (See accompanying file LICENSE.txt)
 */

package com.example.mitfg.ui.newAlarm

import android.app.AlarmManager
import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.mitfg.R
import com.example.mitfg.databinding.AlarmItemBinding
import com.example.mitfg.domain.model.Alarm

/**
 * Adapter for displaying a list of alarms in a RecyclerView.
 * Uses ListAdapter to handle the differences between list items efficiently.
 *
 * @param itemClicked Interface for handling item click events.
 */
class AlarmListAdapter(private val itemClicked: ItemClicked) : androidx.recyclerview.widget.ListAdapter<Alarm, AlarmListAdapter.ViewHolder>(
    AlarmDiff
) {

    /**
     * Interface for handling click events on alarm items.
     */
    interface ItemClicked {
        fun onClick(id: Long)
    }

    /**
     * DiffUtil.ItemCallback implementation for calculating the difference between two alarm items.
     */
    object AlarmDiff : DiffUtil.ItemCallback<Alarm>() {

        /**
         * Checks if two items are the same
         * @param oldItem the old item
         * @param newItem the new item
         */
        override fun areItemsTheSame(oldItem: Alarm, newItem: Alarm): Boolean {
            return (oldItem.id == newItem.id)
        }

        /**
         * Checks if two items has the same content
         * @param oldItem the old item
         * @param newItem the new item
         */
        override fun areContentsTheSame(oldItem: Alarm, newItem: Alarm): Boolean {
            return (oldItem.medicineName == newItem.medicineName) &&
                    (oldItem.quantity == newItem.quantity) &&
                    (oldItem.frequency == newItem.frequency)
        }
    }

    /**
     * ViewHolder class for binding alarm data to the item view.
     *
     * @param binding View binding for the alarm item layout.
     * @param context Context for accessing resources.
     * @param callback Interface for handling item click events.
     */
    class ViewHolder(val binding: AlarmItemBinding, private val context: Context, private val callback: ItemClicked) : RecyclerView.ViewHolder(binding.root) {

        /**
         * Binds the alarm data to the item view.
         *
         * @param alarm The alarm data to be displayed.
         */
        fun bind(alarm: Alarm) {
            binding.tvAlarmTitle.text = alarm.medicineName

            // Determines the frequency text based on the alarm's frequency value
            var frequencyText : String = ""
            when (alarm.frequency) {
                AlarmManager.INTERVAL_DAY -> frequencyText = context.getString(R.string.hours24) // "24 horas"
                AlarmManager.INTERVAL_HALF_DAY -> frequencyText = context.getString(R.string.hours12)  // "12 horas"
                AlarmManager.INTERVAL_HOUR -> frequencyText = context.getString(R.string.hours1) // "1 hora"
                AlarmManager.INTERVAL_HALF_HOUR -> frequencyText = context.getString(R.string.minutes30) // "30 minutos"
                AlarmManager.INTERVAL_FIFTEEN_MINUTES -> frequencyText = context.getString(R.string.minutes15) // "15 minutos"
            }

            // Determines the medicine presentation text based on the alarm's medicine presentation value
            var medicinePresentation : String = ""
            when (alarm.medicinePresentation) {
                context.getString(R.string.pillAbrev) -> medicinePresentation = context.getString(R.string.showPill)
                context.getString(R.string.packetAbrev) -> medicinePresentation = context.getString(R.string.showPacket)
                context.getString(R.string.MlAbrev) -> medicinePresentation = context.getString(R.string.showMl)
            }

            // Sets the description text with quantity, medicine presentation, frequency, and start time
            if (alarm.minuteStart < 10) {
                binding.tvAlarmDescription.text = "${alarm.quantity} $medicinePresentation - $frequencyText - ${alarm.hourStart}:0${alarm.minuteStart}"
            } else {
                binding.tvAlarmDescription.text = "${alarm.quantity} $medicinePresentation - $frequencyText - ${alarm.hourStart}:${alarm.minuteStart}"
            }


            // Sets up the click listener to trigger the callback when the item is clicked
            binding.root.setOnClickListener {
                callback.onClick(alarm.id)
            }
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
            AlarmItemBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            ),
            parent.context,
            itemClicked
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