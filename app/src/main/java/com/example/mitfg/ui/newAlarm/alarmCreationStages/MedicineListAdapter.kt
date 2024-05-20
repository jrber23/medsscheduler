/*
 * Copyright (c) 2022-2024 Universitat Politècnica de València
 * Authors: José Ramón Bermejo Canet
 *                jrber222@gmail.com
 *
 * Distributed under MIT license
 * (See accompanying file LICENSE.txt)
 */

package com.example.mitfg.ui.newAlarm.alarmCreationStages

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.mitfg.databinding.MedicineItemBinding
import com.example.mitfg.domain.model.Medicine

class MedicineListAdapter(val itemClicked: ItemClicked) :
androidx.recyclerview.widget.ListAdapter<Medicine, MedicineListAdapter.ViewHolder>(
    MedicineDiff
){

    interface ItemClicked {
        fun onClick(medicineName: String)
    }

    object MedicineDiff : DiffUtil.ItemCallback<Medicine>() {
        override fun areItemsTheSame(oldItem: Medicine, newItem: Medicine): Boolean {
            return (oldItem.name == newItem.name) &&
                    (oldItem.description == newItem.description)
        }

        override fun areContentsTheSame(oldItem: Medicine, newItem: Medicine): Boolean {
            return (oldItem.name == newItem.name) &&
                    (oldItem.description == newItem.description)
        }

    }

    class ViewHolder(val binding: MedicineItemBinding, callback: ItemClicked) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(medicine: Medicine) {
            binding.tvMedicineName.text = medicine.name
        }

        init {
            binding.root.setOnClickListener {
                callback.onClick(binding.tvMedicineName.text.toString())
            }
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            MedicineItemBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            ), itemClicked
        )
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

}