package ru.skillbranch.kotlinexample

import androidx.annotation.VisibleForTesting

object UserHolder {
    private val map = mutableMapOf<String, User>()

    fun registerUser(
        fullName: String,
        email: String,
        password: String
    ): User = User.makeUser(fullName, email, password)
        .also { user ->
            if (isUserExists(user.login)){
                throw IllegalArgumentException("A user with this email already exists")
            } else {
                map[user.login] = user
            }
        }

    fun isUserExists(login: String): Boolean = map.containsKey(login)

    fun loginUser(login: String, password: String): String? =
        map[login.trim()]?.let {
            if (it.checkPassword(password)) it.userInfo
            else null
        }

    @VisibleForTesting(otherwise = VisibleForTesting.NONE)
    fun clearHolder(){
        map.clear()
    }
}