package com.sebastianfiser.fitnesscoach.models

import com.sebastianfiser.fitnesscoach.models.GenerateFewTimesLeaderBoardData

fun FilterData(parameter: String?): List<LeaderBoardEntry> {
    val data = GenerateFewTimesLeaderBoardData()
    if (parameter == null) return data
    val filteredData = when(parameter) {
        "Squat" -> data.filter { it.lift == "Squat" }
        "Bench Press" -> data.filter { it.lift == "Bench Press" }
        "Deadlift" -> data.filter { it.lift == "Deadlift" }
        else -> data
    }
    return filteredData.sortedByDescending { it.weight }.take(20)
}