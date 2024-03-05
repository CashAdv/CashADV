package app.cashadvisor.authorization.data.api

import app.cashadvisor.authorization.data.models.AuthByEmailInputDto
import app.cashadvisor.authorization.data.models.ConfirmByEmailWithCodeInputDto
import app.cashadvisor.authorization.data.models.LoginAuthenticationOutputDto
import app.cashadvisor.authorization.data.models.LoginOutputDto

interface LoginRemoteDataSource {
    suspend fun loginByEmail(inputDto: AuthByEmailInputDto): LoginOutputDto
    suspend fun confirmLoginByEmailWithCode(inputDto: ConfirmByEmailWithCodeInputDto): LoginAuthenticationOutputDto
}