package com.sebastianfiser.fitnesscoach.models

import com.sebastianfiser.fitnesscoach.models.Exercise
import com.sebastianfiser.fitnesscoach.models.Day 

val weekData = listOf(
    Day("Monday", "MONDAY", listOf(
        Exercise("Bench press 12x4",105.0f),
        Exercise("Peck deck 20x3",50.0f),
        Exercise("Dumbell flyes 10x4",35.0f),
        Exercise("Cable cross 15x3",40.0f),
    )),
    Day("Tuesday", "TUESDAY", listOf(
        Exercise("Squats 20x3",135.0f),
        Exercise("Leg press 15x4",180.0f),
        Exercise("Lunges 12x3",60.0f)
    )),
    Day("Wednesday", "WEDNESDAY", listOf(
        Exercise("Deadlift 10x4",185.0f),
        Exercise("Barbell row 12x3",95.0f),
        Exercise("Lat pulldown 15x4",70.0f)
    )),
    Day("Thursday", "THURSDAY", listOf(
        Exercise("Overhead press 12x4",60.0f),
        Exercise("Lateral raises 15x3",20.0f),
        Exercise("Front raises 10x4",25.0f)
    )),
    Day("Friday", "FRIDAY", listOf(
        Exercise("Barbell curls 12x4",40.0f),
        Exercise("Hammer curls 15x3",30.0f),
        Exercise("Preacher curls 10x4",35.0f)
    )),
    Day("Saturday", "SATURDAY", listOf(
        Exercise("Tricep pushdown 12x4",50.0f),
        Exercise("Overhead tricep extension 15x3",40.0f),
        Exercise("Dips 10x4", 0.0f)
    )),
    Day("Sunday", "SUNDAY", listOf(
        Exercise("Rest day", 0.0f)
    ))
)