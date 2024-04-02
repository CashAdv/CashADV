package app.cashadvisor.authorization.data.models

data class ConfirmResetPasswordByEmailWithCodeInputDto(
   val code:String,
    val token:String
)
