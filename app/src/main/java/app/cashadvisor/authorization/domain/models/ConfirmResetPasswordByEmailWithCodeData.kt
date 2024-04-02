package app.cashadvisor.authorization.domain.models

data class ConfirmResetPasswordByEmailWithCodeData(
    val message:String,
    val statusCode: Int
)
