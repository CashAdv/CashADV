package app.cashadvisor.common.data.api

interface NetworkConnectionProvider {
    fun isConnected(): Boolean
}