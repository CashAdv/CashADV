package app.cashadvisor.authorization.di

import app.cashadvisor.authorization.data.api.LoginApiService
import app.cashadvisor.authorization.data.api.RegisterApiService
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import javax.inject.Singleton


@Module
@InstallIn(SingletonComponent::class)
class AuthenticationNetworkModule {
    @Provides
    @Singleton
    fun provideRegisterApiService(retrofit: Retrofit): RegisterApiService {
        return retrofit.create(RegisterApiService::class.java)
    }

    @Provides
    @Singleton
    fun provideLoginApiService(retrofit: Retrofit): LoginApiService {
        return retrofit.create(LoginApiService::class.java)
    }
}