package app.cashadvisor.authorization.domain

import app.cashadvisor.authorization.data.models.ConfirmByEmailWithCodeInputDto
import app.cashadvisor.authorization.data.models.LoginAuthenticationOutputDto
import app.cashadvisor.authorization.data.models.LoginOutputDto
import app.cashadvisor.authorization.data.models.RegisterAuthenticationOutputDto
import app.cashadvisor.authorization.data.models.AuthByEmailInputDto
import app.cashadvisor.authorization.data.models.RegisterOutputDto
import app.cashadvisor.authorization.data.models.TokenDetailsOutputDto
import app.cashadvisor.authorization.domain.models.ConfirmCode
import app.cashadvisor.authorization.domain.models.Email
import app.cashadvisor.authorization.domain.models.LoginAuthenticationData
import app.cashadvisor.authorization.domain.models.LoginData
import app.cashadvisor.authorization.domain.models.Password
import app.cashadvisor.authorization.domain.models.RegisterAuthenticationData
import app.cashadvisor.authorization.domain.models.RegisterData
import app.cashadvisor.authorization.domain.models.TokenDetails
import javax.inject.Inject

class AuthenticationDomainMapper @Inject constructor() {

    fun toAuthByEmailInputDto(email: Email, password: Password): AuthByEmailInputDto {
        return AuthByEmailInputDto(
            email = email.value,
            password = password.value
        )
    }

    fun toConfirmByEmailWithCodeInputDto(
        email: Email,
        code: ConfirmCode,
        token: String
    ): ConfirmByEmailWithCodeInputDto {
        return ConfirmByEmailWithCodeInputDto(
            email = email.value,
            code = code.value,
            token = token
        )
    }

    fun toRegisterData(data: RegisterOutputDto): RegisterData {
        return RegisterData(
            message = data.message,
        )
    }

    fun toRegisterAuthenticationData(data: RegisterAuthenticationOutputDto): RegisterAuthenticationData {
        return RegisterAuthenticationData(
            message = data.message,
            accessTokenLifeTime = data.accessTokenLifeTime,
            refreshTokenLifeTime = data.refreshTokenLifeTime,
            tokenDetails = mapTokenDetails(data.tokenDetailsDto)
        )
    }

    fun toLoginData(data: LoginOutputDto): LoginData {
        return LoginData(
            message = data.message
        )
    }

    fun toLoginAuthenticationData(data: LoginAuthenticationOutputDto): LoginAuthenticationData {
        return LoginAuthenticationData(
            message = data.message,
            accessTokenLifeTime = data.accessTokenLifeTime,
            refreshTokenLifeTime = data.refreshTokenLifeTime,
            tokenDetails = mapTokenDetails(data.tokenDetailsDto)
        )
    }

    private fun mapTokenDetails(tokenDetailsOutputDto: TokenDetailsOutputDto): TokenDetails {
        return TokenDetails(
            accessToken = tokenDetailsOutputDto.accessToken,
            refreshToken = tokenDetailsOutputDto.refreshToken,
            expiredAt = tokenDetailsOutputDto.expiredAt
        )
    }

}