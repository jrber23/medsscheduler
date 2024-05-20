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

class AlarmListAdapter(val itemClicked: ItemClicked) : androidx.recyclerview.widget.ListAdapter<Alarm, AlarmListAdapter.ViewHolder>(
    AlarmDiff
) {

    interface ItemClicked {
        fun onClick(id: Long)
    }

    object AlarmDiff : DiffUtil.ItemCallback<Alarm>() {
        override fun areItemsTheSame(oldItem: Alarm, newItem: Alarm): Boolean {
            return (oldItem.id == newItem.id)
        }

        override fun areContentsTheSame(oldItem: Alarm, newItem: Alarm): Boolean {
            return (oldItem.medicineName == newItem.medicineName) &&
                    (oldItem.quantity == newItem.quantity) &&
                    (oldItem.frequency == newItem.frequency)
        }
    }

    class ViewHolder(val binding: AlarmItemBinding, private val context: Context, val callback: ItemClicked) : RecyclerView.ViewHolder(binding.root) {
        fun bind(alarm: Alarm) {
            binding.tvAlarmTitle.text = alarm.medicineName

            var frequencyText : String = ""
            when (alarm.frequency) {
                AlarmManager.INTERVAL_DAY -> frequencyText = context.getString(R.string.hours24) // "24 horas"
                AlarmManager.INTERVAL_HALF_DAY -> frequencyText = context.getString(R.string.hours12)  // "12 horas"
                AlarmManager.INTERVAL_HOUR -> frequencyText = context.getString(R.string.hours1) // "1 hora"
                AlarmManager.INTERVAL_HALF_HOUR -> frequencyText = context.getString(R.string.minutes30) // "30 minutos"
                AlarmManager.INTERVAL_FIFTEEN_MINUTES -> frequencyText = context.getString(R.string.minutes15) // "15 minutos"
            }

            var medicinePresentation : String = ""
            when (alarm.medicinePresentation) {
                context.getString(R.string.pillAbrev) -> medicinePresentation = context.getString(R.string.showPill)
                context.getString(R.string.packetAbrev) -> medicinePresentation = context.getString(R.string.showPacket)
                context.getString(R.string.MlAbrev) -> medicinePresentation = context.getString(R.string.showMl)
            }

            binding.tvAlarmDescription.text = "${alarm.quantity.toString()} $medicinePresentation - $frequencyText - ${alarm.hourStart}:${alarm.minuteStart}"

            binding.root.setOnClickListener {
                callback.onClick(alarm.id)
            }
        }
    }

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

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(getItem(position))
    }


}


