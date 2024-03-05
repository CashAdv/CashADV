package app.cashadvisor.authorization.data.impl

import app.cashadvisor.authorization.data.AuthenticationDataMapper
import app.cashadvisor.authorization.data.NetworkToLoginExceptionMapper
import app.cashadvisor.authorization.data.api.LoginApiService
import app.cashadvisor.authorization.data.api.LoginRemoteDataSource
import app.cashadvisor.authorization.data.models.AuthByEmailInputDto
import app.cashadvisor.authorization.data.models.ConfirmByEmailWithCodeInputDto
import app.cashadvisor.authorization.data.models.LoginAuthenticationOutputDto
import app.cashadvisor.authorization.data.models.LoginOutputDto
import app.cashadvisor.common.utill.exceptions.NetworkException
import javax.inject.Inject

class LoginRemoteDataSourceImpl
@Inject constructor(
    private val loginApiService: LoginApiService,
    private val networkToLoginExceptionMapper: NetworkToLoginExceptionMapper,
    private val authenticationDataMapper: AuthenticationDataMapper
) : LoginRemoteDataSource {

    override suspend fun loginByEmail(inputDto: AuthByEmailInputDto): LoginOutputDto {
        return try {
            val response = loginApiService.loginByEmail(
                loginByEmailRequest = authenticationDataMapper.toLoginByEmailRequest(inputDto)
            )
            authenticationDataMapper.toLoginOutputDto(response)
        } catch (exception: NetworkException) {
            throw networkToLoginExceptionMapper.handleExceptionForLoginByEmail(exception)
        }
    }

    override suspend fun confirmLoginByEmailWithCode(
        inputDto: ConfirmByEmailWithCodeInputDto
    ): LoginAuthenticationOutputDto {
        return try {
            val response = loginApiService.confirmLoginByEmailWithCode(
                confirmLoginByEmailRequest = authenticationDataMapper.toConfirmLoginByEmailWithCodeRequest(
                    inputDto
                )
            )
            authenticationDataMapper.toLoginAuthorizationOutputDto(response)
        } catch (exception: NetworkException) {
            throw networkToLoginExceptionMapper
                .handleExceptionForLoginConfirmationByEmailWithCode(exception)
        }
    }

}