package com.example.frontend.di

import android.content.Context
import android.os.Build
import android.util.Log
import com.example.frontend.R
import com.example.frontend.data.api.ApiService
import com.example.frontend.data.api.CommunityApiService
import com.example.frontend.data.api.NotificationApiService
import com.example.frontend.util.TokenManager
import com.google.gson.Gson
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.runBlocking
import okhttp3.Cache
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.WebSocket
import okhttp3.WebSocketListener
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit
import javax.inject.Named
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {
    @Provides
    @Singleton
    @Named("BaseUrl")
    fun provideBaseUrl(@ApplicationContext context: Context): String {

        return context.getString(R.string.base_url)

        //"http://10.0.2.2:3000/"
        // return if (isEmulator()) "http://10.0.2.2:3000/" else "http://your-real-server.com/"
    }
    @Provides
    @Singleton
    @Named("WebSocketUrl")
    fun provideWsBaseUrl(@ApplicationContext context: Context): String {
        return context.getString(R.string.ws_base_url)
    }

    private fun isEmulator(): Boolean {
        return Build.PRODUCT.contains("sdk") || Build.MODEL.contains("Emulator")
    }

    @Provides
    @Singleton
    fun provideOkHttpClient(
        @ApplicationContext context: Context,
        tokenManager: TokenManager
    ): OkHttpClient {
        val loggingInterceptor = HttpLoggingInterceptor().apply {
            level = HttpLoggingInterceptor.Level.BODY
        }

        val cacheSize = 10 * 1024 * 1024 // 10MB cache
        val cache = Cache(context.cacheDir, cacheSize.toLong())

        return OkHttpClient.Builder()
            .connectTimeout(60, TimeUnit.SECONDS)
            .readTimeout(60, TimeUnit.SECONDS)
            .writeTimeout(60, TimeUnit.SECONDS)
            .addInterceptor(loggingInterceptor)
            .addInterceptor { chain ->
                val originalRequest = chain.request()
                val token = runBlocking { tokenManager.getToken() }
                Log.d("NetworkInterceptor", "Original Request Headers: ${originalRequest.headers}")
                Log.d("NetworkInterceptor", "Token: $token")
                val newRequest = if (token != null) {
                    originalRequest.newBuilder()
                        .header("Authorization", "Bearer $token")
                        .build()
                } else {
                    originalRequest
                }
                Log.d("NetworkInterceptor", "New Request Headers: ${newRequest.headers}")
                chain.proceed(newRequest)
            }
            .cache(cache)
            .retryOnConnectionFailure(true)
            .build()
    }

    @Provides
    @Singleton
    fun provideRetrofit(gson: Gson, okHttpClient: OkHttpClient, @Named("BaseUrl") baseUrl: String): Retrofit {
        return Retrofit.Builder()
            .baseUrl(baseUrl)
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create(gson))
            .build()
    }

    @Provides
    @Singleton
    fun provideApiService(retrofit: Retrofit): ApiService {
        Log.d("NetworkModule", "Creating ApiService with Retrofit: $retrofit")
        return retrofit.create(ApiService::class.java)
    }
    @Provides
    @Singleton
    fun provideCommunityApiService(retrofit: Retrofit): CommunityApiService {
        return retrofit.create(CommunityApiService::class.java)
    }

    @Provides
    @Singleton
    fun provideNotificationApiService(retrofit: Retrofit): NotificationApiService {
        return retrofit.create(NotificationApiService::class.java)
    }

    fun createWebSocket(okHttpClient: OkHttpClient, url: String, listener: WebSocketListener): WebSocket {
        val request = Request.Builder()
            .url(url)
            .build()
        return okHttpClient.newWebSocket(request, listener)
    }
}