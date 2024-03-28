package app.cashadvisor.profile.di

import app.cashadvisor.common.domain.BaseExceptionToErrorMapper
import app.cashadvisor.profile.data.ProfileExceptionToErrorMapper
import app.cashadvisor.profile.data.impl.ProfileInfoRepositoryImpl
import app.cashadvisor.profile.domain.api.ProfileInfoRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
interface ProfileDataModule {

    @Binds
    @Singleton
    fun bindProfileInfoRepository(
        impl: ProfileInfoRepositoryImpl
    ): ProfileInfoRepository

    @Binds
    fun bindProfileExceptionToErrorMapper(
        impl: ProfileExceptionToErrorMapper
    ): BaseExceptionToErrorMapper
}