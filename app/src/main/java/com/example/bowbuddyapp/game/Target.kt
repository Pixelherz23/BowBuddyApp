package com.example.bowbuddyapp.game

data class Target(
    var name: String,
    var station: String,

    var pointsPerArrow: IntArray = IntArray(3),
    var points: Int,
)
