package app.cashadvisor.common.utill.exceptions

import java.io.IOException

sealed class ResetPasswordException(
    override val message: String
) : IOException(message) {

    data class NoConnection(
        override val message: String = NO_INTERNET_CONNECTION
    ) : ResetPasswordException(message = message)

    data class Undefined(override val message: String = UNDEFINED_MESSAGE) :
        ResetPasswordException(message = message)

    sealed class ConfirmResetPasswordByEmailWithCode(
        message: String
    ):ResetPasswordException(message){
        class BadRequestInvalidCodeOrMissingContentTypeHeader(
            override val message: String,
            val statusCode: Int
        ) : ConfirmResetPasswordByEmailWithCode(message = message)

        class UnauthorizedInvalidTokenOrMissingContentTypeHeader(
            override val message: String,
            val remainingAttempts:Int,
            val lockDuration:Int,
            val statusCode: Int
        ):ConfirmResetPasswordByEmailWithCode(message = message)

        class InternalServerErrorFailedToConfirmResetPassword(
            override val message: String,
            val statusCode: Int
        ) : ConfirmResetPasswordByEmailWithCode(message = message)

    }

    sealed class ConfirmEmailToResetPassword(
        message: String
    ):ResetPasswordException(message){
        class BadRequestInvalidEmailOrMissingContentTypeHeader(
            override val message: String,
            val statusCode: Int
        ) : ConfirmEmailToResetPassword(message = message)

        class InternalServerErrorFailedToGenerateTokenOrSendEmail(
            override val message: String,
            val statusCode: Int
        ) : ConfirmEmailToResetPassword(message= message)
    }

    sealed class SaveNewPassword(
        message: String
    ):ConfirmEmailToResetPassword(message){
        class BadRequestInvalidPasswordOrMissingContentTypeHeader(
            override val message: String,
            val statusCode: Int
        ) : SaveNewPassword(message = message)

        class UnauthorizedInvalidTokenOrMissingContentTypeHeader(
            override val message: String,
            val statusCode: Int
        ) : SaveNewPassword(message = message)

        class InternalServerErrorFailedToResetPassword(
            override val message: String,
            val statusCode: Int
        ) : SaveNewPassword(message= message)

    }

    companion object {
        const val NO_INTERNET_CONNECTION = "No internet connection"
        const val UNDEFINED_MESSAGE = "Undefined"
    }
}