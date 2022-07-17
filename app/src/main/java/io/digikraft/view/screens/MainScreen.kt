package io.digikraft.view.screens

import android.location.Location
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import io.digikraft.R
import io.digikraft.models.BikeStation
import io.digikraft.view.components.BikeStationCell
import io.digikraft.view.components.LocationPermissionUI
import io.digikraft.viewmodel.BikeStationViewModel
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainScreen(
    vm: BikeStationViewModel = hiltViewModel(),
    location: Location? = null,
    onItemClick: (BikeStation) -> Unit,
) {
    val scope = rememberCoroutineScope()
    val viewState by vm.state.collectAsState()
    val locationState by vm.shouldRequestLocation.collectAsState()
    val context = LocalContext.current
    val snackBarHostState = remember { SnackbarHostState() }

    Scaffold(
        topBar = {
            SmallTopAppBar(
                title = {

                },
                colors = TopAppBarDefaults.smallTopAppBarColors(containerColor = Color.Black)
            )
        },
        snackbarHost = { SnackbarHost(snackBarHostState) },
        content = {
            if (locationState) {
                LocationPermissionUI(context = context,
                    permissionRationale =
                    stringResource(id = R.string.location_permission_rationale),
                    hostState = snackBarHostState,
                    permissionAction = { _ ->
                        vm.setShouldRequestLocation(false)
                    })
            }
            if (viewState.bikeStations.isEmpty() && viewState.error == null) {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(it)
                ) {
                    CircularProgressIndicator(
                        Modifier
                            .wrapContentSize()
                            .align(Alignment.Center)
                    )
                }
            } else if (viewState.error != null) {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(it)
                ) {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally, modifier = Modifier
                            .wrapContentSize()
                            .align(Alignment.Center)
                    ) {
                        Text(
                            viewState.error?.localizedMessage ?: "An error occurred",
                        )
                        Spacer(Modifier.height(16.dp))
                        Button(onClick = {
                            vm.getBikeStations()
                        }) {
                            Text(text = "Retry")
                        }
                    }
                }
            } else {
                LazyColumn(modifier = Modifier.padding(it)) {
                    itemsIndexed(items = viewState.bikeStations) { index, station ->
                        BikeStationCell(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(
                                    start = 16.dp, end = 16.dp,
                                    top = if (index == 0) {
                                        16.dp
                                    } else {
                                        6.dp
                                    },
                                    bottom = if (index == viewState.bikeStations.size - 1) {
                                        16.dp
                                    } else {
                                        6.dp
                                    }
                                ),
                            cornerRadius = 12.dp,
                            userLocation = location,
                            data = station.copy(index = index + 1),
                            onItemClick = onItemClick
                        )
                    }
                }
            }
        }
    )
}