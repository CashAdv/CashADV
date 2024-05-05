package app.cashadvisor.authorization.data.impl

import app.cashadvisor.authorization.data.api.ResetPasswordRemoteDataSource
import app.cashadvisor.authorization.di.ResetPasswordExceptionMapper
import app.cashadvisor.authorization.domain.ResetDomainMapper
import app.cashadvisor.authorization.domain.api.ResetPasswordRepository
import app.cashadvisor.authorization.domain.models.ConfirmCode
import app.cashadvisor.authorization.domain.models.ConfirmResetPasswordByEmailWithCodeData
import app.cashadvisor.authorization.domain.models.Email
import app.cashadvisor.authorization.domain.models.Password
import app.cashadvisor.authorization.domain.models.ResetPasswordData
import app.cashadvisor.authorization.domain.models.ResetPasswordState
import app.cashadvisor.authorization.domain.models.SaveNewPasswordData
import app.cashadvisor.common.domain.BaseExceptionToErrorMapper
import app.cashadvisor.common.domain.Resource
import app.cashadvisor.common.domain.model.ErrorEntity
import javax.inject.Inject
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.update

class ResetPasswordRepositoryImpl @Inject constructor(
    private val resetPasswordRemoteDataSource: ResetPasswordRemoteDataSource,
    @ResetPasswordExceptionMapper private val exceptionToErrorMapper: BaseExceptionToErrorMapper,
    private val resetDomainMapper: ResetDomainMapper

):ResetPasswordRepository {
    private val _state: MutableStateFlow<ResetPasswordState> = MutableStateFlow(ResetPasswordState())
    private val state = _state.asStateFlow()
    private val currentState: ResetPasswordState
        get() = state.replayCache.firstOrNull() ?: ResetPasswordState(ResetPasswordState.State.Initial)

    override suspend fun confirmEmailForPasswordReset(email: Email): Resource<ResetPasswordData> {
        return try {
            val data = resetPasswordRemoteDataSource.resetPassword(
                inputDto = resetDomainMapper.toResetPasswordInputDto(email))

            _state.update {
                it.copy(state = ResetPasswordState.State.InProcess(codeToken = data.token))
            }
            Resource.Success(
                data = resetDomainMapper.toResetPasswordData(data)
            )
        }catch (exception: Exception){
            _state.update { it.copy(state = ResetPasswordState.State.Initial) }
            Resource.Error(
                exceptionToErrorMapper.handleException(exception)
            )
        }
    }

    override suspend fun resetPasswordConfirmWithCode(code: ConfirmCode): Resource<ConfirmResetPasswordByEmailWithCodeData> {
        return try {
            val token:String
            when(val state = currentState.state){
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
                resetDomainMapper.toConfirmResetPasswordByEmailWithCodeInputDto(
                    code,
                    token
                )
            )

            Resource.Success(
                data = resetDomainMapper.toConfirmResetPasswordByEmailWithCodeData(data)
            )

        }catch (exception: Exception){

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
            val token:String
            when(val state = currentState.state){
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
            val data = resetPasswordRemoteDataSource.saveNewPassword(resetDomainMapper.toSaveNewPasswordInputDto(
                email,
                password,
                token
            )
            )
            _state.update { it.copy(state = ResetPasswordState.State.Initial) }
            Resource.Success(
                data = resetDomainMapper.toSaveNewPasswordData(data)
            )
        }catch (exception:Exception){
            Resource.Error(
                exceptionToErrorMapper.handleException(exception)
            )
        }
    }

    override fun isLoginInProgress(): Flow<Boolean> {
        return state.map { it.state is ResetPasswordState.State.InProcess }
    }

    companion object {
        const val WRONG_STATE_ERROR = "ResetPassword is not in progress"
    }
}