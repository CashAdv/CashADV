package app.cashadvisor.profile.di

import android.content.Context
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
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class ProfileDataModule {

    @Provides
    @Singleton
    fun provideProfileInfoRepository(
        @ApplicationContext context: Context,
        remoteDataSource: ProfileInfoRemoteDataSource,
        storage: ProfileInfoStorage,
        credentialsRepository: CredentialsRepository,
        mapper: ProfileInfoMapper,
        profileExceptionToErrorMapper: ProfileExceptionToErrorMapper
    ): ProfileInfoRepository = ProfileInfoRepositoryImpl(
        context = context,
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
    fun providesProfileInfoStorage(): ProfileInfoStorage = ProfileInfoStorageImpl()
}