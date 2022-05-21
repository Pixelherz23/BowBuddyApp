package com.example.bowbuddyapp.data

import com.google.gson.annotations.SerializedName

data class Statistics(
    @SerializedName("hitProbabilityStatistics")
    val hitProbability: Int,
    @SerializedName("placementFirstStatistics")
    val placementFirst: Int,
    @SerializedName("placementSecondStatistics")
    val placementSecond: Int,
    @SerializedName("placementThirdStatistics")
    val placementThird: Int
)