package app.cashadvisor.authorization.data.impl

import kotlinx.serialization.json.Json
import android.content.SharedPreferences
import androidx.core.content.edit
import app.cashadvisor.authorization.data.dto.CredentialsDto
import app.cashadvisor.authorization.domain.api.CredentialsRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import kotlinx.serialization.encodeToString

class CredentialsRepositoryImpl(
    private val storage: SharedPreferences,
    private val key: String,
    private val json: Json
) : CredentialsRepository, CoroutineScope by CoroutineScope(
    Dispatchers.IO +
            SupervisorJob()
) {

    private val _credentialsUpdateFlow = MutableStateFlow<CredentialsDto?>(null)

    init {
        storage.registerOnSharedPreferenceChangeListener(this)
        launch {
            _credentialsUpdateFlow.value = getCredentials()
        }
    }

    override suspend fun saveCredentials(credentials: CredentialsDto) {
        withContext(Dispatchers.IO) {
            val data = json.encodeToString(credentials)
            storage.edit { putString(key, data) }
        }
    }

    override suspend fun getCredentials(): CredentialsDto? {
        return withContext(Dispatchers.IO) {
            storage.getString(key, null)?.let {
                json.decodeFromString<CredentialsDto>(it)
            }
        }
    }

    override fun getCredentialsFlow(): Flow<CredentialsDto?> = _credentialsUpdateFlow

    override suspend fun hasCredentials(): Boolean {
        return withContext(Dispatchers.IO) {
            storage.contains(key)
        }
    }

    override suspend fun clearCredentials() {
        withContext(Dispatchers.IO) {
            storage.edit { remove(key) }
            _credentialsUpdateFlow.emit(null)
        }
    }

    override fun onSharedPreferenceChanged(
        p0: SharedPreferences?,
        p1: String?
    ) {
        if (p1 == key) {
            launch {
                _credentialsUpdateFlow.value = getCredentials()
            }
        }
    }
}