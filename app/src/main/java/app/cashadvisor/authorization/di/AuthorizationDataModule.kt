package app.cashadvisor.authorization.di

import android.content.Context
import android.content.SharedPreferences
import androidx.security.crypto.EncryptedSharedPreferences
import androidx.security.crypto.MasterKeys
import app.cashadvisor.authorization.data.impl.CredentialsRepositoryImpl
import app.cashadvisor.authorization.domain.api.CredentialsRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import kotlinx.serialization.json.Json
import javax.inject.Singleton


@Module
@InstallIn(SingletonComponent::class)
class AuthorizationDataModule {


    @Provides
    @Singleton
    fun providesMasterKey(): String =
        MasterKeys.getOrCreate(MasterKeys.AES256_GCM_SPEC)

    @Provides
    @Singleton
    fun providesEncriptedSharedPreferences(@ApplicationContext context: Context): SharedPreferences {
        val masterKey = providesMasterKey()
        return EncryptedSharedPreferences.create(
            SECRET_SETTINGS,
            masterKey,
            context,
            EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV,
            EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM
        )
    }

    @Provides
    @Singleton
    fun providesCredintialsStorage(
        @ApplicationContext context: Context,
        storage: SharedPreferences = providesEncriptedSharedPreferences(context),
        key: String = CREDENTIALS_KEY,
        gson: Json
    ): CredentialsRepository = CredentialsRepositoryImpl(storage, key, gson)
    companion object {
        private const val SECRET_SETTINGS = "secret_shared_prefs"
        private const val CREDENTIALS_KEY = "secret_credentials"
    }
}