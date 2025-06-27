
package com.example.frontend.di
import android.content.Context
import androidx.room.Room
import com.example.frontend.data.local.LocalAppDatabase
import com.example.frontend.data.local.dao.ChatDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object LocalDatabaseModule {

    @Provides
    @Singleton
    fun provideDatabase(@ApplicationContext context: Context): LocalAppDatabase {
        return Room.databaseBuilder(
            context,
            LocalAppDatabase::class.java,
            "local_app_database" // Tên DB tùy bạn
        ).build()
    }

    @Provides
    fun provideChatDao(db: LocalAppDatabase): ChatDao = db.chatDao()

//    @Provides
//    fun provideUserDao(db: LocalAppDatabase): UserDao = db.userDao()
}
