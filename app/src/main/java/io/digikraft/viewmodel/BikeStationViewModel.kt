package io.digikraft.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import io.digikraft.model.repositories.BikeStationRepository
import io.digikraft.models.BikeStation
import io.digikraft.utils.updateValue
import kotlinx.coroutines.flow.*
import javax.inject.Inject

@HiltViewModel
class BikeStationViewModel @Inject constructor(
    private val repo: BikeStationRepository
) : ViewModel() {

    private val _state: MutableStateFlow<State> = MutableStateFlow(State())
    val state = _state.asStateFlow()

    private val _shouldRequestLocation: MutableStateFlow<Boolean> = MutableStateFlow(true)
    val shouldRequestLocation = _shouldRequestLocation.asStateFlow()

    init {
        getBikeStations()
    }

    fun getBikeStations() {
        _state.updateValue { copy(bikeStations = emptyList(), error = null) }
        repo.getData()
            .catch {
                _state.updateValue { copy(error = it) }
            }
            .onEach {
                _state.updateValue { copy(bikeStations = it) }
            }
            .launchIn(viewModelScope)
    }

    fun setShouldRequestLocation(request: Boolean) {
        _shouldRequestLocation.value = request
    }

    data class State(
        val bikeStations: List<BikeStation> = emptyList(),
        val error: Throwable? = null
    )
}