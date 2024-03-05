package app.cashadvisor.authorization.domain

import app.cashadvisor.common.domain.BaseExceptionToErrorMapper
import app.cashadvisor.common.domain.model.ErrorEntity
import app.cashadvisor.common.utill.exceptions.LoginException
import javax.inject.Inject

class LoginExceptionToErrorMapper @Inject constructor() : BaseExceptionToErrorMapper() {

    override fun handleSpecificException(
        exception: Exception,
    ): ErrorEntity {
        return when (exception) {
            is LoginException.Login -> handleLoginException(exception)
            is LoginException.LoginCodeConfirmation -> handleLoginConfirmCodeException(exception)
            else -> {
                handleUnknownError(exception)
            }
        }
    }

    private fun handleLoginException(exception: LoginException.Login): ErrorEntity {
        return when (exception) {
            is LoginException.Login.BadRequestInvalidInputOrContentType -> {
                ErrorEntity.Login.InvalidInput(
                    exception.message
                )
            }

            is LoginException.Login.UnauthorizedInvalidEmailOrPassword -> {
                ErrorEntity.Login.InvalidEmailOrPassword(
                    exception.message
                )
            }

            is LoginException.Login.InternalServerErrorFailedToGenerateToken -> {
                ErrorEntity.Login.FailedToGenerateTokenOrSendEmail(
                    exception.message
                )
            }
        }
    }

    private fun handleLoginConfirmCodeException(exception: LoginException.LoginCodeConfirmation): ErrorEntity {
        return when (exception) {
            is LoginException.LoginCodeConfirmation.BadRequestInvalidRequestPayload -> {
                ErrorEntity.LoginConfirmationWithCode.InvalidToken(exception.message)
            }

            is LoginException.LoginCodeConfirmation.UnauthorizedWrongConfirmationCode -> {
                ErrorEntity.LoginConfirmationWithCode.WrongConfirmationCode(
                    message = exception.message,
                    remainingAttempts = exception.remainingAttempts,
                    lockDuration = exception.lockDuration
                )
            }

            is LoginException.LoginCodeConfirmation.InternalServerErrorFailedToLogin -> {
                ErrorEntity.LoginConfirmationWithCode.FailedToConfirmEmailOrLoginUser(
                    message = exception.message
                )
            }

        }
    }

}
