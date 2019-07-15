package com.deanna.mvrx.model

import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class User(
    val userId: Int,
    val userName: String,
    val reputation: Int,
    val imageUrl: String,
    val websiteUrl: String
)
