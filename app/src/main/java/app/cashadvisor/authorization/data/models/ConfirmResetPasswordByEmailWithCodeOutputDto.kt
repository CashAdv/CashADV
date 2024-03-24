package app.cashadvisor.authorization.data.models


data class ConfirmResetPasswordByEmailWithCodeOutputDto(
    val message:String,
    val token:Int,
    val statusCode: Int
)
