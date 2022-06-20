package com.example.bowbuddyapp.data

import com.google.gson.annotations.SerializedName

data class User(
    @SerializedName("emailUser")
    val email: String,
    @SerializedName("nameUser")
    val name: String,
    @SerializedName("imageUser")
    val profilImage: String,
    @SerializedName("Game_idGame")
    val idGame: String,
    @SerializedName("Statistics_idStatistics")
    val idStatistics: Int
)