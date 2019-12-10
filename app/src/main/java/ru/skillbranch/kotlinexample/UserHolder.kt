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
                    throw IllegalArgumentException("A user with this email already exists")
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
                    throw IllegalArgumentException("A user with this phone already exists")
                map[phone] = it
            }

    }

    fun requestAccessCode(phone:String){
        map[phone]?.newAccessCode()
    }

    fun importUsers(list: List<String>): List<User>?{
        var res = mutableListOf<User>()
        list.forEach{string ->
            string.split(";")
                .also { unit ->
                    res.add(User.makeUser(unit[0], email = unit[1], salt  = unit[2].split(":")[0], password = unit[2].split(":")[1]))
                }

        }
        return res
    }
}