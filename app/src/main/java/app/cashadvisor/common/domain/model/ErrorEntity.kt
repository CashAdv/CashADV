package app.cashadvisor.common.domain.model

sealed class ErrorEntity(open val message: String) {
    data class UnknownError(override val message: String = BLANC_ERROR) : ErrorEntity(message)

    sealed class NetworksError(override val message: String) : ErrorEntity(message) {
        data class NoInternet(override val message: String) : NetworksError(message)
    }

    sealed class Login(override val message: String) : ErrorEntity(message) {
        data class InvalidInput(override val message: String) : Login(message)
        data class InvalidEmailOrPassword(override val message: String) : Login(message)
        data class FailedToGenerateTokenOrSendEmail(override val message: String) :
            Login(message)
    }

    sealed class Register(override val message: String) : ErrorEntity(message) {
        data class InvalidEmail(override val message: String) : Register(message)
        data class FailedToGenerateTokenOrSendEmail(override val message: String) :
            Register(message)
    }

    sealed class LoginConfirmationWithCode(override val message: String) :
        ErrorEntity(message) {
        data class InvalidToken(override val message: String) :
            LoginConfirmationWithCode(message)

        data class WrongConfirmationCode(
            override val message: String,
            val remainingAttempts: Int,
            val lockDuration: Long
        ) : LoginConfirmationWithCode(message)

        data class FailedToConfirmEmailOrLoginUser(override val message: String) :
            LoginConfirmationWithCode(message)
    }

    sealed class RegisterConfirmationWithCode(override val message: String) :
        ErrorEntity(message) {
        data class InvalidToken(override val message: String) :
            RegisterConfirmationWithCode(message)

        data class WrongConfirmationCode(
            override val message: String,
            val remainingAttempts: Int,
            val lockDuration: Long
        ) : RegisterConfirmationWithCode(message)

        data class FailedToConfirmEmailOrRegisterUser(override val message: String) :
            RegisterConfirmationWithCode(message)
    }

    sealed class ConfirmEmailToResetPassword(override val message: String) :
        ErrorEntity(message) {
        data class InvalidInput(override val message: String) : ConfirmEmailToResetPassword(message)
        data class FailedToGenerateTokenOrSendEmail(override val message: String) :
            ConfirmEmailToResetPassword(message)

    }

    sealed class ConfirmResetPasswordByEmailWithCode(override val message: String) :
        ErrorEntity(message) {
        data class InvalidInput(override val message: String) :
            ConfirmResetPasswordByEmailWithCode(message)

        data class WrongConfirmationCode(
            override val message: String,
            val remainingAttempts:Int? = null,
            val lockDuration:Long? = null
        ) : ConfirmResetPasswordByEmailWithCode(message)

        data class FailedToConfirmPasswordReset(override val message: String) :
            ConfirmResetPasswordByEmailWithCode(message)
    }

    sealed class SaveNewPassword(override val message: String) : ErrorEntity(message) {

        data class InvalidInput(override val message: String) :
            SaveNewPassword(message)

        data class InvalidToken(override val message: String) :
            SaveNewPassword(message)

        data class FailedToResetPassword(override val message: String) :
            SaveNewPassword(message)
    }



    sealed class Profile(override val message: String) : ErrorEntity(message) {
        data class InvalidContent(override val message: String) : Profile(message)
        data class UserNotAuthorized(override val message: String) : Profile(message)
        data class FailedToGetData(override val message: String) :
            Profile(message)

        data class FailedToSaveData(override val message: String) :
            Profile(message)

        data class EmptyProfile(override val message: String = BLANC_ERROR) : Profile(message)
    }

    companion object {
        const val BLANC_ERROR = ""
    }

}
