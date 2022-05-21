package com.example.bowbuddyapp.data

import com.google.gson.annotations.SerializedName

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
    val info: String



)