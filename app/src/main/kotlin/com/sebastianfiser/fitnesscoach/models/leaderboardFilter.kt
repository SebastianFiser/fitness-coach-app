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
            if(entry.lift == parameter) {
                sortedDataList.add(entry)
            }
        } else {
            val min = sortedDataList.minByOrNull { it.weight }
            if (min != null && entry.weight > min.weight) {
                if(entry.lift == parameter) {
                    sortedDataList.remove(min)
                    sortedDataList.add(entry)
                }
            }
        }
    }
    return sortedDataList
}