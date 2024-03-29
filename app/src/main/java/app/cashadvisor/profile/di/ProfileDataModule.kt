package app.cashadvisor.profile.di

import android.content.Context
import app.cashadvisor.authorization.domain.api.CredentialsRepository
import app.cashadvisor.common.domain.BaseExceptionToErrorMapper
import app.cashadvisor.profile.data.ProfileExceptionToErrorMapper
import app.cashadvisor.profile.data.ProfileInfoMapper
import app.cashadvisor.profile.data.api.ProfileInfoRemoteDataSource
import app.cashadvisor.profile.data.impl.ProfileInfoRepositoryImpl
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
        credentialsRepository: CredentialsRepository,
        mapper: ProfileInfoMapper,
        profileExceptionToErrorMapper: ProfileExceptionToErrorMapper
    ): ProfileInfoRepository = ProfileInfoRepositoryImpl(
        context, remoteDataSource, credentialsRepository, mapper, profileExceptionToErrorMapper
    )

    @Provides
    fun providesProfileExceptionToErrorMapper(): BaseExceptionToErrorMapper =
        ProfileExceptionToErrorMapper()

}