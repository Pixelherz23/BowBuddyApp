package com.example.bowbuddyapp.data

import com.google.gson.annotations.SerializedName

/**
 * This is a representation of a game from the database
 *
 * @author Lukas Beckmann, Kai-U. Stieler (co-author)
 *
 * @property idParcours the id of the [Parcours] which is played in the Game
 * @property link the unique link of a game
 * @property gameRuel the rule which is played in the game
 */
data class Game(
    @SerializedName("Parcours_idParcours")
    val idParcours: Int,
    @SerializedName("linkGame")
    val link: String,
    @SerializedName("ruleGame")
    val gameRule: String
)