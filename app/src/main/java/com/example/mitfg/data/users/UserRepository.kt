package com.example.mitfg.data.users

import com.example.mitfg.domain.model.User

interface UserRepository {

    suspend fun getUserByEmail(email: String) : Result<User?>

    suspend fun addUser(user: User)

}