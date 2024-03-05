package app.cashadvisor.authorization.data.impl

import app.cashadvisor.authorization.data.api.RegisterRemoteDataSource
import app.cashadvisor.authorization.di.RegisterExceptionMapper
import app.cashadvisor.authorization.domain.AuthenticationDomainMapper
import app.cashadvisor.authorization.domain.api.RegisterRepository
import app.cashadvisor.authorization.domain.models.ConfirmCode
import app.cashadvisor.authorization.domain.models.Email
import app.cashadvisor.authorization.domain.models.Password
import app.cashadvisor.authorization.domain.models.RegisterAuthenticationData
import app.cashadvisor.authorization.domain.models.RegisterData
import app.cashadvisor.authorization.domain.models.states.RegisterState
import app.cashadvisor.common.domain.BaseExceptionToErrorMapper
import app.cashadvisor.common.domain.Resource
import app.cashadvisor.common.domain.model.ErrorEntity
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.update
import javax.inject.Inject

class RegisterRepositoryImpl
@Inject constructor(
    private val registerRemoteDataSource: RegisterRemoteDataSource,
    @RegisterExceptionMapper val exceptionToErrorMapper: BaseExceptionToErrorMapper,
    private val authenticationDomainMapper: AuthenticationDomainMapper
) : RegisterRepository {

    private val _state: MutableStateFlow<RegisterState> = MutableStateFlow(RegisterState())
    private val state = _state.asStateFlow()
    private val currentState: RegisterState
        get() = state.replayCache.firstOrNull() ?: RegisterState(RegisterState.State.Initial)

    override suspend fun registerByEmail(email: Email, password: Password): Resource<RegisterData> {
        return try {
            val data = registerRemoteDataSource.registerByEmail(
                inputDto = authenticationDomainMapper.toAuthByEmailInputDto(email, password)
            )

            _state.update {
                it.copy(state = RegisterState.State.InProcess(codeToken = data.token))
            }
            Resource.Success(
                data = authenticationDomainMapper.toRegisterData(data)
            )

        } catch (exception: Exception) {
            _state.update {
                it.copy(state = RegisterState.State.Initial)
            }
            Resource.Error(
                exceptionToErrorMapper.handleException(exception)
            )
        }
    }

    override fun isRegisterInProgress(): Flow<Boolean> {
        return state.map { it.state is RegisterState.State.InProcess }
    }

    override suspend fun confirmRegisterByEmailWithCode(
        email: Email,
        code: ConfirmCode,
    ): Resource<RegisterAuthenticationData> {
        return try {
            val token: String

            when (val state = currentState.state) {
                is RegisterState.State.InProcess -> {
                    token = state.codeToken
                }

                else -> {
                    return Resource.Error(
                        ErrorEntity.RegisterConfirmationWithCode.InvalidToken(
                            WRONG_STATE_ERROR
                        )
                    )
                }
            }
            val data = registerRemoteDataSource.confirmRegisterByEmailWithCode(
                inputDto = authenticationDomainMapper
                    .toConfirmByEmailWithCodeInputDto(email, code, token)
            )

            _state.update {
                it.copy(state = RegisterState.State.Initial)
            }

            Resource.Success(
                data = authenticationDomainMapper.toRegisterAuthenticationData(data)
            )


        } catch (exception: Exception) {
            Resource.Error(
                exceptionToErrorMapper.handleException(exception)
            )
        }
    }

    companion object {
        const val WRONG_STATE_ERROR = "Register is not in progress"
    }

}