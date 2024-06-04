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
import com.example.mitfg.databinding.MedicinePresentationItemBinding

/**
 * Adapter for displaying a list of medicine presentations in a RecyclerView.
 * Uses ListAdapter to handle the differences between list items efficiently.
 *
 * @param itemClicked Interface for handling item click events.
 */
class MedicinePresentationListAdapter(private val itemClicked: ItemClicked) : androidx.recyclerview.widget.ListAdapter<String, MedicinePresentationListAdapter.ViewHolder>(
    MedicinePresentationDiff
) {

    /**
     * Interface for handling click events on alarm items.
     */
    interface ItemClicked {
        fun onClick(presentation: String)
    }

    /**
     * DiffUtil.ItemCallback implementation for calculating the difference between two alarm items.
     */
    object MedicinePresentationDiff : DiffUtil.ItemCallback<String>() {
        /**
         * Checks if two items are the same
         * @param oldItem the old item
         * @param newItem the new item
         */
        override fun areItemsTheSame(oldItem: String, newItem: String): Boolean {
            return (oldItem == newItem)
        }

        /**
         * Checks if two items has the same content
         * @param oldItem the old item
         * @param newItem the new item
         */
        override fun areContentsTheSame(oldItem: String, newItem: String): Boolean {
            return (oldItem == newItem)
        }
    }

    /**
     * ViewHolder class for binding alarm data to the item view.
     *
     * @param binding View binding for the alarm item layout.
     * @param callback Interface for handling item click events.
     */
    class ViewHolder(val binding: MedicinePresentationItemBinding, callback: ItemClicked): RecyclerView.ViewHolder(binding.root) {
        /**
         * Binds the alarm data to the item view.
         *
         * @param presentation The medicine presentation data to be displayed.
         */
        fun bind(presentation: String) {
            binding.tvMedicinePresentationName.text = presentation
        }

        // Sets up the click listener to trigger the callback when the item is clicked
        init {
            binding.root.setOnClickListener {
                callback.onClick(binding.tvMedicinePresentationName.text.toString())
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
            MedicinePresentationItemBinding.inflate(
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