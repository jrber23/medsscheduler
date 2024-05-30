/*
 * Copyright (c) 2022-2024 Universitat Politècnica de València
 * Authors: José Ramón Bermejo Canet
 *                jrber222@gmail.com
 *
 * Distributed under MIT license
 * (See accompanying file LICENSE.txt)
 */

package com.example.mitfg.firebase

import androidx.compose.ui.text.intl.Locale
import com.google.mlkit.common.model.DownloadConditions
import com.google.mlkit.nl.translate.TranslateLanguage
import com.google.mlkit.nl.translate.Translation
import com.google.mlkit.nl.translate.TranslatorOptions
import kotlinx.coroutines.tasks.await

/**
 * A class that translates every received String into the device current language
 */
class FirebaseTranslator {

    // The established language in the device
    private lateinit var targetLanguage: String

    /**
     * Translates the received language into the selected language
     * @param text The text to translate
     * @return The encapsulation of the translated text
     */
    suspend fun translate(text: String): Result<String> {
        // Establish the current language
        targetLanguage = when (Locale.current.language) {
            "en" -> TranslateLanguage.ENGLISH
            "ca" -> TranslateLanguage.CATALAN
            else -> TranslateLanguage.SPANISH
        }

        // Set the translator options
        val options = TranslatorOptions.Builder()
            .setSourceLanguage(TranslateLanguage.SPANISH)
            .setTargetLanguage(targetLanguage)
            .build()

        // Creates the translator
        val translator = Translation.getClient(options)

        // Establish the translator conditions
        val conditions = DownloadConditions.Builder()
            .requireWifi()
            .build()

        // Downloads the requiered language model
        translator.downloadModelIfNeeded(conditions).await()

        val traduction = translator.translate(text).await()

        return Result.success(traduction)
    }



}