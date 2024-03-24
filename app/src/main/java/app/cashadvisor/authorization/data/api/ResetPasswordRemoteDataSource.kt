package app.cashadvisor.authorization.data.api

import app.cashadvisor.authorization.data.models.ConfirmResetPasswordByEmailWithCodeInputDto
import app.cashadvisor.authorization.data.models.ConfirmResetPasswordByEmailWithCodeOutputDto
import app.cashadvisor.authorization.data.models.ResetPasswordInputDto
import app.cashadvisor.authorization.data.models.ResetPasswordOutputDto
import app.cashadvisor.authorization.data.models.SaveNewPasswordInputDto
import app.cashadvisor.authorization.data.models.SaveNewPasswordOutputDto

interface ResetPasswordRemoteDataSource {
    suspend fun resetPassword(inputDto: ResetPasswordInputDto):ResetPasswordOutputDto
    suspend fun confirmResetPasswordByEmailWithCode(inputDto:ConfirmResetPasswordByEmailWithCodeInputDto):ConfirmResetPasswordByEmailWithCodeOutputDto
    suspend fun saveNewPassword(inputDto:SaveNewPasswordInputDto):SaveNewPasswordOutputDto
}