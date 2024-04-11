package com.example.mitfg.data.users

import com.example.mitfg.data.users.model.UserDto
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.FirebaseFirestoreException
import com.google.firebase.firestore.toObject
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext
import javax.inject.Inject

class UserDataSourceImpl @Inject constructor(
    private val firestore: FirebaseFirestore
) : UserDataSource {
    override suspend fun getUserByEmail(email: String): Result<UserDto?> =
        withContext(Dispatchers.IO) {
            val docRef = firestore.collection("users")
            return@withContext try {
                val user = docRef.document(email).get().await()

                Result.success(user.toObject<UserDto?>())
            } catch (e: FirebaseFirestoreException) {
                Result.failure(e)
            }
        }


    override suspend fun addUser(user: UserDto) {
        val email = user.email

        val data = hashMapOf(
            "password" to user.password,
            "isDoctor" to user.isDoctor
        )

        firestore.collection("users").document(email)
            .set(data)
            .await()
    }
}