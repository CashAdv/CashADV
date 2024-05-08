package app.cashadvisor.authorization.domain

import app.cashadvisor.common.domain.BaseExceptionToErrorMapper
import app.cashadvisor.common.domain.model.ErrorEntity
import app.cashadvisor.common.utill.exceptions.ResetPasswordException
import javax.inject.Inject

class ResetPasswordExceptionToErrorMapper @Inject constructor():BaseExceptionToErrorMapper() {
    override fun handleSpecificException(exception: Exception): ErrorEntity {
        return when (exception){
            is  ResetPasswordException.ConfirmEmailToResetPassword ->handleConfirmEmailToResetPasswordException(exception)
            is ResetPasswordException.SaveNewPassword -> handleSaveNewPasswordException(exception)
            is ResetPasswordException.ConfirmResetPasswordByEmailWithCode -> handleConfirmResetPasswordByEmailWithCode(exception)
            else -> handleUnknownError(exception)
        }
    }
    private fun handleConfirmEmailToResetPasswordException(exception: ResetPasswordException.ConfirmEmailToResetPassword):ErrorEntity{
        return when(exception){
            is ResetPasswordException.ConfirmEmailToResetPassword.BadRequestInvalidInputOrContentType -> {
                ErrorEntity.ConfirmEmailToResetPassword.InvalidInput(
                    exception.message
                )
            }
            is ResetPasswordException.ConfirmEmailToResetPassword.InternalServerErrorFailedToGenerateTokenOrSendEmail ->{
                ErrorEntity.ConfirmEmailToResetPassword.FailedToGenerateTokenOrSendEmail(
                    exception.message
                )
            }
        }
    }
    private fun handleSaveNewPasswordException(exception: ResetPasswordException.SaveNewPassword):ErrorEntity{
        return when(exception){
            is ResetPasswordException.SaveNewPassword.BadRequestInvalidPasswordOrMissingContentTypeHeader -> {
                ErrorEntity.SaveNewPassword.InvalidInput(
                    exception.message
                )
            }
            is ResetPasswordException.SaveNewPassword.UnauthorizedInvalidTokenOrMissingContentTypeHeader -> {
                ErrorEntity.SaveNewPassword.InvalidToken(
                    exception.message
                )
            }
            is ResetPasswordException.SaveNewPassword.InternalServerErrorFailedToResetPassword -> {
                ErrorEntity.SaveNewPassword.FailedToResetPassword(
                    exception.message
                )
            }
        }

    }
    private fun handleConfirmResetPasswordByEmailWithCode(exception: ResetPasswordException.ConfirmResetPasswordByEmailWithCode):ErrorEntity{
        return when(exception){
            is ResetPasswordException.ConfirmResetPasswordByEmailWithCode.BadRequestInvalidCodeOrMissingContentTypeHeader -> {
                ErrorEntity.ConfirmResetPasswordByEmailWithCode.InvalidInput(
                    exception.message
                )
            }
            is ResetPasswordException.ConfirmResetPasswordByEmailWithCode.UnauthorizedWrongConfirmationCode -> {
                ErrorEntity.ConfirmResetPasswordByEmailWithCode.WrongConfirmationCode(
                    exception.message,
                    exception.remainingAttempts,
                    exception.lockDuration
                )
            }
            is ResetPasswordException.ConfirmResetPasswordByEmailWithCode.InternalServerErrorFailedToConfirmResetPassword -> {
                ErrorEntity.ConfirmResetPasswordByEmailWithCode.FailedToConfirmPasswordReset(
                    exception.message
                )
            }

        }

    }


}