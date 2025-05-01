package com.app.fabricexercise.di

import android.content.Context
import com.app.fabricexercise.data.repository.RegistrationRepository
import com.app.fabricexercise.data.repository.RegistrationRepositoryImpl
import com.app.fabricexercise.domain.usecase.RegistrationUseCase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    fun provideRegistrationRepository(@ApplicationContext context: Context): RegistrationRepository {
        return RegistrationRepositoryImpl(context)
    }

    @Provides
    fun provideRegistrationUseCase(repository: RegistrationRepository): RegistrationUseCase {
        return RegistrationUseCase(repository)
    }
}
