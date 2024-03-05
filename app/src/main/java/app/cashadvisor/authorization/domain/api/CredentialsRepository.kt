package app.cashadvisor.authorization.domain.api

import android.content.SharedPreferences
import app.cashadvisor.authorization.data.dto.CredentialsDto
import kotlinx.coroutines.flow.Flow

interface CredentialsRepository : SharedPreferences.OnSharedPreferenceChangeListener {

    suspend fun saveCredentials(credentials: CredentialsDto)
    suspend fun getCredentials(): CredentialsDto?
    fun getCredentialsFlow(): Flow<CredentialsDto?>
    suspend fun clearCredentials()
    suspend fun hasCredentials(): Boolean

}