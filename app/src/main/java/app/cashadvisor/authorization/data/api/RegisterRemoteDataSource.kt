package app.cashadvisor.authorization.data.api

import app.cashadvisor.authorization.data.models.ConfirmByEmailWithCodeInputDto
import app.cashadvisor.authorization.data.models.RegisterAuthenticationOutputDto
import app.cashadvisor.authorization.data.models.AuthByEmailInputDto
import app.cashadvisor.authorization.data.models.RegisterOutputDto

interface RegisterRemoteDataSource {
    suspend fun registerByEmail(inputDto: AuthByEmailInputDto): RegisterOutputDto
    suspend fun confirmRegisterByEmailWithCode(inputDto: ConfirmByEmailWithCodeInputDto): RegisterAuthenticationOutputDto
}