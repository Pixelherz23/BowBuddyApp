package com.example.bowbuddyapp.data

import com.google.gson.annotations.SerializedName

/**
* This is a representation of a target from the database
*
* @author Lukas Beckmann, Kai-U. Stieler (co-author)
*
* @property id the id of the target
* @property name the name of the target
 * @property imagePath the imagePath of the taget
*/
data class Target(
    @SerializedName("idTarget")
    val id: Int,
    @SerializedName("nameTarget")
    val name: String,
    @SerializedName("imagePathTarget")
    val imagePath: String
)