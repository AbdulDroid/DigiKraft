package io.digikraft.model.remote

import io.digikraft.model.models.ApiResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Url

interface ApiService {

    @GET
    suspend fun fetchData(@Url url: String): Response<ApiResponse>
}