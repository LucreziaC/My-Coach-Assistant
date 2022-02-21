package com.lucreziacarena.mycoachassistant.di

import com.lucreziacarena.mycoachassistant.repository.api.ApiService
import com.lucreziacarena.mycoachassistant.repository.api.ApiServiceImpl
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import io.ktor.client.*
import io.ktor.client.engine.android.*
import io.ktor.client.features.json.*
import io.ktor.client.features.json.serializer.*
import io.ktor.client.features.logging.*
import javax.inject.Singleton


@Module
@InstallIn(SingletonComponent::class)
object NetworkingModule {

//KTOR
    @Provides
    @Singleton
    fun provideHttpClient(): HttpClient = HttpClient(Android) {
        install(Logging) {
            level = LogLevel.ALL
        }

        install(JsonFeature) {
            serializer = KotlinxSerializer()
        }
    }

    @Provides
    @Singleton
    fun providePostsApi(client: HttpClient): ApiService = ApiServiceImpl(client)

}
