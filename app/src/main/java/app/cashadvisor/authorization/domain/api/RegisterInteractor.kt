package app.cashadvisor.authorization.domain.api

import app.cashadvisor.authorization.domain.models.ConfirmCode
import app.cashadvisor.authorization.domain.models.Email
import app.cashadvisor.authorization.domain.models.Password
import app.cashadvisor.authorization.domain.models.RegisterData
import app.cashadvisor.common.domain.Resource
import kotlinx.coroutines.flow.Flow

interface RegisterInteractor {
    suspend fun registerByEmail(email: Email, password: Password): Resource<RegisterData>

    fun isRegisterInProgress(): Flow<Boolean>

    suspend fun confirmEmailAndRegistrationWithCode(
        email: Email,
        code: ConfirmCode,
    ): Resource<String>

}