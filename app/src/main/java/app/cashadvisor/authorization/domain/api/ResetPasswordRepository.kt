package app.cashadvisor.authorization.domain.api

import app.cashadvisor.authorization.domain.models.ConfirmCode
import app.cashadvisor.authorization.domain.models.ConfirmResetPasswordWithCode
import app.cashadvisor.authorization.domain.models.Email
import app.cashadvisor.authorization.domain.models.Password
import app.cashadvisor.authorization.domain.models.ResetPasswordData
import app.cashadvisor.authorization.domain.models.SaveNewPasswordData
import app.cashadvisor.common.domain.Resource

interface ResetPasswordRepository {
    suspend fun confirmEmailForPasswordReset(
        email:Email
    ):Resource<ResetPasswordData>

    suspend fun resetPasswordConfirmWithCode(
        code: ConfirmCode,
    ):Resource<ConfirmResetPasswordWithCode>

    suspend fun saveNewPassword(
        email: Email,
        password:Password,
    ):Resource<SaveNewPasswordData>

}