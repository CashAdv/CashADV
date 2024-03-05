package app.cashadvisor.authorization.domain

import app.cashadvisor.common.domain.BaseExceptionToErrorMapper
import app.cashadvisor.common.domain.model.ErrorEntity
import app.cashadvisor.common.utill.exceptions.RegisterException
import javax.inject.Inject

class RegisterExceptionToErrorMapper @Inject constructor() : BaseExceptionToErrorMapper() {

    override fun handleSpecificException(
        exception: Exception,
    ): ErrorEntity {
        return when (exception) {
            is RegisterException.Register -> handleRegisterException(exception)
            is RegisterException.EmailCodeConfirmation -> handleEmailConfirmCodeException(exception)
            else -> {
                handleUnknownError(exception)
            }
        }
    }

    private fun handleRegisterException(exception: RegisterException.Register): ErrorEntity {
        return when (exception) {

            is RegisterException.Register.BadRequestInvalidEmailOrMissingContentTypeHeader -> {
                ErrorEntity.Register.InvalidEmail(message = exception.message)
            }

            is RegisterException.Register.InternalServerErrorFailedToGenerateTokenOrSendEmail -> {
                ErrorEntity.Register.FailedToGenerateTokenOrSendEmail(message = exception.message)
            }
        }
    }

    private fun handleEmailConfirmCodeException(exception: RegisterException.EmailCodeConfirmation): ErrorEntity {
        return when (exception) {
            is RegisterException.EmailCodeConfirmation.BadRequestInvalidTokenOrMissingContentTypeHeade -> {
                ErrorEntity.RegisterConfirmationWithCode.InvalidToken(exception.message)
            }

            is RegisterException.EmailCodeConfirmation.UnauthorizedWrongConfirmationCode ->
                ErrorEntity.RegisterConfirmationWithCode.WrongConfirmationCode(
                    message = exception.message,
                    remainingAttempts = exception.remainingAttempts,
                    lockDuration = exception.lockDuration
                )

            is RegisterException.EmailCodeConfirmation.InternalServerErrorFailedToConfirmEmailOrRegisterUser -> {
                ErrorEntity.RegisterConfirmationWithCode.FailedToConfirmEmailOrRegisterUser(
                    exception.message
                )
            }
        }
    }

}
