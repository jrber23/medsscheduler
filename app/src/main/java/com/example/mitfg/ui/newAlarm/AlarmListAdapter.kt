package com.example.mitfg.ui.newAlarm

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.mitfg.databinding.AlarmItemBinding
import com.example.mitfg.domain.model.Alarm

class AlarmListAdapter : androidx.recyclerview.widget.ListAdapter<Alarm, AlarmListAdapter.ViewHolder>(
    AlarmDiff
) {

    interface ItemClicked {
        fun onClick()
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

    class ViewHolder(val binding: AlarmItemBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(alarm: Alarm) {
            binding.tvAlarmTitle.text = alarm.medicineName
            binding.tvAlarmDescription.text = alarm.quantity.toString() + " pastillas" + " - Cada 1 horas"
        }

        init {
            binding.root.setOnClickListener {

            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            AlarmItemBinding.inflate(
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


