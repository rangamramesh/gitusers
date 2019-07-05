package com.sharecare.gitusers.model

import androidx.room.Entity
import com.google.gson.annotations.SerializedName

@Entity(primaryKeys = ["id"])
data class User(
    @field:SerializedName("login")
    val login: String,
    @field:SerializedName("avatar_url")
    val avatarUrl: String?,
    @field:SerializedName("id")
    val id: Int,
    @field:SerializedName("type")
    val type: String?,
    @field:SerializedName("repos_url")
    val reposUrl: String?,
    @field:SerializedName("html_url")
    val htmlUrl: String?
)
