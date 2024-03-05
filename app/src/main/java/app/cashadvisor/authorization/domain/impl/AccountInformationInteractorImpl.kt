package app.cashadvisor.authorization.domain.impl

import app.cashadvisor.authorization.domain.api.AccountInformationInteractor
import app.cashadvisor.authorization.domain.api.CredentialsRepository
import app.cashadvisor.authorization.domain.models.states.AccountInformation
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class AccountInformationInteractorImpl(
    private val storage: CredentialsRepository
) : AccountInformationInteractor {
    override suspend fun getAccountInformation(): Flow<AccountInformation> =
        storage.getCredentialsFlow().map { credentials ->
            credentials?.let {
                AccountInformation.Authorized(
                    accessToken = it.accessToken,
                    refreshToken = it.refreshToken
                )
            } ?: AccountInformation.NotAuthorized
        }


    companion object {
        private val MOCK_ACCOUNT_INFORMATION = AccountInformation.Authorized(
            accessToken = "123",
            refreshToken = "123",
        )
    }
}