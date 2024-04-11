package com.example.mitfg.firebase

import androidx.compose.ui.text.intl.Locale
import com.google.mlkit.common.model.DownloadConditions
import com.google.mlkit.nl.translate.TranslateLanguage
import com.google.mlkit.nl.translate.Translation
import com.google.mlkit.nl.translate.TranslatorOptions
import kotlinx.coroutines.tasks.await

class FirebaseTranslator {

    private lateinit var targetLanguage: String

    suspend fun translate(text: String): Result<String> {
        when (Locale.current.language) {
            "en" -> targetLanguage = TranslateLanguage.ENGLISH
            "ca" -> targetLanguage = TranslateLanguage.CATALAN
            else -> targetLanguage = TranslateLanguage.SPANISH
        }

        val options = TranslatorOptions.Builder()
            .setSourceLanguage(TranslateLanguage.SPANISH)
            .setTargetLanguage(targetLanguage)
            .build()

        val translator = Translation.getClient(options)

        val conditions = DownloadConditions.Builder()
            .requireWifi()
            .build()

        translator.downloadModelIfNeeded(conditions).await()

        val traduction = translator.translate(text).await()

        return Result.success(traduction)
    }



}