package app.cashadvisor.authorization.data.models.request

import kotlinx.serialization.Serializable

@Serializable
data class ResetPasswordByEmailWithCodeRequest(
    val code: String,
    val token: String
)
