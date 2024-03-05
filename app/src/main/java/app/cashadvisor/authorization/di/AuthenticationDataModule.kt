package app.cashadvisor.authorization.di

import app.cashadvisor.authorization.data.api.LoginRemoteDataSource
import app.cashadvisor.authorization.data.api.RegisterRemoteDataSource
import app.cashadvisor.authorization.data.impl.LoginRemoteDataSourceImpl
import app.cashadvisor.authorization.data.impl.LoginRepositoryImpl
import app.cashadvisor.authorization.data.impl.RegisterRemoteDataSourceImpl
import app.cashadvisor.authorization.data.impl.RegisterRepositoryImpl
import app.cashadvisor.authorization.domain.LoginExceptionToErrorMapper
import app.cashadvisor.authorization.domain.RegisterExceptionToErrorMapper
import app.cashadvisor.authorization.domain.api.LoginRepository
import app.cashadvisor.authorization.domain.api.RegisterRepository
import app.cashadvisor.common.domain.BaseExceptionToErrorMapper
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Qualifier
import javax.inject.Singleton

@Qualifier
@Retention(AnnotationRetention.BINARY)
annotation class RegisterExceptionMapper

@Qualifier
@Retention(AnnotationRetention.BINARY)
annotation class LoginExceptionMapper

@Module
@InstallIn(SingletonComponent::class)
interface LoginAndRegisterDataModule {
    @Singleton
    @Binds
    fun bindRegisterRemoteDataSource(
        impl: RegisterRemoteDataSourceImpl
    ): RegisterRemoteDataSource

    @Singleton
    @Binds
    fun bindLoginRemoteDataSource(
        impl: LoginRemoteDataSourceImpl
    ): LoginRemoteDataSource


    @Singleton
    @Binds
    fun bindRegisterRepository(
        impl: RegisterRepositoryImpl
    ): RegisterRepository

    @Singleton
    @Binds
    fun bindLoginRepository(
        impl: LoginRepositoryImpl
    ): LoginRepository

    @RegisterExceptionMapper
    @Binds
    fun bindRegisterExceptionToErrorMapper(
        impl: RegisterExceptionToErrorMapper
    ): BaseExceptionToErrorMapper

    @LoginExceptionMapper
    @Binds
    fun bindLoginExceptionToErrorMapper(
        impl: LoginExceptionToErrorMapper
    ): BaseExceptionToErrorMapper

}