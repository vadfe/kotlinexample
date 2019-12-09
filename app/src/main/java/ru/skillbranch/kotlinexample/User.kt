package ru.skillbranch.kotlinexample

import java.lang.StringBuilder
import java.math.BigInteger
import java.security.MessageDigest
import java.security.SecureRandom

class User(
    private val firstName: String,
    private val lastName: String?,
    email: String? = null,
    rawPhone: String? = null,
    meta: Map<String, Any>? = null
) {
    val userInfo: String
    private val fullName: String
        get() = listOfNotNull(firstName, lastName)
            .joinToString { " " }
            .capitalize()
    private val initials:String
        get() = listOfNotNull(firstName, lastName)
            .map { it.first().toUpperCase() }
            .joinToString { " " }
    private var phone: String? = null
        set(value){
            field = value?.replace("[^+||d]".toRegex(),"")
        }
    private var _login: String? = null
    private var login: String
        set(value) {
            _login = value?.toLowerCase()
        }
        get() = _login!!
    private lateinit var passwordHash:String

    //for email
    constructor(
        firstName: String,
        lastName: String?,
        email: String,
        password: String
    ): this(firstName, lastName, email = email, meta = mapOf("auth" to "password")){
        println("Secondary mail constructor")
        passwordHash = encrypt(password)
    }



    //for phone
    constructor(
        firstName: String,
        lastName: String?,
        rawPhone: String?
    ): this(firstName, lastName, rawPhone = rawPhone, meta = mapOf("auth" to "sms")){
        println("Secondary mail constructor")
        val code: String = generateAcsessCode()
    }

    private fun generateAcsessCode(): String {
        val possible ="ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789"
        return StringBuilder().apply {
            repeat(6){
                (possible.indices).random().also {index ->
                    append(possible[index])
                }
            }

        }.toString()
    }

    init{
        println("First init block, primary constructor was called")
        userInfo = """
            firstName: $firstName
            lastName: $lastName
            login: $login
            fullName: $fullName
            initials: $initials
            email: $email
            phone: $phone
            meta: $meta
        """.trimIndent()
    }

    private val salt: String by lazy {
        ByteArray(16).also { SecureRandom().nextBytes(it)}.toString()
    }

    private fun encrypt(password: String): String = salt.plus(password).md5()

    private fun String.md5(): String {
        val md:MessageDigest = MessageDigest.getInstance("MD5")
        val digest:ByteArray = md.digest(toByteArray())
        val  hexString:String = BigInteger(1,digest).toString(16)
        return hexString.padStart(32,'0')
    }
}


