package com.sebastianfiser.fitnesscoach.models

import com.sebastianfiser.fitnesscoach.models.GenerateFewTimesLeaderBoardData

fun FilterData(parameter: String?, data: List<LeaderBoardEntry>): List<LeaderBoardEntry> {
    if (parameter == null) {
        return dataToReturn(data)
    }
    return SortAndReturnTop20(data, parameter)
}

fun dataToReturn(data: List<LeaderBoardEntry>): List<LeaderBoardEntry> {
    return data.sortedByDescending { it.weight }.take(20)
}

fun SortAndReturnTop20(data: List<LeaderBoardEntry>, parameter: String): List<LeaderBoardEntry> {
    val sortedDataList = mutableListOf<LeaderBoardEntry>()
    data.forEach { entry ->
        if (sortedDataList.size < 20) {
            if (matchesFilter(entry, parameter)) {
                sortedDataList.add(entry)
            }
        } else {
            val min = sortedDataList.minByOrNull { it.weight }
            if (min != null && entry.weight > min.weight) {
                if(matchesFilter(entry, parameter)) {   
                    sortedDataList.remove(min)
                    sortedDataList.add(entry)
                }
            }
        }
    }
    return sortedDataList.sortedByDescending { it.weight }
}

fun matchesFilter(entry: LeaderBoardEntry, parameter: String): Boolean {
    return when {
        parameter in listOf("Squat", "Bench Press", "Deadlift") -> entry.lift == parameter
        parameter in listOf("Under 18", "18-25", "26-35", "36-45", "46+") -> {
            when(parameter) {
                "Under 18" -> entry.age < 18
                "18-25" -> entry.age in 18..25
                "26-35" -> entry.age in 26..35
                "36-45" -> entry.age in 36..45
                "46+" -> entry.age > 46
                else -> false
            }
        }
        parameter =="Male" -> entry.gender == 1
        parameter =="Female" -> entry.gender == 2
        parameter =="OtherGen" -> entry.gender == 3
        parameter =="Natural" -> entry.natural
        parameter =="Enhanced" -> !entry.natural
        parameter =="USA" -> entry.nationality == "USA"
        parameter =="UK" -> entry.nationality == "UK"
        parameter =="GE" -> entry.nationality == "GE"
        parameter =="CZ" -> entry.nationality == "CZ"
        parameter =="Other" -> entry.nationality == "Other"
        else -> false
    }
}