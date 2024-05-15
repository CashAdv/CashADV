package app.cashadvisor.authorization.data.impl

import app.cashadvisor.authorization.data.NetworkToResetPasswordExceptionMapper
import app.cashadvisor.authorization.data.ResetDataMapper
import app.cashadvisor.authorization.data.api.ResetPasswordApiService
import app.cashadvisor.authorization.data.api.ResetPasswordRemoteDataSource
import app.cashadvisor.authorization.data.models.ConfirmResetPasswordWithCodeInputDto
import app.cashadvisor.authorization.data.models.ResetPasswordInputDto
import app.cashadvisor.authorization.data.models.ConfirmResetPasswordWithCodeOutputDto
import app.cashadvisor.authorization.data.models.ResetPasswordOutputDto
import app.cashadvisor.authorization.data.models.SaveNewPasswordInputDto
import app.cashadvisor.authorization.data.models.SaveNewPasswordOutputDto
import app.cashadvisor.common.utill.exceptions.NetworkException
import app.cashadvisor.common.utill.extensions.logDebugMessage
import javax.inject.Inject

class ResetPasswordRemoteDataSourceImpl @Inject constructor(
    private val resetDataMapper: ResetDataMapper,
    private val resetPasswordApiService: ResetPasswordApiService,
    private val networkToResetPasswordExceptionMapper: NetworkToResetPasswordExceptionMapper):ResetPasswordRemoteDataSource {

    override suspend fun confirmEmail(inputDto: ResetPasswordInputDto): ResetPasswordOutputDto {
        return try {
            val response = resetPasswordApiService.resetPassword(
                passwordResetRequest = resetDataMapper.toResetPasswordRequest(inputDto)
            )
            logDebugMessage(resetDataMapper.toResetPasswordRequest(inputDto).toString())
            resetDataMapper.toResetPasswordOutputDto(response)
        }catch (exception: NetworkException){
            throw networkToResetPasswordExceptionMapper.handleConfirmEmailToResetPassword(exception)
        }
    }

    override suspend fun confirmResetPasswordByEmailWithCode(inputDto: ConfirmResetPasswordWithCodeInputDto): ConfirmResetPasswordWithCodeOutputDto {
        return try {
            val response = resetPasswordApiService.resetPasswordConfirm(
                resetPasswordRequest = resetDataMapper.toResetPasswordWithCodeRequest(inputDto)
            )
            resetDataMapper.toConfirmResetPasswordWithCodeOutputDto(response)
        }catch (exception: NetworkException){
            throw networkToResetPasswordExceptionMapper.handleConfirmResetPasswordWithCode(exception)
        }
    }

    override suspend fun saveNewPassword(inputDto: SaveNewPasswordInputDto): SaveNewPasswordOutputDto {
        return try {
            val response = resetPasswordApiService.saveNewPassword(
                saveNewPasswordRequest = resetDataMapper.toSaveNewPasswordRequest(inputDto)
            )
            resetDataMapper.toSaveNewPasswordOutputDto(response)
        }catch (exception: NetworkException){
            throw networkToResetPasswordExceptionMapper.handleSaveNewPassword(exception)
        }
    }
}