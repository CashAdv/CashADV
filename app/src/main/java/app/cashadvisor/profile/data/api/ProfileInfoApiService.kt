package app.cashadvisor.profile.data.api

import app.cashadvisor.profile.data.dto.request.UpdateProfilePicRequest
import app.cashadvisor.profile.data.dto.request.UpdateUserNameRequest
import app.cashadvisor.profile.data.dto.response.ConfirmUpdateNameResponse
import app.cashadvisor.profile.data.dto.response.ConfirmUpdatePicResponse
import app.cashadvisor.profile.data.dto.response.ProfileInfoResponse
import okhttp3.MultipartBody
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Headers
import retrofit2.http.Multipart
import retrofit2.http.PUT
import retrofit2.http.Part

interface ProfileInfoApiService {

    @Headers("Content-Type: application/json")
    @GET("profile/info/get")
    suspend fun getUserInfo(
        @Header("Authorization") accessToken: String
    ): ProfileInfoResponse

    @Headers("Content-Type: application/json")
    @PUT("profile/name/put")
    suspend fun updateUserName(
        @Header("Authorization") accessToken: String,
        @Body updateUserNameRequest: UpdateUserNameRequest
    ): ConfirmUpdateNameResponse

    @Headers("Content-Type: application/json")
    @PUT("profile/image/put")
    suspend fun updateProfilePic(
        @Header("Authorization") accessToken: String,
        @Body updateProfilePicRequest: UpdateProfilePicRequest
    ): ConfirmUpdatePicResponse
}
