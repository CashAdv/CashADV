package app.cashadvisor.authorization.domain.impl

import app.cashadvisor.authorization.data.dto.CredentialsDto
import app.cashadvisor.authorization.domain.api.CredentialsRepository
import app.cashadvisor.authorization.domain.api.LoginInteractor
import app.cashadvisor.authorization.domain.api.LoginRepository
import app.cashadvisor.authorization.domain.models.ConfirmCode
import app.cashadvisor.authorization.domain.models.Email
import app.cashadvisor.authorization.domain.models.LoginData
import app.cashadvisor.authorization.domain.models.Password
import app.cashadvisor.common.domain.Resource
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class LoginInteractorImpl @Inject constructor(
    private val loginRepository: LoginRepository,
    private val credentialsRepository: CredentialsRepository
) : LoginInteractor {

    override suspend fun loginByEmail(email: Email, password: Password): Resource<LoginData> {
        val result = loginRepository.loginByEmail(email, password)
        return when (result) {
            is Resource.Success -> {
                Resource.Success(data = result.data)
            }

            is Resource.Error -> {
                Resource.Error(error = result.error)
            }
        }
    }

    override suspend fun confirmLoginByEmailWithCode(
        email: Email,
        code: ConfirmCode,
    ): Resource<String> {
        val result = loginRepository
            .confirmLoginByEmailWithCode(email, code)
        return when (result) {

            is Resource.Success -> {
                credentialsRepository.saveCredentials(
                    CredentialsDto(
                        accessToken = result.data.tokenDetails.accessToken,
                        refreshToken = result.data.tokenDetails.refreshToken
                    )
                )
                Resource.Success(data = result.data.message)
            }

            is Resource.Error -> {
                Resource.Error(error = result.error)
            }
        }
    }

    override fun isLoginInProgress(): Flow<Boolean> {
        return loginRepository.isLoginInProgress()
    }

}