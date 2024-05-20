package app.cashadvisor.profile.di

import app.cashadvisor.profile.data.mapper.NetworkToProfileExceptionMapper
import app.cashadvisor.profile.data.api.ProfileInfoApiService
import app.cashadvisor.profile.data.api.ProfileInfoRemoteDataSource
import app.cashadvisor.profile.data.impl.ProfileInfoRemoteDataSourceImpl
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class ProfileNetworkModule {
    @Provides
    @Singleton
    fun provideProfileInfoApiService(retrofit: Retrofit): ProfileInfoApiService {
        return retrofit.create(ProfileInfoApiService::class.java)
    }

    @Provides
    @Singleton
    fun provideProfileInfoRemoteDataSource(
        profileInfoApiService: ProfileInfoApiService,
        networkToProfileExceptionMapper: NetworkToProfileExceptionMapper
    ): ProfileInfoRemoteDataSource = ProfileInfoRemoteDataSourceImpl(
        profileInfoApiService,
        networkToProfileExceptionMapper
    )
}