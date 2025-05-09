package com.example.frontend.di

import android.content.Context
import android.os.Build
import com.example.frontend.data.util.ApiService
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import okhttp3.Cache
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {
    @Provides
    @Singleton
    fun provideBaseUrl(@ApplicationContext context: Context): String {
        return  "https://10.0.2.2:3000/"
       // return if (isEmulator()) "https://10.0.2.2:3000/" else "https://your-real-server.com/"
    }

    private fun isEmulator(): Boolean {
        return Build.PRODUCT.contains("sdk") || Build.MODEL.contains("Emulator")
    }

    @Provides
    @Singleton
    fun provideOkHttpClient(@ApplicationContext context: Context): OkHttpClient {
        val loggingInterceptor = HttpLoggingInterceptor().apply {
            level = HttpLoggingInterceptor.Level.BODY // Log toàn bộ yêu cầu và phản hồi
        }

        val cacheSize = 10 * 1024 * 1024 // 10MB cache
        val cache = Cache(context.cacheDir, cacheSize.toLong())

        return OkHttpClient.Builder()
            .connectTimeout(30, TimeUnit.SECONDS)
            .readTimeout(30, TimeUnit.SECONDS)
            .addInterceptor(loggingInterceptor)
            .cache(cache) // Thêm cache để giảm tải mạng
            .build()
    }

    @Provides
    @Singleton
    fun provideRetrofit(okHttpClient: OkHttpClient,baseUrl: String): Retrofit {
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
}