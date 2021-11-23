package com.example.data.model

import kotlinx.serialization.Serializable

@Serializable
data class RegisterRequest(
    val username:String,
    val email:String,
    val image:String?,
    val password:String
)