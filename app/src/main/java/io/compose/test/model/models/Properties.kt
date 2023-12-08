package io.compose.test.model.models


import com.google.gson.annotations.SerializedName

data class Properties(
    @SerializedName("bike_racks")
    val bikeRacks: String,
    val bikes: String,
    @SerializedName("free_racks")
    val freeRacks: String,
    val label: String,
    val updated: String
)