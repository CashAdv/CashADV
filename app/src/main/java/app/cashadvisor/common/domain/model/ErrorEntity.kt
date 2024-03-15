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

    companion object {
        const val BLANC_ERROR = ""
    }

}
