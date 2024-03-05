package app.cashadvisor.authorization.di

import app.cashadvisor.authorization.domain.api.AccountInformationInteractor
import app.cashadvisor.authorization.domain.api.CredentialsRepository
import app.cashadvisor.authorization.domain.impl.AccountInformationInteractorImpl
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent


@Module
@InstallIn(ViewModelComponent::class)
class AuthorizationInteractorModule {
    @Provides
    fun providesAccountInformationInteractor(
        storage: CredentialsRepository,
    ): AccountInformationInteractor = AccountInformationInteractorImpl(storage)

}