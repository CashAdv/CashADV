package app.cashadvisor.authorization.data.models.request

import kotlinx.serialization.Serializable

@Serializable
data class LoginByEmailRequest(
    val email: String,
    val password: String
)