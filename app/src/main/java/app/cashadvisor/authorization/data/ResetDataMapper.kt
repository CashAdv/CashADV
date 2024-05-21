package app.cashadvisor.authorization.data

import app.cashadvisor.authorization.data.models.ConfirmResetPasswordWithCodeInputDto
import app.cashadvisor.authorization.data.models.ResetPasswordInputDto
import app.cashadvisor.authorization.data.models.ConfirmResetPasswordWithCodeOutputDto
import app.cashadvisor.authorization.data.models.ResetPasswordOutputDto
import app.cashadvisor.authorization.data.models.SaveNewPasswordInputDto
import app.cashadvisor.authorization.data.models.SaveNewPasswordOutputDto
import app.cashadvisor.authorization.data.models.request.ResetPasswordRequest
import app.cashadvisor.authorization.data.models.request.ResetPasswordWithCodeRequest
import app.cashadvisor.authorization.data.models.request.SaveNewPasswordRequest
import app.cashadvisor.authorization.data.models.response.ConfirmResetPasswordResponse
import app.cashadvisor.authorization.data.models.response.ResetPasswordResponse
import app.cashadvisor.authorization.data.models.response.SaveNewPasswordResponse
import javax.inject.Inject

class ResetDataMapper @Inject constructor() {

    fun toResetPasswordRequest(inputDto: ResetPasswordInputDto): ResetPasswordRequest {
        return ResetPasswordRequest(
            email = inputDto.email

        )
    }

    fun toConfirmResetPasswordWithCodeOutputDto(response: ConfirmResetPasswordResponse):
            ConfirmResetPasswordWithCodeOutputDto {
        return ConfirmResetPasswordWithCodeOutputDto(
            message = response.message,
            statusCode = response.statusCode
        )
    }

    fun toResetPasswordWithCodeRequest(inputDto: ConfirmResetPasswordWithCodeInputDto):
            ResetPasswordWithCodeRequest {
        return ResetPasswordWithCodeRequest(
            code = inputDto.code,
            token = inputDto.token
        )
    }

    fun toResetPasswordOutputDto(response: ResetPasswordResponse): ResetPasswordOutputDto {
        return ResetPasswordOutputDto(
            message = response.message,
            token = response.token,
            statusCode = response.statusCode
        )
    }

    fun toSaveNewPasswordRequest(inputDto: SaveNewPasswordInputDto): SaveNewPasswordRequest {
        return SaveNewPasswordRequest(
            email = inputDto.email,
            password = inputDto.password,
            resetToken = inputDto.resetToken
        )
    }

    fun toSaveNewPasswordOutputDto(response: SaveNewPasswordResponse): SaveNewPasswordOutputDto {
        return SaveNewPasswordOutputDto(
            message = response.message,
            statusCode = response.statusCode
        )
    }
}