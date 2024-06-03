/*
 * Copyright (c) 2022-2024 Universitat Politècnica de València
 * Authors: José Ramón Bermejo Canet
 *                jrber222@gmail.com
 *
 * Distributed under MIT license
 * (See accompanying file LICENSE.txt)
 */

package com.example.mitfg.utils

import android.content.ContentValues
import android.content.Context
import android.speech.tts.TextToSpeech
import android.util.Log
import com.example.mitfg.R
import java.util.Locale
import javax.inject.Inject

/**
 * Object that manages the TextToSpeech instance
 */
class TextToSpeechHelper @Inject constructor(
    private val context: Context
) : TextToSpeech.OnInitListener {

    // Text-to-Speech instance
    private val textToSpeech: TextToSpeech = TextToSpeech(context, this)

    /**
     * Every triggered action when the main activity has been initiated.
     * @param status the TextToSpeech status.
     */
    override fun onInit(status: Int) {
        if (status == TextToSpeech.SUCCESS) {
            val result = setVoiceLanguage()

            if (result == TextToSpeech.LANG_MISSING_DATA || result == TextToSpeech.LANG_NOT_SUPPORTED) {
                Log.e(ContentValues.TAG, "Language not supported")
            } else {
                speak(context.getString(R.string.welcomeVoiceMessage))
            }
        } else {
            Log.e(ContentValues.TAG, "Initialization failed")
        }
    }

    /**
     * The current device language is setted in the TextToSpeech instance.
     * @return the Locale code
     */
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

    /**
     * The TextToSpeech instance plays the given message
     * @param message The message to be spoken
     */
    fun speak(message: String) {
        textToSpeech.speak(message, TextToSpeech.QUEUE_FLUSH, null, "1")
    }

    /**
     * Stops and shutdowns the TextToSpeech instance.
     */
    fun destroy() {
        textToSpeech.stop()
        textToSpeech.shutdown()
    }
}