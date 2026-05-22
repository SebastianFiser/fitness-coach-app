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
            if (parameter in listOf("Squat", "Bench Press", "Deadlift")) {
                if(entry.lift == parameter) {
                    sortedDataList.add(entry)
                }
            } else if (parameter in listOf("Under 18", "18-25", "26-35", "36-45", "46+")) {
                val matchesAge = when(parameter) {
                    "Under 18" -> entry.age < 18
                    "18-25" -> entry.age 18..25
                    "26-35" -> entry.age 26..35
                    "36-45" -> entry.age 36..45
                    "46+" -> entry.age > 46
                    else -> false
                }
                if (matchesAge) sortedDataList.add(entry)
            } else if (parameter in listOf("Male", "Female", "OtherGen")) {
                if(parameter == "Male" && entry.gender == 1) {
                    sortedDataList.add(entry)
                } else if(parameter == "Female" && entry.gender == 2) {
                    sortedDataList.add(entry)
                } else if(parameter == "OtherGen" && entry.gender == 3) {
                    sortedDataList.add(entry)
                }
            } else if (parameter in listOf("Natural", "Enhanced")) {
                if(parameter == "Natural" && entry.natural) {
                    sortedDataList.add(entry)
                } else if(parameter == "Enhanced" && !entry.natural) {
                    sortedDataList.add(entry)
                }
            } else if (parameter in listOf("USA", "UK", "GE", "CZ", "Other")) {
                if(entry.nationality == parameter) {
                    sortedDataList.add(entry)
                }
            }
        } else {
            val min = sortedDataList.minByOrNull { it.weight }
            if (min != null && entry.weight > min.weight) {
                if (parameter in listOf("Squat", "Bench Press", "Deadlift")) {
                    if(entry.lift == parameter) {
                        sortedDataList.add(entry)
                    }
                } else if (parameter in listOf("Under 18", "18-25", "26-35", "36-45", "46+")) {
                val matchesAge = when(parameter) {
                    "Under 18" -> entry.age < 18
                    "18-25" -> entry.age 18..25
                    "26-35" -> entry.age 26..35
                    "36-45" -> entry.age 36..45
                    "46+" -> entry.age > 46
                    else -> false
                }
                if (matchesAge) sortedDataList.add(entry)
                } else if (parameter in listOf("Male", "Female", "OtherGen")) {
                    if(parameter == "Male" && entry.gender == 1) {
                        sortedDataList.add(entry)
                    } else if(parameter == "Female" && entry.gender == 2) {
                        sortedDataList.add(entry)
                    } else if(parameter == "OtherGen" && entry.gender == 3) {
                        sortedDataList.add(entry)
                    }
                } else if (parameter in listOf("Natural", "Enhanced")) {
                    if(parameter == "Natural" && entry.natural) {
                        sortedDataList.add(entry)
                    } else if(parameter == "Enhanced" && !entry.natural) {
                        sortedDataList.add(entry)
                    }
                } else if (parameter in listOf("USA", "UK", "GE", "CZ", "Other")) {
                    if(entry.nationality == parameter) {
                        sortedDataList.add(entry)
                    }
                }
            }
        }
    }
    return sortedDataList
}