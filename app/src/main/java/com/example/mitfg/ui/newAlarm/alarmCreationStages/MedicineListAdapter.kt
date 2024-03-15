package com.example.mitfg.ui.newAlarm.alarmCreationStages

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.mitfg.databinding.MedicineItemBinding
import com.example.mitfg.domain.model.Medicine

class MedicineListAdapter() :
androidx.recyclerview.widget.ListAdapter<Medicine, MedicineListAdapter.ViewHolder>(
    MedicineDiff
){

    interface ItemClicked {
        fun OnClick()
    }

    object MedicineDiff : DiffUtil.ItemCallback<Medicine>() {
        override fun areItemsTheSame(oldItem: Medicine, newItem: Medicine): Boolean {
            return (oldItem.name == newItem.name) &&
                    (oldItem.type == newItem.type)
        }

        override fun areContentsTheSame(oldItem: Medicine, newItem: Medicine): Boolean {
            return false
        }

    }

    class ViewHolder(val binding: MedicineItemBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(medicine: Medicine) {
            binding.tvMedicineName.text = medicine.name
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            MedicineItemBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: MedicineListAdapter.ViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

}