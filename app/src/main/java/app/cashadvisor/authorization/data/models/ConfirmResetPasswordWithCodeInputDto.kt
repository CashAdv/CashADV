package app.cashadvisor.authorization.data.models

data class ConfirmResetPasswordWithCodeInputDto(
   val code:String,
    val token:String
)
