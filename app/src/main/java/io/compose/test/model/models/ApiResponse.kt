package io.compose.test.model.models


import com.google.gson.annotations.SerializedName

data class ApiResponse(
    @SerializedName("features")
    val features: List<BikeStationResponse>
)