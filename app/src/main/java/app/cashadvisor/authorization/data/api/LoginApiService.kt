package app.cashadvisor.authorization.data.api

import app.cashadvisor.authorization.data.models.request.ConfirmLoginByEmailRequest
import app.cashadvisor.authorization.data.models.request.LoginByEmailRequest
import app.cashadvisor.authorization.data.models.response.ConfirmLoginResponse
import app.cashadvisor.authorization.data.models.response.LoginResponse
import retrofit2.http.Body
import retrofit2.http.Headers
import retrofit2.http.POST

interface LoginApiService {
    @Headers("Content-Type: application/json")
    @POST("auth/login")
    suspend fun loginByEmail(@Body loginByEmailRequest: LoginByEmailRequest): LoginResponse

    @Headers("Content-Type: application/json")
    @POST("auth/login/confirm")
    suspend fun confirmLoginByEmailWithCode(
        @Body confirmLoginByEmailRequest: ConfirmLoginByEmailRequest
    ): ConfirmLoginResponse
}