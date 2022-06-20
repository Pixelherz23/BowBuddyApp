package com.example.bowbuddyapp.data

import com.google.gson.annotations.SerializedName

/**
 * This is a representation of a station from the database
 *
 * @author Lukas Beckmann, Kai-U. Stieler (co-author)
 *
 * @property id the id of the station
 * @property name the name of the station
 */
data class Station(
    @SerializedName("idStation")
    val id: Int,
    @SerializedName("nameStation")
    val name: String
)