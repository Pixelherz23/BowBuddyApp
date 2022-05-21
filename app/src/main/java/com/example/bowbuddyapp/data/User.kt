package com.example.bowbuddyapp.data

import com.google.gson.annotations.SerializedName

data class User(
    @SerializedName("emailUser")
    val email: String,
    @SerializedName("nameUser")
    val name: String,
    @SerializedName("Game_idGame")
    val idGame: Int,
    @SerializedName("Statistics_idStatistics")
    val idStatistics: Int
)