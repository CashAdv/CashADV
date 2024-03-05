package app.cashadvisor.authorization.data.models.request

import kotlinx.serialization.Serializable

@Serializable
data class RegisterByEmailRequest(
    val email: String,
    val password: String
)