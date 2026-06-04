package com.sebastianfiser.fitnesscoach.models

import com.sebastianfiser.fitnesscoach.models.Exercise
import com.sebastianfiser.fitnesscoach.models.Day 

val weekData = listOf(
    Day("Monday", listOf(
        Exercise("Bench press",105.0f, 10, 4),
        Exercise("Peck deck ",50.0f, 3, 20),
        Exercise("Dumbell flyes ",35.0f, 4, 10),
        Exercise("Cable cross ",40.0f, 3, 15),
    )),
    Day("Tuesday", listOf(
        Exercise("Squats",135.0f, 3, 20),
        Exercise("Leg press",180.0f, 4, 15),
        Exercise("Lunges",60.0f, 3, 12)
    )),
    Day("Wednesday", listOf(
        Exercise("Deadlift",185.0f, 4, 10),
        Exercise("Barbell row",95.0f, 3, 12),
        Exercise("Lat pulldown",70.0f, 4, 15)
    )),
    Day("Thursday", listOf(
        Exercise("Overhead press",60.0f, 4, 12),
        Exercise("Lateral raises",20.0f, 3, 15),
        Exercise("Front raises",25.0f, 4, 10)
    )),
    Day("Friday", listOf(
        Exercise("Barbell curls",40.0f, 4, 12),
        Exercise("Hammer curls",30.0f, 2, 8),
        Exercise("Preacher curls",35.0f, 4, 10)
    )),
    Day("Saturday", listOf(
        Exercise("Tricep pushdown",50.0f, 4, 12),
        Exercise("Overhead tricep extension",40.0f, 3, 15),
        Exercise("Dips", 0.0f, 4, 8)
    )),
    Day("Sunday", listOf(
        Exercise("Rest day", 0.0f, 0, 0)
    ))
)