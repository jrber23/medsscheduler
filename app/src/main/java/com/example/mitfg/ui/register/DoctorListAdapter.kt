/*
 * Copyright (c) 2022-2024 Universitat Politècnica de València
 * Authors: José Ramón Bermejo Canet
 *                jrber222@gmail.com
 *
 * Distributed under MIT license
 * (See accompanying file LICENSE.txt)
 */

package com.example.mitfg.ui.register

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.mitfg.databinding.DoctorItemBinding
import com.example.mitfg.domain.model.User

/**
 * The adapter of the doctor list shown in the doctor selection activity.
 */
class DoctorListAdapter(private val itemClicked: ItemClicked) : androidx.recyclerview.widget.ListAdapter<User, DoctorListAdapter.ViewHolder>(
    DoctorDiff
) {

    // Interface for handling item clicks
    interface ItemClicked {
        fun onClick(email: String)
    }

    // Object for calculating the difference between old and new items
    object DoctorDiff : DiffUtil.ItemCallback<User>() {
        /**
         * Checks if two items represent the same doctor based on email
         * @param oldItem the old user
         * @param newItem the new user
         */
        override fun areItemsTheSame(oldItem: User, newItem: User): Boolean {
            return oldItem.email == newItem.email
        }

        /**
         * Checks if the contents of two items are the same
         * @param oldItem the old user
         * @param newItem the new user
         */
        override fun areContentsTheSame(oldItem: User, newItem: User): Boolean {
            return (oldItem.name == newItem.name &&
                    oldItem.surnames == newItem.surnames &&
                    oldItem.email == newItem.email &&
                    oldItem.password == newItem.password &&
                    oldItem.isDoctor == newItem.isDoctor)
        }

    }

    // ViewHolder class for holding and binding the view for each doctor item
    class ViewHolder(val binding: DoctorItemBinding, private val callback: ItemClicked) : RecyclerView.ViewHolder(binding.root) {
        /**
         * Binds the user data to the view
         * @param user The user data to bind to the view
         */
        fun bind(user: User?) {
            binding.tvDoctorNameSurname.text = "${user?.name} ${user?.surnames}"

            // Set click listener to handle item clicks
            binding.root.setOnClickListener {
                callback.onClick(user!!.email)
            }
        }

    }

    /**
     * Creates the ViewHolder by inflating the item layout
     */
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            DoctorItemBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            ),
            itemClicked
        )
    }

    /**
     * Binds the ViewHolder with data at the given position
     */
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(getItem(position))
    }
}