package app.cashadvisor.authorization.data.api

import app.cashadvisor.authorization.data.models.request.ResetPasswordRequest
import app.cashadvisor.authorization.data.models.request.ResetPasswordConfirmByEmailRequest
import app.cashadvisor.authorization.data.models.request.SaveNewPasswordRequest
import app.cashadvisor.authorization.data.models.response.ConfirmResetPasswordResponse
import app.cashadvisor.authorization.data.models.response.ResetPasswordConfirmationResponse
import app.cashadvisor.authorization.data.models.response.SaveNewPasswordResponse
import retrofit2.http.Body
import retrofit2.http.Headers
import retrofit2.http.POST

interface ResetPasswordApi {

    @Headers("Content-Type: application/json")
    @POST("/auth/login/reset/password/confirm")
    suspend fun resetPassword(@Body passworResetRequest: ResetPasswordRequest ):ResetPasswordConfirmationResponse

    @Headers("Content-Type: application/json")
        @POST("/auth/login/reset/password/")
    suspend fun resetPasswordConfirm(@Body resetPasswordConfirmByEmailRequest: ResetPasswordConfirmByEmailRequest):ConfirmResetPasswordResponse

    @Headers("Content-Type: application/json")
    @POST("/auth/login/reset/password/")
    suspend fun saveNewPassword(@Body saveNewPasswordRequest: SaveNewPasswordRequest):SaveNewPasswordResponse

}