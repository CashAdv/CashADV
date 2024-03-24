package app.cashadvisor.authorization.data.models

data class SaveNewPasswordInputDto(
    val email:String,
    val password:String,
    val resetToken:String
)