package com.example.data.model

import com.google.gson.annotations.SerializedName
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import java.io.Serial


@Serializable
data class LoginRequest(
    val email: String,
    val password: String
)