package app.cashadvisor.authorization.data

import app.cashadvisor.authorization.data.models.response.customError.ErrorWrongConfirmationCodeResponse
import app.cashadvisor.common.data.models.ErrorResponse
import app.cashadvisor.common.utill.exceptions.LoginException
import app.cashadvisor.common.utill.exceptions.NetworkException
import app.cashadvisor.common.utill.exceptions.ResetPasswordException
import javax.inject.Inject
import kotlinx.serialization.json.Json

class NetworkToResetPasswordExceptionMapper @Inject constructor(
    private val json: Json
) {
    fun handleConfirmResetPasswordByEmailWithCode(exception: NetworkException):ResetPasswordException{
        return when(exception){
            is NetworkException.BadRequest -> {
                val errorResponse = handleErrorResponse<ErrorResponse>(exception.errorBody)
                ResetPasswordException.ConfirmResetPasswordByEmailWithCode.BadRequestInvalidCodeOrMissingContentTypeHeader(
                    message = errorResponse.message,
                    statusCode = errorResponse.statusCode
                )
            }
            is NetworkException.Unauthorized -> {
                val errorResponse = handleErrorResponse<ErrorWrongConfirmationCodeResponse>(exception.errorBody)
                ResetPasswordException.ConfirmResetPasswordByEmailWithCode.UnauthorizedInvalidTokenOrMissingContentTypeHeader(
                    remainingAttempts = errorResponse.remainingAttempts,
                    lockDuration = errorResponse.lockDuration,
                    message = errorResponse.error,
                    statusCode = errorResponse.statusCode
                )
            }
            is NetworkException.InternalServerError -> {
                val errorResponse = handleErrorResponse<ErrorWrongConfirmationCodeResponse>(exception.errorBody)
                ResetPasswordException.ConfirmResetPasswordByEmailWithCode.InternalServerErrorFailedToConfirmResetPassword(
                    message = errorResponse.error,
                    statusCode = errorResponse.statusCode
                )
            }
            else -> handleCommonException(exception)
        }

    }
    fun handleConfirmEmailToResetPassword(exception: NetworkException):ResetPasswordException{
        return when(exception){
            is NetworkException.BadRequest -> {
                val errorResponse = handleErrorResponse<ErrorResponse>(exception.errorBody)
                ResetPasswordException.ConfirmEmailToResetPassword.BadRequestInvalidEmailOrMissingContentTypeHeader(
                    message = errorResponse.message,
                    statusCode = errorResponse.statusCode
                )
            }
            is NetworkException.InternalServerError -> {
                val errorResponse = handleErrorResponse<ErrorResponse>(exception.errorBody)
                ResetPasswordException.ConfirmEmailToResetPassword.InternalServerErrorFailedToGenerateTokenOrSendEmail(
                    message = errorResponse.message,
                    statusCode = errorResponse.statusCode
                )
            }
            else -> handleCommonException(exception)
        }

    }
    fun handleSaveNewPassword(exception: NetworkException):ResetPasswordException{
        return when(exception){
            is NetworkException.BadRequest -> {
                val errorResponse = handleErrorResponse<ErrorResponse>(exception.errorBody)
                ResetPasswordException.SaveNewPassword.BadRequestInvalidPasswordOrMissingContentTypeHeader(
                    message = errorResponse.message,
                    statusCode = errorResponse.statusCode
                )
            }
            is NetworkException.Unauthorized -> {
                val errorResponse = handleErrorResponse<ErrorWrongConfirmationCodeResponse>(exception.errorBody)
                ResetPasswordException.SaveNewPassword.UnauthorizedInvalidTokenOrMissingContentTypeHeader(
                    message = errorResponse.error,
                    statusCode = errorResponse.statusCode
                )
            }
            is NetworkException.InternalServerError -> {
                val errorResponse = handleErrorResponse<ErrorResponse>(exception.errorBody)
                ResetPasswordException.SaveNewPassword.InternalServerErrorFailedToResetPassword(
                    message = errorResponse.message,
                    statusCode = errorResponse.statusCode
                )
            }
            else -> handleCommonException(exception)
        }
    }

    private fun handleCommonException(exception: NetworkException): ResetPasswordException {
        return when (exception) {
            is NetworkException.NoInternetConnection -> {
                val errorResponse = handleErrorResponse<ErrorResponse>(exception.errorBody)
                ResetPasswordException.NoConnection(errorResponse.message)
            }

            is NetworkException.Undefined -> {
                ResetPasswordException.Undefined()
            }

            else -> {
                ResetPasswordException.Undefined()
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