package com.example.mitfg.data.healthAdvices

import android.content.ContentValues.TAG
import android.util.Log
import com.example.mitfg.data.healthAdvices.model.HealthAdviceDto
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.toObject
import javax.inject.Inject

class HealthAdviceDataSourceImpl @Inject constructor(
    private val firestore: FirebaseFirestore
) : HealthAdviceDataSource {
    override fun getRandomHealthAdvice(): HealthAdviceDto? {
        /*
        * El caso es que estoy intentando devolver un objeto DTO con este metodo.
        * Mi idea era crear una variable que almacenará el resultado, asignarle un valor dentro del addOnCompleteListener
        * y devolverlo al final del método. Pero devuelve un objeto con sus campos nulos, por consiguiente no se actualiza el estado de la variable.
        * */


        val docRef = firestore.collection("health_advices").document("t5XrqadXPcjN1mcoswhP")
        var healthAdvice: HealthAdviceDto? = HealthAdviceDto()
        docRef.get()
            .addOnCompleteListener { document ->
                if (document.isSuccessful) {
                    healthAdvice = document.result.toObject<HealthAdviceDto>()

                    Log.d(TAG, "This document: ${document.result.data}")
                    Log.d(TAG, "This document: ${document.result.toObject<HealthAdviceDto>()}")
                } else {
                    Log.d(TAG, "No such document", document.exception)
                }
            }
            .addOnFailureListener { exception ->
                Log.d(TAG, "get failed with ", exception)
            }
        return healthAdvice
    }
}