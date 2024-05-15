package app.cashadvisor.authorization.domain.impl

import app.cashadvisor.authorization.domain.api.ResetPasswordInteractor
import app.cashadvisor.authorization.domain.api.ResetPasswordRepository
import app.cashadvisor.authorization.domain.models.ConfirmCode
import app.cashadvisor.authorization.domain.models.Email
import app.cashadvisor.authorization.domain.models.Password
import app.cashadvisor.authorization.domain.models.ResetPasswordData
import app.cashadvisor.authorization.domain.models.SaveNewPasswordData
import app.cashadvisor.common.domain.Resource
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class ResetPasswordInteractorImpl @Inject constructor(
    private val resetPasswordRepository: ResetPasswordRepository,
):ResetPasswordInteractor {
    override suspend fun confirmEmailForPasswordReset(email: Email): Resource<ResetPasswordData> {
        val result = resetPasswordRepository.confirmEmailForPasswordReset(email)
        return when(result){
            is Resource.Success -> {
                Resource.Success(data = result.data)
            }
            is Resource.Error -> {
                Resource.Error(error = result.error)
            }
        }
    }

    override suspend fun resetPasswordConfirmWithCode(code: ConfirmCode): Resource<String> {
        val result = resetPasswordRepository.resetPasswordConfirmWithCode(code)
        return when(result){
            is Resource.Success -> {
                if(result.data.message!=null){
                    Resource.Success(data = result.data.message)
                }else{
                    Resource.Success(data = "null")
                }
            }
            is Resource.Error -> {
                Resource.Error(error = result.error)
            }
        }

    }

    override suspend fun saveNewPassword(
        email: Email,
        password: Password
    ): Resource<SaveNewPasswordData> {
       val result = resetPasswordRepository.saveNewPassword(email, password)
        return when(result){
            is Resource.Success -> {
                Resource.Success(data = result.data)
            }
            is Resource.Error -> {
                Resource.Error(error = result.error)
            }
        }
    }

    override fun isResetPasswordInProgress(): Flow<Boolean> {
        return resetPasswordRepository.isLoginInProgress()
    }
}