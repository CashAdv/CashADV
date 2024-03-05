package app.cashadvisor.common.utill.exceptions

import java.io.IOException

sealed class NetworkException(
    val errorBody: String,
    val httpStatusCode: Int,
) : IOException(errorBody) {

    class BadRequest(errorMessage: String, httpStatusCode: Int) :
        NetworkException(errorMessage, httpStatusCode)

    class Unauthorized(errorMessage: String, httpStatusCode: Int) :
        NetworkException(errorMessage, httpStatusCode)

    class InternalServerError(errorMessage: String, httpStatusCode: Int) :
        NetworkException(errorMessage, httpStatusCode)

    class NoInternetConnection(
        errorMessage: String = NO_INTERNET_CONNECTION,
        httpStatusCode: Int = NO_INTERNET_CONNECTION_CODE
    ) : NetworkException(errorMessage, httpStatusCode)

    class Undefined(
        errorMessage: String = UNDEFINED_MESSAGE,
        httpStatusCode: Int = UNDEFINED_CODE
    ) : NetworkException(errorMessage, httpStatusCode)

    class ResponseBodyIsNull(
        errorMessage: String = RESPONSE_BODY_IS_NULL,
        httpStatusCode: Int
    ) : NetworkException(errorMessage, httpStatusCode)


    companion object {
        const val NO_INTERNET_CONNECTION = "No internet connection"
        const val RESPONSE_BODY_IS_NULL = "Response body is null"
        const val NO_INTERNET_CONNECTION_CODE = -1
        const val UNDEFINED_MESSAGE = "Undefined"
        const val UNDEFINED_CODE = 0
    }
}