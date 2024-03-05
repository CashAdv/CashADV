package app.cashadvisor.authorization.domain.impl

import app.cashadvisor.authorization.data.dto.CredentialsDto
import app.cashadvisor.authorization.domain.api.CredentialsRepository
import app.cashadvisor.authorization.domain.api.RegisterInteractor
import app.cashadvisor.authorization.domain.api.RegisterRepository
import app.cashadvisor.authorization.domain.models.ConfirmCode
import app.cashadvisor.authorization.domain.models.Email
import app.cashadvisor.authorization.domain.models.Password
import app.cashadvisor.authorization.domain.models.RegisterData
import app.cashadvisor.common.domain.Resource
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class RegisterInteractorImpl @Inject constructor(
    private val registerRepository: RegisterRepository,
    private val credentialsRepository: CredentialsRepository
) : RegisterInteractor {

    override suspend fun registerByEmail(email: Email, password: Password): Resource<RegisterData> {
        val result = registerRepository.registerByEmail(email, password)
        return when (result) {
            is Resource.Success -> {
                Resource.Success(data = result.data)
            }

            is Resource.Error -> {
                Resource.Error(error = result.error)
            }
        }
    }

    override fun isRegisterInProgress(): Flow<Boolean> {
        return registerRepository.isRegisterInProgress()
    }

    override suspend fun confirmEmailAndRegistrationWithCode(
        email: Email,
        code: ConfirmCode,
    ): Resource<String> {
        val result = registerRepository.confirmRegisterByEmailWithCode(email, code)
        return when (result) {
            is Resource.Success -> {
                credentialsRepository.saveCredentials(
                    CredentialsDto(
                        accessToken = result.data.tokenDetails.accessToken,
                        refreshToken = result.data.tokenDetails.refreshToken
                    )
                )
                Resource.Success(result.data.message)
            }

            is Resource.Error -> {
                Resource.Error(result.error)
            }
        }
    }

}