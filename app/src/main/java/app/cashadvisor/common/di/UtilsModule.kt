package app.cashadvisor.common.di

import app.cashadvisor.common.utils.MoneyFormatter
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class UtilsModule {

    @Provides
    @Singleton
    fun providesMoneyFormatter(): MoneyFormatter = MoneyFormatter()
}