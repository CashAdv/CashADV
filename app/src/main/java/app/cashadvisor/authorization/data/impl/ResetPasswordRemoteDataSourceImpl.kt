package app.cashadvisor.authorization.data.impl

import app.cashadvisor.authorization.data.NetworkToResetPasswordExceptionMapper
import app.cashadvisor.authorization.data.ResetDataMapper
import app.cashadvisor.authorization.data.api.ResetPasswordApiService
import app.cashadvisor.authorization.data.api.ResetPasswordRemoteDataSource
import app.cashadvisor.authorization.data.models.ConfirmResetPasswordByEmailWithCodeInputDto
import app.cashadvisor.authorization.data.models.ResetPasswordInputDto
import app.cashadvisor.authorization.data.models.ConfirmResetPasswordByEmailWithCodeOutputDto
import app.cashadvisor.authorization.data.models.ResetPasswordOutputDto
import app.cashadvisor.authorization.data.models.SaveNewPasswordInputDto
import app.cashadvisor.authorization.data.models.SaveNewPasswordOutputDto
import app.cashadvisor.common.utill.exceptions.NetworkException
import javax.inject.Inject

class ResetPasswordRemoteDataSourceImpl @Inject constructor(
    private val resetDataMapper: ResetDataMapper,
    private val resetPasswordApiService: ResetPasswordApiService,
    private val networkToResetPasswordExceptionMapper: NetworkToResetPasswordExceptionMapper):ResetPasswordRemoteDataSource {

    override suspend fun resetPassword(inputDto: ResetPasswordInputDto): ResetPasswordOutputDto {
        return try {
            val response = resetPasswordApiService.resetPassword(
                passworResetRequest = resetDataMapper.toResetPasswordRequest(inputDto)
            )
            resetDataMapper.toResetPasswordOutputDto(response)
        }catch (exception: NetworkException){
            throw networkToResetPasswordExceptionMapper.handleConfirmEmailToResetPassword(exception)
        }
    }

    override suspend fun confirmResetPasswordByEmailWithCode(inputDto: ConfirmResetPasswordByEmailWithCodeInputDto): ConfirmResetPasswordByEmailWithCodeOutputDto {
        return try {
            val response = resetPasswordApiService.resetPasswordConfirm(
                resetPasswordRequest = resetDataMapper.toResetPasswordByEmailWithCodeRequest(inputDto)
            )
            resetDataMapper.toConfirmResetPasswordByEmailWithCodeOutputDto(response)
        }catch (exception: NetworkException){
            throw networkToResetPasswordExceptionMapper.handleConfirmResetPasswordByEmailWithCode(exception)
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