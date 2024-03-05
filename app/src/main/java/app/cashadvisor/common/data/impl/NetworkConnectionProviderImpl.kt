package app.cashadvisor.common.data.impl

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import app.cashadvisor.common.data.api.NetworkConnectionProvider

class NetworkConnectionProviderImpl(
    private val context: Context
) : NetworkConnectionProvider {

    override fun isConnected(): Boolean {
        val connectivityManager =
            context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val capabilities =
            connectivityManager.getNetworkCapabilities(connectivityManager.activeNetwork)
        if (capabilities != null) {
            when {
                capabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) || capabilities.hasTransport(
                    NetworkCapabilities.TRANSPORT_WIFI
                ) || capabilities.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET) -> return true
            }
        }
        return false
    }

}