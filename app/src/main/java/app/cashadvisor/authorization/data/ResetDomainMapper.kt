package app.cashadvisor.authorization.data

import app.cashadvisor.authorization.data.models.ConfirmResetPasswordWithCodeInputDto
import app.cashadvisor.authorization.data.models.ResetPasswordInputDto
import app.cashadvisor.authorization.data.models.ConfirmResetPasswordWithCodeOutputDto
import app.cashadvisor.authorization.data.models.ResetPasswordOutputDto
import app.cashadvisor.authorization.data.models.SaveNewPasswordInputDto
import app.cashadvisor.authorization.data.models.SaveNewPasswordOutputDto
import app.cashadvisor.authorization.domain.models.ConfirmCode
import app.cashadvisor.authorization.domain.models.ConfirmResetPasswordWithCode
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

    fun toConfirmResetPasswordByEmailWithCodeInputDto(
        code: ConfirmCode,
        token: String
    ): ConfirmResetPasswordWithCodeInputDto {
        return ConfirmResetPasswordWithCodeInputDto(
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

    fun toConfirmResetPasswordWithCode(data: ConfirmResetPasswordWithCodeOutputDto): ConfirmResetPasswordWithCode {
        return ConfirmResetPasswordWithCode(
            message = data.message,
        )
    }

    fun toResetPasswordData(data: ResetPasswordOutputDto): ResetPasswordData {
        return ResetPasswordData(
            message = data.message,
            statusCode = data.statusCode
        )
    }

    fun toSaveNewPasswordData(data: SaveNewPasswordOutputDto): SaveNewPasswordData {
        return SaveNewPasswordData(
            message = data.message,
            statusCode = data.statusCode
        )
    }
}