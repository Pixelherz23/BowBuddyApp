package com.example.bowbuddyapp.data

import com.google.gson.annotations.SerializedName


/**
 * This is a representation of a parcours from the database
 *
 * @author Lukas Beckmann, Kai-U. Stieler (co-author)
 *
 * @property email the google email of the user
 * @property name the google name of the user
 * @property profilImage the google profile image of the user
 * @property idGame the link of the [Game] when the user is playing a game
 * @property idStatistics the id of the [Statistics] of a user
 */
data class User(
    @SerializedName("emailUser")
    val email: String,
    @SerializedName("nameUser")
    val name: String,
    @SerializedName("imageUser")
    val profilImage: String,
    @SerializedName("Game_idGame")
    val idGame: String,
    @SerializedName("Statistics_idStatistics")
    val idStatistics: Int
)