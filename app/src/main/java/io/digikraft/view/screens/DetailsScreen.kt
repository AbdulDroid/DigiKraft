package io.digikraft.view.screens

import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.Marker
import com.google.maps.android.compose.*
import io.digikraft.R
import io.digikraft.models.BikeStation
import io.digikraft.utils.checkLocationPermission
import io.digikraft.view.components.BikeStationCell

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DetailsScreen(
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
            SmallTopAppBar(
                title = {},
                colors = TopAppBarDefaults.smallTopAppBarColors(containerColor = Color.Black),
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.Default.ArrowBack, contentDescription = null, tint = Color.White)
                    }
                }
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
                data.bikes,
                current,
                onMapLoaded = {
                    isMapLoaded = true
                },
                cameraPositionState = cameraPositionState
            ) {}
            BikeStationCell(
                modifier = Modifier
                    .fillMaxWidth()
                    .align(Alignment.BottomCenter),
                data = data
            ) {

            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun GoogleMapView(
    modifier: Modifier,
    count: String,
    current: LatLng,
    cameraPositionState: CameraPositionState,
    onMapLoaded: () -> Unit,
    onClickedMarker: (Marker) -> Unit
) {
    val currentPositionState = rememberMarkerState(position = current)
    var circleCentre by remember { mutableStateOf(current) }
    val context = LocalContext.current

    if (currentPositionState.dragState == DragState.END) {
        circleCentre = currentPositionState.position
    }

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
        mutableStateOf(MapProperties(mapType = MapType.NORMAL, isMyLocationEnabled = context.checkLocationPermission()))
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
                onClickedMarker(it)
            }
            false
        }
        MarkerInfoWindowContent(
            state = currentPositionState,
            onClick = onMarkerClicked,
            draggable = false
        ) {
            Row {
                Card(shape = CircleShape, colors = CardDefaults.cardColors(containerColor = Color.White), modifier = Modifier.wrapContentSize()) {
                    Image(painter = painterResource(id = R.drawable.ic_bike), contentDescription = null, modifier = Modifier.size(16.dp))
                }
                Spacer(modifier = Modifier.width(2.dp))
                Text(text = count, style = TextStyle(color = Color.Green, fontWeight = FontWeight.Black, fontSize = 18.sp))
            }

        }
    }
}