package com.example.frontend.di

import android.content.Context
import android.os.Build
import com.example.frontend.R
import com.example.frontend.data.api.ApiService
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
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
    fun provideOkHttpClient(@ApplicationContext context: Context): OkHttpClient {
        val loggingInterceptor = HttpLoggingInterceptor().apply {
            level = HttpLoggingInterceptor.Level.BODY
        }

        val cacheSize = 10 * 1024 * 1024 // 10MB cache
        val cache = Cache(context.cacheDir, cacheSize.toLong())

        return OkHttpClient.Builder()
            .connectTimeout(60, TimeUnit.SECONDS) // Tăng timeout lên 60 giây
            .readTimeout(60, TimeUnit.SECONDS) // Tăng timeout lên 60 giây
            .writeTimeout(60, TimeUnit.SECONDS)
            .addInterceptor(loggingInterceptor)
            .cache(cache)
            .retryOnConnectionFailure(true) // Thêm retry khi kết nối thất bại
            .build()
    }

    @Provides
    @Singleton
    fun provideRetrofit(okHttpClient: OkHttpClient,  @Named("BaseUrl") baseUrl: String): Retrofit {
        return Retrofit.Builder()
            .baseUrl(baseUrl) // Sử dụng HTTPS, thay bằng URL thực tế khi cần
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    @Provides
    @Singleton
    fun provideApiService(retrofit: Retrofit): ApiService {
        return retrofit.create(ApiService::class.java)
    }

    // khởi tạo WebSocket connection
    fun createWebSocket(okHttpClient: OkHttpClient, url: String, listener: WebSocketListener): WebSocket {
        val request = Request.Builder()
            .url(url)
            .build()
        return okHttpClient.newWebSocket(request, listener)
    }
}