package io.digikraft.model.remote

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Build
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.IOException
import java.net.InetSocketAddress
import java.net.Socket
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class NetworkUtil @Inject constructor(
    @ApplicationContext private val ctx: Context
): Network {
    override suspend fun hasInternet(): Boolean {
        return if (isConnected())
            withContext(Dispatchers.IO) {
                try {
                    // Connect to api to check for connection
                    val timeoutMs = 1500
                    val socket = Socket()
                    val socketAddress = InetSocketAddress("urldefense.proofpoint.com", 443)

                    socket.connect(socketAddress, timeoutMs)
                    socket.close()
                    true
                } catch (e: IOException) {
                    e.printStackTrace()
                    false
                }
            }
        else false
    }

    @Suppress("DEPRECATION")
    private fun isConnected(): Boolean {
        val cm = ctx.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            val network = cm.activeNetwork ?: return false
            val activeNetwork = cm.getNetworkCapabilities(network) ?: return false
            return when {
                activeNetwork.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) -> true
                activeNetwork.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) -> true
                activeNetwork.hasTransport(NetworkCapabilities.TRANSPORT_BLUETOOTH) -> true
                activeNetwork.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET) -> true
                else -> false
            }
        } else {
            val info = cm.activeNetworkInfo ?: return false
            return info.isConnectedOrConnecting
        }
    }
}