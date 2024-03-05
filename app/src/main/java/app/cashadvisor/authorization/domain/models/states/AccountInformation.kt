package app.cashadvisor.authorization.domain.models.states

sealed class AccountInformation {
    object NotAuthorized : AccountInformation()
    data class Authorized(
        val accessToken: String,
        val refreshToken: String
    ) : AccountInformation()
}