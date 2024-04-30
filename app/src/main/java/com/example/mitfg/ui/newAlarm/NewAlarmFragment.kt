package com.example.mitfg.ui.newAlarm

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
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
import com.example.mitfg.ui.alarmDetail.AlarmDetailActivity
import com.example.mitfg.R
import com.example.mitfg.databinding.FragmentNewAlarmBinding
import com.example.mitfg.ui.main.AlarmReceiver
import com.example.mitfg.ui.newAlarm.alarmCreationStages.AlarmCreationActivity
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class NewAlarmFragment : Fragment(R.layout.fragment_new_alarm) {

    private var _binding : FragmentNewAlarmBinding? = null
    private val binding get() = _binding!!
    private val viewModel: NewAlarmViewModel by viewModels()
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
                viewModel.deleteAlarmAtPosition(viewHolder.adapterPosition)
            }

            override fun isLongPressDragEnabled(): Boolean {
                return false
            }

        }
    private val callback = object : AlarmListAdapter.ItemClicked {
        override fun onClick(id: Long) {
            val intent = Intent(requireContext(), AlarmDetailActivity::class.java)

            intent.putExtra("alarmId", id)
            startActivity(intent)
        }

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding = FragmentNewAlarmBinding.bind(view)

        binding.createNewAlarm.setOnClickListener {
            swapToCreateAlarmActivity()
        }

        val adapter = AlarmListAdapter(callback)
        binding.recyclerViewAlarms.adapter = adapter

        lifecycleScope.launch {
            viewModel.alarmList.collect { list ->
                adapter.submitList(list)
            }
        }

        val itemTouchHelper = ItemTouchHelper(itemTouchHelperCallback)
        itemTouchHelper.attachToRecyclerView(binding.recyclerViewAlarms)

        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.alarmIdToDelete.collect { newAlarmIdToDelete ->
                    if (newAlarmIdToDelete != null) {
                        val intent = Intent(context, AlarmReceiver::class.java)
                        val alarmToCancel : PendingIntent =
                            PendingIntent.getBroadcast(
                                requireContext(),
                                newAlarmIdToDelete.toInt(),
                                intent,
                                PendingIntent.FLAG_MUTABLE
                            )

                        val alarmManager = requireContext().getSystemService(Context.ALARM_SERVICE) as AlarmManager
                        alarmManager.cancel(alarmToCancel)
                    }
                }
            }
        }
    }

    private fun swapToCreateAlarmActivity() {
        val intent = Intent(context, AlarmCreationActivity::class.java)
        startActivity(intent)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}