package com.example.bowbuddyapp.data


import com.google.gson.annotations.SerializedName

/**
 * This is a representation of points from the database
 *
 * @author Lukas Beckmann, Kai-U. Stieler (co-author)
 *
 * @property points the poinst of the parcours
 * @property link the link of the [Game] to which the points are assigned
 * @property target the id of the [Target] to which the points are assigned
 * @property email the email of the [User] to which the points are assigned
 */
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