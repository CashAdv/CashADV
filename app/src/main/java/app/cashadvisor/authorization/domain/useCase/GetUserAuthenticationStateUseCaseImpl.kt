package app.cashadvisor.authorization.domain.useCase

import ErrorsAccessToken
import app.cashadvisor.Resource
import app.cashadvisor.authorization.domain.repository.AuthRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class GetUserAuthenticationStateUseCaseImpl @Inject constructor(
    private val authRepository: AuthRepository
) : GetUserAuthenticationStateUseCase {

    override fun invoke(): Flow<Resource<Boolean, ErrorsAccessToken>> =
        authRepository.getToken().map {
            when (it) {
                is Resource.Error -> Resource.Error(
                    error = it.error ?: ErrorsAccessToken.ErrorAccessTokenThree
                )

                is Resource.Success -> Resource.Success(
                    data = it.data?.isNotEmpty() ?: false
                )
            }
        }
}
