package app.cashadvisor.authorization.data.api

import app.cashadvisor.authorization.data.models.ConfirmResetPasswordWithCodeInputDto
import app.cashadvisor.authorization.data.models.ConfirmResetPasswordWithCodeOutputDto
import app.cashadvisor.authorization.data.models.ResetPasswordInputDto
import app.cashadvisor.authorization.data.models.ResetPasswordOutputDto
import app.cashadvisor.authorization.data.models.SaveNewPasswordInputDto
import app.cashadvisor.authorization.data.models.SaveNewPasswordOutputDto

interface ResetPasswordRemoteDataSource {
    suspend fun confirmEmail(inputDto: ResetPasswordInputDto):ResetPasswordOutputDto
    suspend fun confirmResetPasswordByEmailWithCode(inputDto: ConfirmResetPasswordWithCodeInputDto):ConfirmResetPasswordWithCodeOutputDto
    suspend fun saveNewPassword(inputDto:SaveNewPasswordInputDto):SaveNewPasswordOutputDto
}