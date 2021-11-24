package com.example.data.model

import kotlinx.serialization.Serializable

@Serializable
data class Note(
    val id: Int?,
    val title: String,
    val subTitle: String,
    val dataTime: String,
    val imagePath: String?=null,
    val note: String,
    val color: String="#FFFF",
    val webLink: String?=null,
    val userId: Int?
)