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

/**
 * Adapter for displaying a list of medicines in a RecyclerView.
 * Uses ListAdapter to handle the differences between list items efficiently.
 *
 * @param itemClicked Interface for handling item click events.
 */
class MedicineListAdapter(private val itemClicked: ItemClicked) :
androidx.recyclerview.widget.ListAdapter<Medicine, MedicineListAdapter.ViewHolder>(
    MedicineDiff
){

    /**
     * Interface for handling click events on alarm items.
     */
    interface ItemClicked {
        fun onClick(medicineName: String)
    }

    /**
     * DiffUtil.ItemCallback implementation for calculating the difference between two alarm items.
     */
    object MedicineDiff : DiffUtil.ItemCallback<Medicine>() {
        /**
         * Checks if two items are the same
         * @param oldItem the old item
         * @param newItem the new item
         */
        override fun areItemsTheSame(oldItem: Medicine, newItem: Medicine): Boolean {
            return (oldItem.name == newItem.name) &&
                    (oldItem.description == newItem.description)
        }

        /**
         * Checks if two items has the same content
         * @param oldItem the old item
         * @param newItem the new item
         */
        override fun areContentsTheSame(oldItem: Medicine, newItem: Medicine): Boolean {
            return (oldItem.name == newItem.name) &&
                    (oldItem.description == newItem.description)
        }

    }

    /**
     * ViewHolder class for binding medicine data to the item view.
     *
     * @param binding View binding for the medicine item layout.
     * @param callback Interface for handling item click events.
     */
    class ViewHolder(val binding: MedicineItemBinding, callback: ItemClicked) :
        RecyclerView.ViewHolder(binding.root) {

        /**
         * Binds the medicine data to the item view.
         *
         * @param medicine The medicine data to be displayed.
         */
        fun bind(medicine: Medicine) {
            binding.tvMedicineName.text = medicine.name
        }

        // Sets up the click listener to trigger the callback when the item is clicked
        init {
            binding.root.setOnClickListener {
                callback.onClick(binding.tvMedicineName.text.toString())
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
            MedicineItemBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            ), itemClicked
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