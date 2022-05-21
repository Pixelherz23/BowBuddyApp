package com.example.bowbuddyapp.data

import com.google.gson.annotations.SerializedName

data class Station(
    @SerializedName("idStation")
    val id: Int,
    @SerializedName("nameStation")
    val name: String
)