package io.compose.test.model.repositories

import io.compose.test.BuildConfig
import io.compose.test.model.models.toBikeStation
import javax.inject.Inject
import javax.inject.Singleton

import io.compose.test.model.remote.ApiService
import io.compose.test.model.remote.Network
import io.compose.test.models.BikeStation
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

@Singleton
class BikeStationRepository @Inject constructor (
    private val api: ApiService,
    private val networkUtil: Network
) {
    fun getData(): Flow<List<BikeStation>> =
       flow {
            if (networkUtil.hasInternet()) {
                val resp = api.fetchData(BuildConfig.BIKE_FETCH_URL)
                if (resp.isSuccessful && resp.body()?.features?.isNotEmpty() == true) {
                    val data = resp.body()?.features?.map {
                        it.toBikeStation()
                    } ?: emptyList()
                    emit(data)
                } else {
                    throw Exception(resp.errorBody()?.string() ?: "An error occurred")
                }
            } else {
                throw Exception("No Internet Connection")
            }
        }
}