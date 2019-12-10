package ru.skillbranch.kotlinexample

import java.lang.IllegalArgumentException

object UserHolder {
    private val map = mutableMapOf<String, User>()

    fun registerUser(
            fullName: String,
            email: String,
            password:String
    ):User{
        return User.makeUser(fullName, email = email, password = password)
            .also{user ->
                if(map.containsKey(user.login))
                    throw IllegalArgumentException("This login already exist")
                map[user.login] = user
                }
    }

    fun loginUser(login:String, password: String):String?{
        return map[login.trim()]?.run{
            if(checkPassword(password)) this.userInfo
            else null
        }
    }

    fun registerUserByPhone(
        fullName: String,
        phone:String
    ):User{
        return User.makeUser(fullName, phone=phone)
            .also {
                if(map.containsKey(phone))
                    throw IllegalArgumentException("This login already exist")
                map[phone] = it
            }

    }

    fun requestAccessCode(phone:String){
        map[phone]?.newAccessCode()
    }
}