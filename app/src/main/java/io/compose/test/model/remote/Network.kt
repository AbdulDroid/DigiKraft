package io.compose.test.model.remote

interface Network {
    suspend fun hasInternet(): Boolean
}