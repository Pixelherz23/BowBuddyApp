package com.example.bowbuddyapp.data

import com.google.gson.annotations.SerializedName

/**
 * This is a representation of a parcours from the database
 *
 * @author Lukas Beckmann, Kai-U. Stieler (co-author)
 *
 * @property id the id of the parcours
 * @property name the name of the parcours
 * @property address the address of the parcours
 * @property price the price of the parcours
 * @property info the additional info of the parcours
 * @property email the email from the owner([User]) of the parcours
 */
data class Parcours(
    @SerializedName("idParcours")
    val id: Int,
    @SerializedName("nameParcours")
    val name: String,
    @SerializedName("addressParcours")
    val address: String,
    @SerializedName("priceParcours")
    val price: String,
    @SerializedName("infoParcours")
    val info: String,
    @SerializedName("User_emailUser")
    val email: String



)