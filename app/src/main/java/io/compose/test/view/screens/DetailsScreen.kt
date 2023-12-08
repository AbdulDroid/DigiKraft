package io.compose.test.view.screens

import android.location.Location
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.material3.TopAppBarDefaults.topAppBarColors
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.Marker
import com.google.maps.android.compose.*
import io.compose.test.R
import io.compose.test.models.BikeStation
import io.compose.test.utils.checkLocationPermission
import io.compose.test.view.components.BikeStationCell
import io.compose.test.view.components.MapMarker

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DetailsScreen(
    userLocation: Location?,
    data: BikeStation,
    onBack: () -> Unit,
) {
    val current = LatLng(data.coordinates[1], data.coordinates[0])
    var isMapLoaded by remember { mutableStateOf(false) }
    val cameraPositionState = rememberCameraPositionState {
        position = CameraPosition.fromLatLngZoom(current, 15.47f)
    }
    Scaffold(
        topBar = {
            TopAppBar(
                title = {},
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.Default.ArrowBack, contentDescription = null, tint = Color.White)
                    }
                },
                colors = topAppBarColors(
                    containerColor = Color.Black,
                    scrolledContainerColor = Color.Black
                )
            )
        }
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(it)
        ) {
            GoogleMapView(
                Modifier.matchParentSize(),
                data.name,
                data.bikes,
                current,
                onMapLoaded = {
                    isMapLoaded = true
                },
                cameraPositionState = cameraPositionState
            )
            BikeStationCell(
                modifier = Modifier
                    .fillMaxWidth()
                    .align(Alignment.BottomCenter),
                data = data,
                userLocation = userLocation
            ) {

            }
        }
    }
}

@Composable
fun GoogleMapView(
    modifier: Modifier,
    title: String,
    count: String,
    current: LatLng,
    cameraPositionState: CameraPositionState,
    onMapLoaded: () -> Unit
) {
    val context = LocalContext.current

    val uiSettings by remember {
        mutableStateOf(
            MapUiSettings(
                compassEnabled = false,
                rotationGesturesEnabled = false,
                tiltGesturesEnabled = false,
                myLocationButtonEnabled = true
            )
        )
    }
    val mapProperties by remember {
        mutableStateOf(
            MapProperties(
                mapType = MapType.NORMAL,
                isMyLocationEnabled = context.checkLocationPermission()
            )
        )
    }

    GoogleMap(
        modifier,
        cameraPositionState,
        properties = mapProperties,
        uiSettings = uiSettings,
        onMapLoaded = onMapLoaded,
    ) {
        val onMarkerClicked: (Marker) -> Boolean = {
            it.hideInfoWindow()
            cameraPositionState.projection?.let { _ ->
                it.hideInfoWindow()
            }
            false
        }
        MapMarker(
            context = context,
            position = MarkerState(position = current),
            title = title,
            count = count,
            layoutResId = R.layout.custom_marker,
            onMarkerClick = onMarkerClicked
        )
    }
}