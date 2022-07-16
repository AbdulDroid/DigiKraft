package io.digikraft.model.remote

interface Network {
    suspend fun hasInternet(): Boolean
}