package com.example.bowbuddyapp.data

import com.google.gson.annotations.SerializedName

data class Target(
    @SerializedName("idTarget")
    val id: Int,
    @SerializedName("nameTarget")
    val name: String,
    @SerializedName("imagePathTarget")
    val imagePath: String
)