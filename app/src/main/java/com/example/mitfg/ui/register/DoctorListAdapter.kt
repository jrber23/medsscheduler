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

class DoctorListAdapter(val itemClicked: ItemClicked) : androidx.recyclerview.widget.ListAdapter<User, DoctorListAdapter.ViewHolder>(
    DoctorDiff
) {

    interface ItemClicked {
        fun onClick(email: String)
    }

    object DoctorDiff : DiffUtil.ItemCallback<User>() {
        override fun areItemsTheSame(oldItem: User, newItem: User): Boolean {
            return oldItem.email == newItem.email
        }

        override fun areContentsTheSame(oldItem: User, newItem: User): Boolean {
            return (oldItem.name == newItem.name &&
                    oldItem.surnames == newItem.surnames &&
                    oldItem.email == newItem.email &&
                    oldItem.password == newItem.password &&
                    oldItem.isDoctor == newItem.isDoctor)
        }

    }

    class ViewHolder(val binding: DoctorItemBinding, val callback: ItemClicked) : RecyclerView.ViewHolder(binding.root) {
        fun bind(user: User?) {
            binding.tvDoctorNameSurname.text = "${user?.name} ${user?.surnames}"

            binding.root.setOnClickListener {
                callback.onClick(user!!.email)
            }
        }

    }

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

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(getItem(position))
    }
}