package io.digikraft.view.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import io.digikraft.models.BikeStation
import io.digikraft.view.components.BikeStationCell
import io.digikraft.viewmodel.BikeStationViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainScreen(
    vm: BikeStationViewModel = hiltViewModel(),
    onItemClick: (BikeStation) -> Unit,
) {
    val viewState = vm.state.collectAsState()
    Scaffold(
        topBar = {
            SmallTopAppBar(
                title = {

                },
                colors = TopAppBarDefaults.smallTopAppBarColors(containerColor = Color.Black)
            )
        },
        content = {
            if (viewState.value.bikeStations.isEmpty() && viewState.value.error == null) {
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
            } else if (viewState.value.error != null) {
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
                            viewState.value.error?.localizedMessage ?: "An error occurred",
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
                    itemsIndexed(items = viewState.value.bikeStations) { index, station ->
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
                                    bottom = if (index == viewState.value.bikeStations.size - 1) {
                                        16.dp
                                    } else {
                                        6.dp
                                    }
                                ),
                            cornerRadius = 12.dp,
                            data = station.copy(index = index + 1),
                            onItemClick = onItemClick
                        )
                    }
                }
            }
        }
    )
}