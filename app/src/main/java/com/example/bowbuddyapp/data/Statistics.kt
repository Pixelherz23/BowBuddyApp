package com.example.bowbuddyapp.data

import com.google.gson.annotations.SerializedName

/**
 * This is a representation of the statistics in the database
 *
 * @author Lukas Beckmann, Kai-U. Stieler (co-author)
 *
 * @property hitProbability the hit probability of a user
 * @property placementFirst the number of first placements of a user
 * @property placementSecond the number of second placements of a user
 * @property placementThird the number of third placements of a user
 */
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