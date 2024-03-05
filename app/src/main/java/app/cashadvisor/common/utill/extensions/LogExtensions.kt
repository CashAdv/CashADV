package app.cashadvisor.common.utill.extensions

import timber.log.Timber

fun logNetworkError(message: String?) {
    Timber.tag(NETWORK_ERROR_LOG).d(message)
}

fun logDebugError(message: String?) {
    Timber.tag(ERROR_LOG).d(message)
}

fun logDebugMessage(message: String?) {
    Timber.tag(DEBUG_MESSAGE).d(message)
}

const val NETWORK_ERROR_LOG = "NETWORK_ERROR_LOG"
const val ERROR_LOG = "ERROR_LOG"
const val DEBUG_MESSAGE = "DEBUG_MESSAGE"