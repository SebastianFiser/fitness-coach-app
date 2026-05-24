package com.sebastianfiser.fitnesscoach.models

import com.sebastianfiser.fitnesscoach.models.Exercise
import com.sebastianfiser.fitnesscoach.models.Day 

val weekData = listOf(
    Day("Monday", listOf(
        Exercise("Bench press",105.0f, 4, 10),
        Exercise("Peck deck 20x3",50.0f, 20, 3),
        Exercise("Dumbell flyes 10x4",35.0f, 10, 4),
        Exercise("Cable cross 15x3",40.0f, 15, 3),
    )),
    Day("Tuesday", listOf(
        Exercise("Squats",135.0f, 20, 3),
        Exercise("Leg press",180.0f, 15, 4),
        Exercise("Lunges",60.0f, 12, 3)
    )),
    Day("Wednesday", listOf(
        Exercise("Deadlift",185.0f, 10, 4),
        Exercise("Barbell row",95.0f, 12, 3),
        Exercise("Lat pulldown",70.0f, 15, 4)
    )),
    Day("Thursday", listOf(
        Exercise("Overhead press",60.0f, 12, 4),
        Exercise("Lateral raises",20.0f, 15, 3),
        Exercise("Front raises",25.0f, 10, 4)
    )),
    Day("Friday", listOf(
        Exercise("Barbell curls",40.0f, 12, 4),
        Exercise("Hammer curls",30.0f, 8, 2),
        Exercise("Preacher curls",35.0f, 10, 4)
    )),
    Day("Saturday", listOf(
        Exercise("Tricep pushdown",50.0f, 12, 4),
        Exercise("Overhead tricep extension",40.0f, 15, 3),
        Exercise("Dips 10x4", 0.0f, 8, 4)
    )),
    Day("Sunday", listOf(
        Exercise("Rest day", 0.0f, 0, 0)
    ))
)