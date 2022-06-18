package com.example.bowbuddyapp.data


import com.google.gson.annotations.SerializedName

data class Points(
    @SerializedName("pointsPoints")
    val points: Int,
    @SerializedName("Game_linkGame")
    val link: String,
    @SerializedName("Target_idTarget")
    val target: Int,
    @SerializedName("User_emailUser")
    val email: String
)