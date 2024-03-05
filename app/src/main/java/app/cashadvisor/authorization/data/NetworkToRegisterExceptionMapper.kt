package app.cashadvisor.authorization.data

import app.cashadvisor.authorization.data.models.response.customError.ErrorWrongConfirmationCodeResponse
import app.cashadvisor.common.data.models.ErrorResponse
import app.cashadvisor.common.utill.exceptions.NetworkException
import app.cashadvisor.common.utill.exceptions.RegisterException
import kotlinx.serialization.json.Json
import javax.inject.Inject

class NetworkToRegisterExceptionMapper @Inject constructor(
    private val json: Json
) {

    fun handleExceptionForRegisterByEmail(exception: NetworkException): RegisterException {
        return when (exception) {
            is NetworkException.BadRequest -> {
                val errorResponse = handleErrorResponse<ErrorResponse>(exception.errorBody)
                RegisterException.Register.BadRequestInvalidEmailOrMissingContentTypeHeader(
                    message = errorResponse.message,
                    errorResponse.statusCode
                )
            }

            is NetworkException.InternalServerError -> {
                val errorResponse = handleErrorResponse<ErrorResponse>(exception.errorBody)
                RegisterException.Register.InternalServerErrorFailedToGenerateTokenOrSendEmail(
                    errorResponse.message,
                    errorResponse.statusCode
                )
            }

            else -> handleCommonException(exception)

        }
    }

    fun handleExceptionForRegisterConfirmationByEmailWithCode(exception: NetworkException): RegisterException {
        return when (exception) {
            is NetworkException.BadRequest -> {
                val errorResponse = handleErrorResponse<ErrorResponse>(exception.errorBody)
                RegisterException.EmailCodeConfirmation.BadRequestInvalidTokenOrMissingContentTypeHeade(
                    errorResponse.message,
                    errorResponse.statusCode
                )
            }

            is NetworkException.Unauthorized -> {
                val errorResponse =
                    handleErrorResponse<ErrorWrongConfirmationCodeResponse>(exception.errorBody)
                RegisterException.EmailCodeConfirmation.UnauthorizedWrongConfirmationCode(
                    remainingAttempts = errorResponse.remainingAttempts,
                    lockDuration = errorResponse.lockDuration,
                    message = errorResponse.error,
                    statusCode = errorResponse.statusCode
                )
            }

            is NetworkException.InternalServerError -> {
                val errorResponse = handleErrorResponse<ErrorResponse>(exception.errorBody)
                RegisterException.EmailCodeConfirmation.InternalServerErrorFailedToConfirmEmailOrRegisterUser(
                    errorResponse.message,
                    errorResponse.statusCode
                )
            }

            else -> handleCommonException(exception)
        }
    }

    private fun handleCommonException(exception: NetworkException): RegisterException {
        return when (exception) {
            is NetworkException.NoInternetConnection -> {
                val errorResponse = handleErrorResponse<ErrorResponse>(exception.errorBody)
                RegisterException.NoConnection(errorResponse.message)
            }

            is NetworkException.Undefined -> {
                RegisterException.Undefined()
            }

            else -> {
                RegisterException.Undefined()
            }
        }
    }

    private inline fun <reified T> handleErrorResponse(errorMessage: String): T {
        try {
            return json.decodeFromString<T>(errorMessage)

        } catch (e: Exception) {
            throw RegisterException.Undefined()
        }
    }

}