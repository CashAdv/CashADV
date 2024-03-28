package app.cashadvisor.profile.data

import app.cashadvisor.authorization.data.models.response.customError.ErrorWrongConfirmationCodeResponse
import app.cashadvisor.common.data.models.ErrorResponse
import app.cashadvisor.common.utill.exceptions.LoginException
import app.cashadvisor.common.utill.exceptions.NetworkException
import kotlinx.serialization.json.Json
import javax.inject.Inject

class NetworkToProfileExceptionMapper @Inject constructor(
    private val json: Json
) {

    fun handleExceptionGettingProfile(exception: NetworkException): UserProfileException {
        return when (exception) {
            is NetworkException.Unauthorized -> {
                val errorResponse = handleErrorResponse<ErrorResponse>(exception.errorBody)
                UserProfileException.Profile.UnauthorizedUserNotAuthenticated(
                    message = errorResponse.message,
                    errorResponse.statusCode
                )
            }

            is NetworkException.InternalServerError -> {
                val errorResponse = handleErrorResponse<ErrorResponse>(exception.errorBody)
                UserProfileException.Profile.InternalServerErrorFailedToRetrieve(
                    errorResponse.message,
                    errorResponse.statusCode
                )
            }

            else -> handleCommonException(exception)

        }
    }

    fun handleExceptionUpdatingProfile(exception: NetworkException): UserProfileException {
        return when (exception) {
            is NetworkException.BadRequest -> {
                val errorResponse = handleErrorResponse<ErrorResponse>(exception.errorBody)
                UserProfileException.Profile.BadRequestInvalidInputOrContentType(
                    errorResponse.message,
                    errorResponse.statusCode
                )
            }

            is NetworkException.Unauthorized -> {
                val errorResponse =
                    handleErrorResponse<ErrorWrongConfirmationCodeResponse>(exception.errorBody)
                UserProfileException.Profile.UnauthorizedUserNotAuthenticated(
                    message = errorResponse.error,
                    statusCode = errorResponse.statusCode
                )
            }

            is NetworkException.InternalServerError -> {
                val errorResponse =
                    handleErrorResponse<ErrorResponse>(exception.errorBody)
                UserProfileException.Profile.InternalServerErrorFailedToUpload(
                    message = errorResponse.message,
                    statusCode = errorResponse.statusCode
                )
            }

            else -> handleCommonException(exception)
        }
    }

    private fun handleCommonException(exception: NetworkException): UserProfileException {
        return when (exception) {
            is NetworkException.NoInternetConnection -> {
                val errorResponse = handleErrorResponse<ErrorResponse>(exception.errorBody)
                UserProfileException.NoConnection(errorResponse.message)
            }

            is NetworkException.Undefined -> {
                UserProfileException.Undefined(message = exception.errorBody)
            }

            else -> {
                UserProfileException.Undefined(message = exception.errorBody)
            }
        }
    }

    private inline fun <reified T> handleErrorResponse(errorMessage: String): T {
        try {
            return json.decodeFromString<T>(errorMessage)

        } catch (e: Exception) {
            throw LoginException.Undefined()
        }
    }
}