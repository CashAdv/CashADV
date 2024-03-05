package app.cashadvisor.authorization.domain.api

import app.cashadvisor.authorization.domain.models.ConfirmCode
import app.cashadvisor.authorization.domain.models.Email
import app.cashadvisor.authorization.domain.models.LoginAuthenticationData
import app.cashadvisor.authorization.domain.models.LoginData
import app.cashadvisor.authorization.domain.models.Password
import app.cashadvisor.common.domain.Resource
import kotlinx.coroutines.flow.Flow

interface LoginRepository {
    suspend fun loginByEmail(
        email: Email,
        password: Password
    ): Resource<LoginData>

    suspend fun confirmLoginByEmailWithCode(
        email: Email,
        code: ConfirmCode,
    ): Resource<LoginAuthenticationData>

    fun isLoginInProgress(): Flow<Boolean>
}