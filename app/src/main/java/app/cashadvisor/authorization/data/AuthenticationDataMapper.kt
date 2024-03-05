package app.cashadvisor.authorization.data

import app.cashadvisor.authorization.data.models.AuthByEmailInputDto
import app.cashadvisor.authorization.data.models.ConfirmByEmailWithCodeInputDto
import app.cashadvisor.authorization.data.models.LoginAuthenticationOutputDto
import app.cashadvisor.authorization.data.models.LoginOutputDto
import app.cashadvisor.authorization.data.models.RegisterAuthenticationOutputDto
import app.cashadvisor.authorization.data.models.RegisterOutputDto
import app.cashadvisor.authorization.data.models.TokenDetailsOutputDto
import app.cashadvisor.authorization.data.models.request.ConfirmLoginByEmailRequest
import app.cashadvisor.authorization.data.models.request.ConfirmRegisterByEmailRequest
import app.cashadvisor.authorization.data.models.request.LoginByEmailRequest
import app.cashadvisor.authorization.data.models.request.RegisterByEmailRequest
import app.cashadvisor.authorization.data.models.response.ConfirmLoginResponse
import app.cashadvisor.authorization.data.models.response.ConfirmRegisterResponse
import app.cashadvisor.authorization.data.models.response.LoginResponse
import app.cashadvisor.authorization.data.models.response.RegisterResponse
import app.cashadvisor.authorization.data.models.response.TokenDetailsDto
import javax.inject.Inject

class AuthenticationDataMapper @Inject constructor() {

    fun toRegisterByEmailRequest(inputDto: AuthByEmailInputDto): RegisterByEmailRequest {
        return RegisterByEmailRequest(
            email = inputDto.email,
            password = inputDto.password
        )
    }

    fun toRegisterOutputDto(response: RegisterResponse): RegisterOutputDto {
        return RegisterOutputDto(
            token = response.token,
            statusCode = response.statusCode,
            message = response.message
        )
    }

    fun toConfirmRegisterByEmailRequest(inputDto: ConfirmByEmailWithCodeInputDto): ConfirmRegisterByEmailRequest {
        return ConfirmRegisterByEmailRequest(
            email = inputDto.email,
            code = inputDto.code,
            token = inputDto.token
        )
    }

    fun toRegisterAuthorizationOutputDto(response: ConfirmRegisterResponse): RegisterAuthenticationOutputDto {
        return RegisterAuthenticationOutputDto(
            message = response.message,
            statusCode = response.statusCode,
            accessTokenLifeTime = response.accessTokenLifeTime,
            refreshTokenLifeTime = response.refreshTokenLifeTime,
            deviceId = response.deviceId,
            tokenDetailsDto = toTokenDetailsOutputDto(response.tokenDetailsDto)
        )
    }

    fun toLoginByEmailRequest(inputDto: AuthByEmailInputDto): LoginByEmailRequest {
        return LoginByEmailRequest(
            email = inputDto.email,
            password = inputDto.password
        )
    }

    fun toLoginOutputDto(response: LoginResponse): LoginOutputDto {
        return LoginOutputDto(
            token = response.token,
            statusCode = response.statusCode,
            message = response.message
        )
    }

    fun toConfirmLoginByEmailWithCodeRequest(inputDto: ConfirmByEmailWithCodeInputDto): ConfirmLoginByEmailRequest {
        return ConfirmLoginByEmailRequest(
            email = inputDto.email,
            code = inputDto.code,
            token = inputDto.token
        )
    }

    fun toLoginAuthorizationOutputDto(response: ConfirmLoginResponse): LoginAuthenticationOutputDto {
        return LoginAuthenticationOutputDto(
            message = response.message,
            statusCode = response.statusCode,
            accessTokenLifeTime = response.accessTokenLifeTime,
            refreshTokenLifeTime = response.refreshTokenLifeTime,
            deviceId = response.deviceId,
            tokenDetailsDto = toTokenDetailsOutputDto(response.tokenDetailsDto)
        )
    }

    private fun toTokenDetailsOutputDto(tokenDetailsDto: TokenDetailsDto): TokenDetailsOutputDto {
        return TokenDetailsOutputDto(
            accessToken = tokenDetailsDto.accessToken,
            refreshToken = tokenDetailsDto.refreshToken,
            expiredAt = tokenDetailsDto.expiredAt
        )
    }

}