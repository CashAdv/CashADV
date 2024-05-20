package app.cashadvisor.profile.di

import app.cashadvisor.profile.domain.api.InputValidationInteractor
import app.cashadvisor.profile.domain.api.ProfileInfoInteractor
import app.cashadvisor.profile.domain.impl.InputValidationInteractorImpl
import app.cashadvisor.profile.domain.impl.ProfileInfoInteractorImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent

@Module
@InstallIn(ViewModelComponent::class)
interface ProfileInteractorModule {

    @Binds
    fun bindInputValidationInteractor(
        impl: InputValidationInteractorImpl
    ): InputValidationInteractor

    @Binds
    fun bindProfileInfoInteractor(
        impl: ProfileInfoInteractorImpl
    ): ProfileInfoInteractor

}

