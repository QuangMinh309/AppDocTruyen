package com.example.frontend.di

import com.example.frontend.services.navigation.NavigationManager
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object NavigationModule {
//    @Singleton
//    @Provides
//    fun provideNavigationManager(): NavigationManager {
//        return NavigationManager()
//    }
}