package io.compose.test.model.models

import io.compose.test.models.BikeStation

data class BikeStationResponse(
    val geometry: Geometry,
    val id: String,
    val properties: Properties,
    val type: String
)

fun BikeStationResponse.toBikeStation() = BikeStation (
    this.id,
    coordinates = this.geometry.coordinates,
    name = this.properties.label,
    bikes = this.properties.bikes,
    freeRacks = this.properties.freeRacks
)