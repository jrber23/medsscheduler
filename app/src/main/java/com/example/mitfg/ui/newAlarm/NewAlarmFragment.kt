/*
 * Copyright (c) 2022-2024 Universitat Politècnica de València
 * Authors: José Ramón Bermejo Canet
 *                jrber222@gmail.com
 *
 * Distributed under MIT license
 * (See accompanying file LICENSE.txt)
 */

package com.example.mitfg.ui.newAlarm

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import com.example.mitfg.R
import com.example.mitfg.databinding.FragmentNewAlarmBinding
import com.example.mitfg.ui.alarmDetail.AlarmDetailActivity
import com.example.mitfg.ui.newAlarm.alarmCreationStages.AlarmCreationActivity
import com.example.mitfg.utils.AlarmManagerHelper
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * The fragment where all the created alarms are shown.
 */
@AndroidEntryPoint
class NewAlarmFragment : Fragment(R.layout.fragment_new_alarm) {

    // View binding for the fragment layout
    private var _binding : FragmentNewAlarmBinding? = null
    private val binding get() = _binding!!

    // ViewModel instance for managing UI-related data
    private val viewModel: NewAlarmViewModel by viewModels()

    // The AlarmManagerHelper instance
    @Inject
    lateinit var alarmManagerHelper: AlarmManagerHelper

    // ItemTouchHelper callback for handling swipe actions on RecyclerView items
    private val itemTouchHelperCallback =
        object : ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.END) {
            override fun onMove(
                recyclerView: RecyclerView,
                viewHolder: RecyclerView.ViewHolder,
                target: RecyclerView.ViewHolder
            ): Boolean {
                return false
            }

            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                // Deletes de swiped alarm
                viewModel.deleteAlarmAtPosition(viewHolder.adapterPosition)
            }

            override fun isLongPressDragEnabled(): Boolean {
                return false
            }

        }

    // Callback interface for handling item click events in the alarm list
    private val callback = object : AlarmListAdapter.ItemClicked {
        override fun onClick(id: Long) {
            // Loads the alarm detail activity with the ID alarm
            val intent = Intent(requireContext(), AlarmDetailActivity::class.java)

            intent.putExtra("alarmId", id)
            startActivity(intent)
        }

    }

    /**
     * Called when the fragment's view has been created.
     * Initializes the view binding, sets up the RecyclerView adapter,
     * and collects the alarm list from the ViewModel to update the UI.
     */
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding = FragmentNewAlarmBinding.bind(view)

        // Swaps to the alarm creation process when this button is pressed
        binding.createNewAlarm.setOnClickListener {
            swapToCreateAlarmActivity()
        }

        // Creates the adapter for the recycler view and assing the adapter to it.
        val adapter = AlarmListAdapter(callback)
        binding.recyclerViewAlarms.adapter = adapter

        // Everytime an alarm list new value is collected, the recycler view is updated.
        lifecycleScope.launch {
            viewModel.alarmList.collect { list ->
                adapter.submitList(list)
            }
        }

        // Attachs the ItemTouchHelper created before to the recycler view
        val itemTouchHelper = ItemTouchHelper(itemTouchHelperCallback)
        itemTouchHelper.attachToRecyclerView(binding.recyclerViewAlarms)

        // If an alarm is deleted, is also canceled in the AlarmManager
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.alarmIdToDelete.collect { newAlarmIdToDelete ->
                    if (newAlarmIdToDelete != null) {
                        val alarmId = newAlarmIdToDelete.toInt()

                        alarmManagerHelper.cancel(alarmId)
                    }
                }
            }
        }
    }

    /**
     * Navigates to the AlarmCreationActivity for creating a new alarm.
     */
    private fun swapToCreateAlarmActivity() {
        val intent = Intent(context, AlarmCreationActivity::class.java)
        startActivity(intent)
    }

    /**
     * Called when the fragment's view is destroyed.
     * Cleans up the view binding.
     */
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}