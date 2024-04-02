package app.cashadvisor.authorization.domain

import app.cashadvisor.authorization.data.models.ConfirmResetPasswordByEmailWithCodeInputDto
import app.cashadvisor.authorization.data.models.ResetPasswordInputDto
import app.cashadvisor.authorization.data.models.ConfirmResetPasswordByEmailWithCodeOutputDto
import app.cashadvisor.authorization.data.models.ResetPasswordOutputDto
import app.cashadvisor.authorization.data.models.SaveNewPasswordInputDto
import app.cashadvisor.authorization.data.models.SaveNewPasswordOutputDto
import app.cashadvisor.authorization.domain.models.ConfirmCode
import app.cashadvisor.authorization.domain.models.ConfirmResetPasswordByEmailWithCodeData
import app.cashadvisor.authorization.domain.models.Email
import app.cashadvisor.authorization.domain.models.Password
import app.cashadvisor.authorization.domain.models.ResetPasswordData
import app.cashadvisor.authorization.domain.models.SaveNewPasswordData
import javax.inject.Inject

class ResetDomainMapper @Inject constructor() {

    fun toResetPasswordInputDto(email: Email): ResetPasswordInputDto {
        return ResetPasswordInputDto(
            email = email.value
        )
    }

    fun toConfirmResetPasswordByEmailWithCodeInputDto(code: ConfirmCode, token: String): ConfirmResetPasswordByEmailWithCodeInputDto {
        return ConfirmResetPasswordByEmailWithCodeInputDto(
            code = code.value,
            token = token
        )
    }

    fun toSaveNewPasswordInputDto(
        email: Email,
        password: Password,
        resetToken: String
    ): SaveNewPasswordInputDto {
        return SaveNewPasswordInputDto(
            email = email.value,
            password = password.value,
            resetToken = resetToken
        )
    }
    fun toConfirmResetPasswordByEmailWithCodeData(data:ConfirmResetPasswordByEmailWithCodeOutputDto):ConfirmResetPasswordByEmailWithCodeData{
        return ConfirmResetPasswordByEmailWithCodeData(
            message = data.message,
            statusCode = data.statusCode
        )
    }
    fun toResetPasswordData(data:ResetPasswordOutputDto):ResetPasswordData{
        return ResetPasswordData(
            message = data.message,
            statusCode = data.statusCode
        )
    }
    fun toSaveNewPasswordData(data:SaveNewPasswordOutputDto):SaveNewPasswordData{
        return SaveNewPasswordData(
            message = data.message,
            statusCode = data.statusCode
        )
    }
}