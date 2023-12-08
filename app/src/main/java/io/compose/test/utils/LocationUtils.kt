package io.compose.test.utils

import android.content.Context
import android.location.Location
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.model.LatLng

private lateinit var fusedLocationClient: FusedLocationProviderClient

fun Context.getUserLocation(onLocationChanged: (Location?) -> Unit) {
    fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)
    fusedLocationClient.lastLocation
        .addOnSuccessListener { location ->
            onLocationChanged(location)
        }
}

fun computeUserDistance(userLocation: Location?, targetLatLng: LatLng): String {
    val targetLocation = Location("").apply {
        latitude = targetLatLng.latitude
        longitude = targetLatLng.longitude
    }
        val distanceString = if (userLocation == null) {
            "Unknown"
        } else {
            val distanceInMeters = targetLocation.distanceTo(userLocation)
            "${if (distanceInMeters % 1000 >= 1) {
                String.format("%,.2fk", distanceInMeters / 1000)
            }
            else {
                String.format("%,.2f", distanceInMeters)
            }}m"
            //"${distanceInMeters}m"
        }
    return distanceString
}