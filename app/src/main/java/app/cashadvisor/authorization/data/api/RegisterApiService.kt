package app.cashadvisor.authorization.data.api

import app.cashadvisor.authorization.data.models.request.ConfirmRegisterByEmailRequest
import app.cashadvisor.authorization.data.models.request.RegisterByEmailRequest
import app.cashadvisor.authorization.data.models.response.ConfirmRegisterResponse
import app.cashadvisor.authorization.data.models.response.RegisterResponse
import retrofit2.http.Body
import retrofit2.http.Headers
import retrofit2.http.POST

interface RegisterApiService {
    @Headers("Content-Type: application/json")
    @POST("auth/register")
    suspend fun registerByEmail(@Body registerByEmailRequest: RegisterByEmailRequest): RegisterResponse

    @Headers("Content-Type: application/json")
    @POST("auth/register/confirm-email")
    suspend fun confirmRegisterByEmailWithCode(
        @Body confirmRegisterByEmailRequest: ConfirmRegisterByEmailRequest
    ): ConfirmRegisterResponse

}