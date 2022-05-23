package com.example.bowbuddyapp.data

import com.google.gson.annotations.SerializedName

data class Game(
    @SerializedName("Parcours_idParcours")
    val idParcours: Int,
    @SerializedName("linkGame")
    val link: String,
    @SerializedName("ruleGame")
    val gameRule: String
)