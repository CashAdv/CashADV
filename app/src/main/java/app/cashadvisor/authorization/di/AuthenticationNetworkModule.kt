package app.cashadvisor.authorization.di

import app.cashadvisor.authorization.data.api.LoginApiService
import app.cashadvisor.authorization.data.api.RegisterApiService
import app.cashadvisor.authorization.data.api.ResetPasswordApiService
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton
import retrofit2.Retrofit


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

    @Provides
    @Singleton
    fun provideResetPasswordApiService(retrofit: Retrofit):ResetPasswordApiService{
        return retrofit.create(ResetPasswordApiService::class.java)
    }
}