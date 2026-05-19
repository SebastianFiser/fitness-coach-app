package com.sebastianfiser.fitnesscoach.models

import com.sebastianfiser.fitnesscoach.models.LeaderBoardEntry
import kotlin.random.Random
import java.math.BigDecimal
import java.math.RoundingMode

fun GenerateFewTimesLeaderBoardData(): List<LeaderBoardEntry> {
    val list = mutableListOf<LeaderBoardEntry>()
    for (i in 1..500) {
        list.add(generateFakeLeaderboardData(i))
    }
    return list

}

fun generateFakeLeaderboardData(i: Int): LeaderBoardEntry {
    var rank = i
    var name = "User${Random.nextInt(1, 1000)}"
    var lift = listOf("Squat", "Bench Press", "Deadlift").random()
    var weight = Random.nextFloat() * 300
    val RoundedWeight = BigDecimal(weight).setScale(2, RoundingMode.HALF_EVEN).toFloat()
    var gender = Random.nextInt(1, 4)
    var natural = Random.nextBoolean()
    var age = Random.nextInt(15, 60)
    var nationality = listOf("USA", "UK", "Germany", "France", "Czechia", "Other").random()
    return LeaderBoardEntry(rank, name, lift, RoundedWeight.toDouble(), gender, natural, age, nationality)
}