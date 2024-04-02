package app.cashadvisor.authorization.data

import app.cashadvisor.authorization.data.models.ConfirmResetPasswordByEmailWithCodeInputDto
import app.cashadvisor.authorization.data.models.ResetPasswordInputDto
import app.cashadvisor.authorization.data.models.ConfirmResetPasswordByEmailWithCodeOutputDto
import app.cashadvisor.authorization.data.models.ResetPasswordOutputDto
import app.cashadvisor.authorization.data.models.SaveNewPasswordInputDto
import app.cashadvisor.authorization.data.models.SaveNewPasswordOutputDto
import app.cashadvisor.authorization.data.models.request.ResetPasswordRequest
import app.cashadvisor.authorization.data.models.request.ResetPasswordByEmailWithCodeRequest
import app.cashadvisor.authorization.data.models.request.SaveNewPasswordRequest
import app.cashadvisor.authorization.data.models.response.ConfirmResetPasswordResponse
import app.cashadvisor.authorization.data.models.response.ResetPasswordConfirmationResponse
import app.cashadvisor.authorization.data.models.response.SaveNewPasswordResponse
import javax.inject.Inject

class ResetDataMapper @Inject constructor() {

    fun toResetPasswordRequest(inputDto: ResetPasswordInputDto): ResetPasswordRequest {
        return ResetPasswordRequest(
            email = inputDto.email

        )
    }
    fun toConfirmResetPasswordByEmailWithCodeOutputDto(response:ConfirmResetPasswordResponse): ConfirmResetPasswordByEmailWithCodeOutputDto{
        return ConfirmResetPasswordByEmailWithCodeOutputDto(
            message = response.message,
            statusCode = response.statusCode
        )
    }
    fun toResetPasswordByEmailWithCodeRequest(inputDto: ConfirmResetPasswordByEmailWithCodeInputDto):ResetPasswordByEmailWithCodeRequest{
        return ResetPasswordByEmailWithCodeRequest(
            code = inputDto.code,
            token = inputDto.token
        )
    }
    fun toResetPasswordOutputDto(response:ResetPasswordConfirmationResponse):ResetPasswordOutputDto{
        return ResetPasswordOutputDto(
            message = response.message,
            token = response.token,
            statusCode = response.statusCode
        )
    }
    fun toSaveNewPasswordRequest(inputDto:SaveNewPasswordInputDto):SaveNewPasswordRequest{
        return SaveNewPasswordRequest(
            email = inputDto.email,
            password = inputDto.password,
            resetToken = inputDto.resetToken
        )
    }
    fun toSaveNewPasswordOutputDto(response: SaveNewPasswordResponse):SaveNewPasswordOutputDto{
        return SaveNewPasswordOutputDto(
            message = response.message,
            statusCode = response.statusCode
        )
    }
}