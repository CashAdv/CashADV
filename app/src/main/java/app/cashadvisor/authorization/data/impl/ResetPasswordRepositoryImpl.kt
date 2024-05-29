package app.cashadvisor.authorization.data.impl

import app.cashadvisor.authorization.data.ResetDomainMapper
import app.cashadvisor.authorization.data.api.ResetPasswordRemoteDataSource
import app.cashadvisor.authorization.di.ResetPasswordExceptionMapper
import app.cashadvisor.authorization.domain.api.ResetPasswordRepository
import app.cashadvisor.authorization.domain.models.ConfirmCode
import app.cashadvisor.authorization.domain.models.ConfirmResetPasswordWithCode
import app.cashadvisor.authorization.domain.models.Email
import app.cashadvisor.authorization.domain.models.Password
import app.cashadvisor.authorization.domain.models.ResetPasswordData
import app.cashadvisor.authorization.domain.models.ResetPasswordState
import app.cashadvisor.authorization.domain.models.SaveNewPasswordData
import app.cashadvisor.common.domain.BaseExceptionToErrorMapper
import app.cashadvisor.common.domain.Resource
import app.cashadvisor.common.domain.model.ErrorEntity
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.update
import javax.inject.Inject

class ResetPasswordRepositoryImpl @Inject constructor(
    private val resetPasswordRemoteDataSource: ResetPasswordRemoteDataSource,
    @ResetPasswordExceptionMapper private val exceptionToErrorMapper: BaseExceptionToErrorMapper,
    private val resetDomainMapper: ResetDomainMapper

) : ResetPasswordRepository {
    private val _state: MutableStateFlow<ResetPasswordState> =
        MutableStateFlow(ResetPasswordState())

    private val currentState: ResetPasswordState
        get() = _state.value

    override suspend fun confirmEmailForPasswordReset(email: Email): Resource<ResetPasswordData> {
        return try {
            val data = resetPasswordRemoteDataSource.confirmEmail(
                inputDto = resetDomainMapper.toResetPasswordInputDto(email)
            )

            _state.update {
                it.copy(state = ResetPasswordState.State.InProcess(codeToken = data.token))
            }
            return when (data.statusCode) {
                SUCCESS -> Resource.Success(
                    data = resetDomainMapper.toResetPasswordData(data)
                )

                BAD_REQUEST -> Resource.Error(
                    ErrorEntity.ConfirmEmailToResetPassword.InvalidInput(INVALID_INPUT)
                )

                INTERNAL_SERVER_ERROR -> Resource.Error(
                    ErrorEntity.ConfirmEmailToResetPassword.FailedToGenerateTokenOrSendEmail(
                        FAILED_TO_GENERATE
                    )
                )

                else -> Resource.Error(ErrorEntity.UnknownError())
            }
        } catch (exception: Exception) {
            _state.update { it.copy(state = ResetPasswordState.State.Initial) }
            Resource.Error(
                exceptionToErrorMapper.handleException(exception)
            )
        }
    }

    override suspend fun resetPasswordConfirmWithCode(code: ConfirmCode):
            Resource<ConfirmResetPasswordWithCode> {
        return try {
            val token: String
            when (val state = currentState.state) {
                is ResetPasswordState.State.InProcess -> {
                    token = state.codeToken
                }

                else -> {
                    return Resource.Error(
                        ErrorEntity.ConfirmResetPasswordByEmailWithCode.InvalidInput(
                            WRONG_STATE_ERROR
                        )
                    )
                }
            }
            val data = resetPasswordRemoteDataSource.confirmResetPasswordByEmailWithCode(
                inputDto = resetDomainMapper.toConfirmResetPasswordByEmailWithCodeInputDto(
                    code,
                    token
                )
            )
            return when (data.statusCode) {
                SUCCESS -> Resource.Success(
                    data = resetDomainMapper.toConfirmResetPasswordWithCode(data)
                )

                BAD_REQUEST -> Resource.Error(
                    ErrorEntity.ConfirmResetPasswordByEmailWithCode.InvalidInput(INVALID_INPUT)
                )

                UNAUTHORIZED -> Resource.Error(
                    ErrorEntity.ConfirmResetPasswordByEmailWithCode.WrongConfirmationCode(
                        WRONG_CONFIRMATION_CODE
                    )
                )

                INTERNAL_SERVER_ERROR -> Resource.Error(
                    ErrorEntity.ConfirmResetPasswordByEmailWithCode.FailedToConfirmPasswordReset(
                        FAILED_TO_CONFIRM
                    )
                )

                else -> Resource.Error(ErrorEntity.UnknownError())
            }


        } catch (exception: Exception) {
            Resource.Error(
                exceptionToErrorMapper.handleException(exception)
            )
        }
    }

    override suspend fun saveNewPassword(
        email: Email,
        password: Password
    ): Resource<SaveNewPasswordData> {
        return try {
            val token: String
            when (val state = currentState.state) {
                is ResetPasswordState.State.InProcess -> {
                    token = state.codeToken
                }

                else -> {
                    return Resource.Error(
                        ErrorEntity.ConfirmResetPasswordByEmailWithCode.InvalidInput(
                            WRONG_STATE_ERROR
                        )
                    )
                }
            }
            val data = resetPasswordRemoteDataSource.saveNewPassword(
                resetDomainMapper.toSaveNewPasswordInputDto(
                    email,
                    password,
                    token
                )
            )
            _state.update { it.copy(state = ResetPasswordState.State.Initial) }
            return when (data.statusCode) {
                SUCCESS -> Resource.Success(
                    data = resetDomainMapper.toSaveNewPasswordData(data)
                )

                BAD_REQUEST -> Resource.Error(
                    ErrorEntity.SaveNewPassword.InvalidInput(INVALID_INPUT)
                )

                UNAUTHORIZED -> Resource.Error(
                    ErrorEntity.SaveNewPassword.InvalidToken(INVALID_TOKEN)
                )

                INTERNAL_SERVER_ERROR -> Resource.Error(
                    ErrorEntity.SaveNewPassword.FailedToResetPassword(FAILED_TO_RESET_PASSWORD)
                )

                else -> Resource.Error(ErrorEntity.UnknownError())
            }

        } catch (exception: Exception) {
            Resource.Error(
                exceptionToErrorMapper.handleException(exception)
            )
        }
    }



    companion object {
        const val WRONG_STATE_ERROR = "ResetPassword is not in progress"
        const val WRONG_CONFIRMATION_CODE = "Wrong confirmation code"
        const val INVALID_INPUT = "Invalid input or content type"
        const val INVALID_TOKEN = "Invalid or expired reset token"
        const val FAILED_TO_CONFIRM = "Failed to confirm password reset"
        const val FAILED_TO_GENERATE = "Failed to generate token or send email"
        const val FAILED_TO_RESET_PASSWORD = "Failed to reset password"
        const val SUCCESS = 200
        const val BAD_REQUEST = 400
        const val UNAUTHORIZED = 401
        const val INTERNAL_SERVER_ERROR = 500

    }
}