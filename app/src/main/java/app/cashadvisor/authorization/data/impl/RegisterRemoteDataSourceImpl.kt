package app.cashadvisor.authorization.data.impl

import app.cashadvisor.authorization.data.AuthenticationDataMapper
import app.cashadvisor.authorization.data.NetworkToRegisterExceptionMapper
import app.cashadvisor.authorization.data.api.RegisterApiService
import app.cashadvisor.authorization.data.api.RegisterRemoteDataSource
import app.cashadvisor.authorization.data.models.ConfirmByEmailWithCodeInputDto
import app.cashadvisor.authorization.data.models.RegisterAuthenticationOutputDto
import app.cashadvisor.authorization.data.models.AuthByEmailInputDto
import app.cashadvisor.authorization.data.models.RegisterOutputDto
import app.cashadvisor.common.utill.exceptions.NetworkException
import javax.inject.Inject

class RegisterRemoteDataSourceImpl
@Inject constructor(
    private val registerApiService: RegisterApiService,
    private val networkToRegisterExceptionMapper: NetworkToRegisterExceptionMapper,
    private val authenticationDataMapper: AuthenticationDataMapper
) : RegisterRemoteDataSource {

    override suspend fun registerByEmail(inputDto: AuthByEmailInputDto): RegisterOutputDto {
        return try {
            val response = registerApiService.registerByEmail(
                registerByEmailRequest = authenticationDataMapper.toRegisterByEmailRequest(inputDto)
            )
            authenticationDataMapper.toRegisterOutputDto(response)
        } catch (exception: NetworkException) {
            throw networkToRegisterExceptionMapper.handleExceptionForRegisterByEmail(exception)
        }
    }

    override suspend fun confirmRegisterByEmailWithCode(
        inputDto: ConfirmByEmailWithCodeInputDto
    ): RegisterAuthenticationOutputDto {
        return try {
            val response = registerApiService.confirmRegisterByEmailWithCode(
                confirmRegisterByEmailRequest = authenticationDataMapper.toConfirmRegisterByEmailRequest(
                    inputDto
                )
            )
            authenticationDataMapper.toRegisterAuthorizationOutputDto(response)
        } catch (exception: NetworkException) {
            throw networkToRegisterExceptionMapper
                .handleExceptionForRegisterConfirmationByEmailWithCode(exception)
        }
    }

}