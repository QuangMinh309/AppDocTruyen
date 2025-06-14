package com.example.frontend.di

import android.content.Context
import com.example.frontend.data.api.ApiService
import com.example.frontend.data.repository.AuthRepository
import com.example.frontend.data.repository.HomeRepository
import com.example.frontend.services.navigation.NavigationManager
import com.example.frontend.util.TokenManager
import com.example.frontend.util.UserPreferences
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun provideContext(@ApplicationContext context: Context): Context {
        return context
    }

    @Provides
    @Singleton
    fun provideAuthRepository(
        apiService: ApiService,
        tokenManager: TokenManager,
        @ApplicationContext context: Context
    ): AuthRepository {
        return AuthRepository(apiService, tokenManager, context)
    }

    @Provides
    @Singleton
    fun provideHomeRepository(apiService: ApiService): HomeRepository {
        return HomeRepository(apiService)
    }

    @Provides
    @Singleton
    fun provideNavigationManager(): NavigationManager {
        return NavigationManager()
    }
}