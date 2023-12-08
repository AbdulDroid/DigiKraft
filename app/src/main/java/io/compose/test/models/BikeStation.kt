package io.compose.test.models

import com.google.gson.Gson

data class BikeStation (
    val id: String,
    val index: Int = 0,
    val name: String,
    val coordinates: List<Double>,
    val bikes: String,
    val freeRacks: String
)

fun String.fromJson(): BikeStation = Gson().fromJson(this, BikeStation::class.java)

fun BikeStation.toJson(): String = Gson().toJson(this)