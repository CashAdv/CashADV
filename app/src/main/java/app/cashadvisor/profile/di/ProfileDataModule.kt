package app.cashadvisor.profile.di

import app.cashadvisor.profile.data.api.ProfileInfoApiService
import app.cashadvisor.profile.data.api.ProfileInfoRemoteDataSource
import app.cashadvisor.profile.data.impl.ProfileInfoRemoteDataSourceImpl
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
interface ProfileDataModule {

    @Provides
    @Singleton
    fun provideProfileInfoApiService(retrofit: Retrofit): ProfileInfoApiService {
        return retrofit.create(ProfileInfoApiService::class.java)
    }

    @Binds
    @Singleton
    fun bindProfileInfoRemoteDataSource(
        impl: ProfileInfoRemoteDataSourceImpl
    ): ProfileInfoRemoteDataSource
}