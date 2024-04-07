package app.cashadvisor.profile.di

import android.content.Context
import android.content.SharedPreferences
import androidx.security.crypto.EncryptedSharedPreferences
import androidx.security.crypto.MasterKeys
import app.cashadvisor.authorization.domain.api.CredentialsRepository
import app.cashadvisor.common.domain.BaseExceptionToErrorMapper
import app.cashadvisor.profile.data.api.ProfileInfoRemoteDataSource
import app.cashadvisor.profile.data.api.ProfileInfoStorage
import app.cashadvisor.profile.data.impl.ProfileInfoRepositoryImpl
import app.cashadvisor.profile.data.impl.ProfileInfoStorageImpl
import app.cashadvisor.profile.data.mapper.ProfileExceptionToErrorMapper
import app.cashadvisor.profile.data.mapper.ProfileInfoMapper
import app.cashadvisor.profile.domain.api.ProfileInfoRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import kotlinx.serialization.json.Json
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class ProfileDataModule {

    @Provides
    @Singleton
    fun provideProfileInfoRepository(
        remoteDataSource: ProfileInfoRemoteDataSource,
        storage: ProfileInfoStorage,
        credentialsRepository: CredentialsRepository,
        mapper: ProfileInfoMapper,
        profileExceptionToErrorMapper: ProfileExceptionToErrorMapper
    ): ProfileInfoRepository = ProfileInfoRepositoryImpl(
        remoteDataSource = remoteDataSource,
        credentialsRepository = credentialsRepository,
        storage = storage,
        mapper = mapper,
        profileExceptionToErrorMapper = profileExceptionToErrorMapper
    )

    @Provides
    fun providesProfileExceptionToErrorMapper(): BaseExceptionToErrorMapper =
        ProfileExceptionToErrorMapper()


    @Provides
    @Singleton
    fun providesMasterKey(): String =
        MasterKeys.getOrCreate(MasterKeys.AES256_GCM_SPEC)

    @Provides
    @Singleton
    fun providesEncryptedSharedPreferences(@ApplicationContext context: Context): SharedPreferences {
        val masterKey = providesMasterKey()
        return EncryptedSharedPreferences.create(
            PROFILE_PREFS,
            masterKey,
            context,
            EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV,
            EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM
        )
    }

    @Provides
    @Singleton
    fun providesProfileInfoStorage(
        @ApplicationContext context: Context,
        storage: SharedPreferences = providesEncryptedSharedPreferences(context),
        key: String = PROFILE_KEY,
        gson: Json
    ): ProfileInfoStorage = ProfileInfoStorageImpl(storage, key, gson)

    companion object {
        private const val PROFILE_PREFS = "profile_shared_prefs"
        private const val PROFILE_KEY = "profile_key"
    }
}