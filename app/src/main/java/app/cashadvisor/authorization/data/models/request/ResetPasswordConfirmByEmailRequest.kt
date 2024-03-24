package app.cashadvisor.authorization.data.models.request

import kotlinx.serialization.Serializable

@Serializable
data class ResetPasswordConfirmByEmailRequest(
    val email:String
)
