package com.lucreziacarena.mycoachassistant.di

import android.content.Context
import com.lucreziacarena.mycoachassistant.db.AppDatabase
import com.squareup.sqldelight.android.AndroidSqliteDriver
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object SQLDelightModule {

    @Singleton
    @Provides
    fun provideAndroidDriver(@ApplicationContext applicationContext: Context): AndroidSqliteDriver {
        return AndroidSqliteDriver(
            schema = AppDatabase.Schema,
            context = applicationContext,
            name = "athletes.db"
        )
    }

    @Singleton
    @Provides
    fun provideSQLDelightDatabase(driver: AndroidSqliteDriver): AppDatabase {
        return AppDatabase(driver)
    }
}

