package com.example.data.model

import io.ktor.auth.*
import kotlinx.serialization.Serializable
import org.mindrot.jbcrypt.BCrypt

@Serializable
data class User(
    val id:Int?=-1,
    val username:String,
    val email:String,
    val image:String?=null,
    val password:String
): Principal {
    fun hashedPassword(): String {
        return BCrypt.hashpw(password, BCrypt.gensalt())
    }

    fun matchPassword(hashPassword:String):Boolean{
        val doesPasswordMatch = BCrypt.checkpw( hashPassword,password)
       return doesPasswordMatch
    }

}