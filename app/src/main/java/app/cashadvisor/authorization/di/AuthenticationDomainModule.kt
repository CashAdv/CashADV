package app.cashadvisor.authorization.di

import app.cashadvisor.authorization.domain.api.InputValidationInteractor
import app.cashadvisor.authorization.domain.api.LoginInteractor
import app.cashadvisor.authorization.domain.api.RegisterInteractor
import app.cashadvisor.authorization.domain.impl.InputValidationInteractorImpl
import app.cashadvisor.authorization.domain.impl.LoginInteractorImpl
import app.cashadvisor.authorization.domain.impl.RegisterInteractorImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent

@Module
@InstallIn(ViewModelComponent::class)
interface AuthenticationDomainModule {

    @Binds
    fun bindRegisterInteractor(
        impl: RegisterInteractorImpl
    ): RegisterInteractor

    @Binds
    fun bindLoginInteractor(
        impl: LoginInteractorImpl
    ): LoginInteractor

    @Binds
    fun bindInputValidationInteractor(
        impl: InputValidationInteractorImpl
    ): InputValidationInteractor

}