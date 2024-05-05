package app.cashadvisor.authorization.data.api

import app.cashadvisor.authorization.data.models.request.ResetPasswordByEmailWithCodeRequest
import app.cashadvisor.authorization.data.models.request.ResetPasswordRequest
import app.cashadvisor.authorization.data.models.request.SaveNewPasswordRequest
import app.cashadvisor.authorization.data.models.response.ConfirmResetPasswordResponse
import app.cashadvisor.authorization.data.models.response.ResetPasswordConfirmationResponse
import app.cashadvisor.authorization.data.models.response.SaveNewPasswordResponse
import retrofit2.http.Body
import retrofit2.http.Headers
import retrofit2.http.POST

interface ResetPasswordApiService {

    @Headers("Content-Type: application/json")
    @POST("auth/login/reset/password")
    suspend fun resetPassword(@Body passwordResetRequest: ResetPasswordRequest ):ResetPasswordConfirmationResponse

    @Headers("Content-Type: application/json")
    @POST("auth/login/reset/password/confirm")
    suspend fun resetPasswordConfirm(@Body resetPasswordRequest: ResetPasswordByEmailWithCodeRequest):ConfirmResetPasswordResponse

    @Headers("Content-Type: application/json")
    @POST("auth/login/reset/password")
    suspend fun saveNewPassword(@Body saveNewPasswordRequest: SaveNewPasswordRequest):SaveNewPasswordResponse

}