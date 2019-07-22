package com.deanna.mvrx.database

import androidx.annotation.NonNull
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "userResponses")
data class UserEntity (
    @ColumnInfo(name = "id") @PrimaryKey @field:NonNull val userId: Int,
    @ColumnInfo(name = "display_name") val userName: String,
    @ColumnInfo(name = "reputation") val reputation: Int,
    @ColumnInfo(name = "image_url") val imageUrl: String,
    @ColumnInfo(name = "website_url") val websiteUrl: String?
)