package com.example.mitfg.data

import com.example.mitfg.data.users.UserRepository
import com.example.mitfg.domain.model.User
import javax.inject.Inject

class FakeUserRepository @Inject constructor() : UserRepository {

    private val fakeUsers = mutableListOf<User>(
        User("arroba@hotmail.com", "1234", true),
        User("nach8@yahoo.com", "holamundo", true),
        User("carolina.gomez@hotmail.com", "maja_2988", false)
    )

    override suspend fun getUserByEmail(email: String): Result<User?> {
        val user : User? = fakeUsers.find { user -> user.email == email }

        return Result.success(user)
    }

    override suspend fun addUser(user: User) {
        fakeUsers.add(user)
    }
}