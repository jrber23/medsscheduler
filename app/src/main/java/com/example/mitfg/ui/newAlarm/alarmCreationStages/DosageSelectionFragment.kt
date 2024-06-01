/*
 * Copyright (c) 2022-2024 Universitat Politècnica de València
 * Authors: José Ramón Bermejo Canet
 *                jrber222@gmail.com
 *
 * Distributed under MIT license
 * (See accompanying file LICENSE.txt)
 */

package com.example.mitfg.ui.newAlarm.alarmCreationStages

import android.content.ContentValues
import android.os.Bundle
import android.speech.tts.TextToSpeech
import android.util.Log
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Spinner
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.example.mitfg.R
import com.example.mitfg.databinding.FragmentDosageSelectionBinding
import dagger.hilt.android.AndroidEntryPoint
import java.util.Locale

@AndroidEntryPoint
class DosageSelectionFragment : Fragment(R.layout.fragment_dosage_selection), TextToSpeech.OnInitListener {

    private var _binding : FragmentDosageSelectionBinding? = null
    private val binding get() = _binding!!
    private val viewModel: AlarmCreationViewModel by activityViewModels()

    private lateinit var textToSpeech: TextToSpeech

    private fun playDosageSelectionAudioMessage() {
        textToSpeech.speak(getString(R.string.dosageSelectionVoiceMessage), TextToSpeech.QUEUE_FLUSH, null, null)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding = FragmentDosageSelectionBinding.bind(view)

        val spinner: Spinner = binding.pillQuantitySpinner

        val listener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
                viewModel.assignPillQuantity(parent?.getItemAtPosition(position).toString())
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {
                TODO("Not yet implemented")
            }
        }

        val medicinePresentation = viewModel.getMedicinePresentation()

        when (medicinePresentation) {
            getString(R.string.pillAbrev) -> {
                setQuantityArrayAdapter(getString(R.string.pill), R.array.pill_quantity_array, spinner)
            }

            getString(R.string.packetAbrev) -> {
                setQuantityArrayAdapter(getString(R.string.packet), R.array.packet_quantity_array, spinner)
            }

            getString(R.string.MlAbrev) -> {
                setQuantityArrayAdapter(getString(R.string.mililitres), R.array.mililiters_quantity_array, spinner)
            }
        }

        spinner.onItemSelectedListener = listener

        binding.buttonNext.setOnClickListener {
            viewModel.increaseProgressBar()

            navigateForward()
        }

        binding.buttonBefore.setOnClickListener {
            viewModel.decreaseProgressBar()

            navigateBack()
        }

        textToSpeech = TextToSpeech(requireContext(), this)
    }

    private fun setVoiceLanguage() : Int {
        val locale = Locale.getDefault().displayLanguage

        val result = when (locale) {
            "English" -> textToSpeech.setLanguage(Locale("en", "US"))
            "Spanish" -> textToSpeech.setLanguage(Locale("es", "ES"))
            "Catalan" -> textToSpeech.setLanguage(Locale("ca", "ES"))
            else -> textToSpeech.setLanguage(Locale("es", "ES"))
        }

        return result
    }

    private fun setQuantityArrayAdapter(medicinePresentation: String, array: Int, spinner: Spinner) {
        binding.tvDosage.text = medicinePresentation

        ArrayAdapter.createFromResource(
            requireActivity().baseContext,
            array,
            R.layout.simple_spinner_item_custom
        ).also { arrayAdapter ->
            arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            spinner.adapter = arrayAdapter
        }
    }

    private fun assignPillQuantity() {
        viewModel.assignPillQuantity(binding.pillQuantitySpinner.selectedItem.toString())
    }

    private fun navigateForward() {
        assignPillQuantity()

        findNavController().navigate(R.id.frequenceFragment)
    }

    private fun navigateBack() {
        findNavController().navigate(R.id.medicationPresentationFragment)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onInit(status: Int) {
        if (status == TextToSpeech.SUCCESS) {
            val result = setVoiceLanguage()

            if (result == TextToSpeech.LANG_MISSING_DATA || result == TextToSpeech.LANG_NOT_SUPPORTED) {
                Log.e(ContentValues.TAG, "Language not supported")
            } else {
                playDosageSelectionAudioMessage()
            }
        } else {
            Log.e(ContentValues.TAG, "Initialization failed")
        }
    }
}