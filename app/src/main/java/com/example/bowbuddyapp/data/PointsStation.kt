package com.example.bowbuddyapp.data

import com.google.gson.annotations.SerializedName

/**
 * This is a representation of a poinst from a station of the database
 *
 * @author Lukas Beckmann, Kai-U. Stieler (co-author)
 *
 * @property points the poinst of the station
 * @property link the link of the [Game] to which the points are assigned
 * @property station the id of the [Station] to which the points are assigned
 * @property email the email of the [User] to which the points are assigned
 */
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
