package com.example.frontend.di

import android.content.Context
import com.example.frontend.data.api.ApiService
import com.example.frontend.data.api.NotificationApiService
import com.example.frontend.data.repository.AuthRepository
import com.example.frontend.data.repository.HomeRepository
import com.example.frontend.data.repository.NotificationRepository
import com.example.frontend.services.navigation.NavigationManager
import com.example.frontend.services.websocket.WebSocketManager
import com.example.frontend.util.TokenManager
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.google.gson.TypeAdapter
import com.google.gson.stream.JsonReader
import com.google.gson.stream.JsonToken
import com.google.gson.stream.JsonWriter
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import java.time.LocalDateTime
import java.time.OffsetDateTime
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
    fun provideGson(): Gson {
        return GsonBuilder()
            .registerTypeAdapter(LocalDateTime::class.java, LocalDateTimeTypeAdapter())
            .create()
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

    @Provides
    @Singleton
    fun provideTokenManager(@ApplicationContext context: Context): TokenManager {
        return TokenManager(context)
    }

    @Provides
    @Singleton
    fun provideNotificationRepository(
        apiService: NotificationApiService,
        webSocketManager: WebSocketManager,
        @ApplicationContext context: Context,
        gson: Gson
    ): NotificationRepository {
        return NotificationRepository(apiService, webSocketManager,context, gson)
    }
}





class LocalDateTimeTypeAdapter : TypeAdapter<LocalDateTime>() {
    override fun write(out: JsonWriter, value: LocalDateTime?) {
        if (value == null) {
            out.nullValue()
        } else {
            out.value(value.toString()) // hoặc format nếu bạn muốn
        }
    }


    override fun read(reader: JsonReader): LocalDateTime? {
        if (reader.peek() == JsonToken.NULL) {
            reader.nextNull()
            return null
        }


        val string = reader.nextString()
        return try {
            OffsetDateTime.parse(string).toLocalDateTime()
        } catch (e: Exception) {
            null
        }
    }
}

