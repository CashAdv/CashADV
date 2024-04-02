package app.cashadvisor.authorization.domain.api

import app.cashadvisor.authorization.domain.models.ConfirmCode
import app.cashadvisor.authorization.domain.models.ConfirmResetPasswordByEmailWithCodeData
import app.cashadvisor.authorization.domain.models.Email
import app.cashadvisor.authorization.domain.models.Password
import app.cashadvisor.authorization.domain.models.ResetPasswordData
import app.cashadvisor.authorization.domain.models.SaveNewPasswordData
import app.cashadvisor.common.domain.Resource
import kotlinx.coroutines.flow.Flow

interface ResetPasswordRepository {
    suspend fun confirmEmailForPasswordReset(
        email:Email
    ):Resource<ResetPasswordData>

    suspend fun resetPasswordConfirmWithCode(
        code: ConfirmCode,
    ):Resource<ConfirmResetPasswordByEmailWithCodeData>

    suspend fun saveNewPassword(
        email: Email,
        password:Password,
    ):Resource<SaveNewPasswordData>

    fun isLoginInProgress(): Flow<Boolean>
}