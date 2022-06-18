package com.example.bowbuddyapp.data

import com.google.gson.annotations.SerializedName

data class PointsParcours(
    @SerializedName("pointsParcours")
    val points: Int,
    @SerializedName("Game_linkGame")
    val link: String,
    @SerializedName("Parcours_idParcours")
    val parcours: Int,
    @SerializedName("User_emailUser")
    val email: String
)
