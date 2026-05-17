package com.sebastianfiser.fitnesscoach.models

data class LeaderBoardEntry(
    var rank: Int,
    var username: String,
    var lift: String,
    var weight: Float,
    var gender: Int, //1 male 2 female 3 anything else
    var natural: Boolean,
    var age: Int,
    var nationality: String
)