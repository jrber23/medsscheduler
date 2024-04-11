package com.example.mitfg.ui.newAlarm.alarmCreationStages

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.mitfg.databinding.MedicinePresentationItemBinding

class MedicinePresentationListAdapter(val itemClicked: ItemClicked) : androidx.recyclerview.widget.ListAdapter<String, MedicinePresentationListAdapter.ViewHolder>(
    MedicinePresentationDiff
) {

    interface ItemClicked {
        fun onClick(presentation: String)
    }

    object MedicinePresentationDiff : DiffUtil.ItemCallback<String>() {
        override fun areItemsTheSame(oldItem: String, newItem: String): Boolean {
            return (oldItem == newItem)
        }

        override fun areContentsTheSame(oldItem: String, newItem: String): Boolean {
            return (oldItem == newItem)
        }

    }

    class ViewHolder(val binding: MedicinePresentationItemBinding, callback: ItemClicked): RecyclerView.ViewHolder(binding.root) {
        fun bind(presentation: String) {
            binding.tvMedicinePresentationName.text = presentation
        }

        init {
            binding.root.setOnClickListener {
                callback.onClick(binding.tvMedicinePresentationName.text.toString())
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            MedicinePresentationItemBinding.inflate(
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