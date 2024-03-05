package app.cashadvisor.common.data

import app.cashadvisor.common.utill.exceptions.NetworkException
import java.io.IOException
import javax.inject.Inject

class NetworkErrorCodeToExceptionMapper @Inject constructor() {

    fun getException(errorMessage: String, responseCode: Int): IOException {
        return when (responseCode) {
            400 -> NetworkException.BadRequest(errorMessage, responseCode)
            401 -> NetworkException.Unauthorized(errorMessage, responseCode)
            500 -> NetworkException.InternalServerError(errorMessage, responseCode)
            else -> NetworkException.Undefined(errorMessage, responseCode)
        }
    }

}