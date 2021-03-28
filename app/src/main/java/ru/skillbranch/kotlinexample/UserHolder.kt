package ru.skillbranch.kotlinexample

import androidx.annotation.VisibleForTesting
import ru.skillbranch.kotlinexample.extensions.phone

object UserHolder {
    private val map = mutableMapOf<String, User>()

    fun registerUser(
        fullName: String,
        email: String,
        password: String
    ): User = User.makeUser(fullName, email, password)
        .also { user ->
            if (isUserExists(user.login)) {
                throw IllegalArgumentException("A user with this email already exists")
            } else {
                map[user.login] = user
            }
        }

    private fun isUserExists(login: String): Boolean = map.containsKey(login)

    fun loginUser(login: String, password: String): String? {
        var loginTmp = login
        if (!login.contains('@', false)){
            loginTmp = login.phone()
        }
        return map[loginTmp.trim()]?.let {
            if (it.checkPassword(password)) it.userInfo
            else null
        }
    }

    fun registerUserByPhone(fullName: String, rawPhone: String): User =
        User.makeUser(fullName, phone = rawPhone)
            .also { user ->
                if (isUserExists(user.login)) {
                    throw IllegalArgumentException("A user with this phone already exists")
                } else {
                    map[user.login] = user
                }
            }

    fun requestAccessCode(login: String) {
        val loginTmp = if (!login.contains('@', false)) {
            login.phone()
        } else login
        map[loginTmp].let {
            it?.changePassword(it.accessCode!!, it.generateAccessCode())
        }
    }

    @VisibleForTesting(otherwise = VisibleForTesting.NONE)
    fun clearHolder() {
        map.clear()
    }
}