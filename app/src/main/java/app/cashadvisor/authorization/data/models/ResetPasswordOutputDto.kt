package app.cashadvisor.authorization.data.models


data class ResetPasswordOutputDto(
    val message:String,
    val token:String,
    val statusCode: Int
)
