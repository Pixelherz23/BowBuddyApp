package com.example.bowbuddyapp.data

import com.google.gson.annotations.SerializedName

data class PointsStation(
    @SerializedName("pointsStation")
    val points: Int,
    @SerializedName("Game_linkGame")
    val link: String,
    @SerializedName("Station_idStation")
    val station: Int,
    @SerializedName("User_emailUser")
    val email: String
)
