package app.cashadvisor.authorization.data.dto

import android.os.Parcelable
import kotlinx.parcelize.Parcelize
import kotlinx.serialization.Serializable

@Serializable
@Parcelize
data class CredentialsDto(
    val accessToken: String,
    val refreshToken: String,
) : Parcelable
