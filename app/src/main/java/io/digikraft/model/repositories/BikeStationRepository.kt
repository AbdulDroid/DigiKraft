package io.digikraft.model.repositories;

import io.digikraft.BuildConfig
import io.digikraft.model.models.toBikeStation
import javax.inject.Inject;
import javax.inject.Singleton;

import io.digikraft.model.remote.ApiService;
import io.digikraft.model.remote.Network;
import io.digikraft.models.BikeStation
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.withContext

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