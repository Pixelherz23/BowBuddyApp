package com.example.bowbuddyapp.data

import com.google.gson.annotations.SerializedName
import kotlin.annotation.Target

/**
 * This is a representation of a poinst from a parcour of the database
 *
 * @author Lukas Beckmann, Kai-U. Stieler (co-author)
 *
 * @property points the poinst of the parcours
 * @property link the link of the [Game] to which the points are assigned
 * @property parcours the id of the [Parcours] to which the points are assigned
 * @property email the email of the [User] to which the points are assigned
 */
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
