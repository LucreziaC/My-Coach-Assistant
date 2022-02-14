package com.lucreziacarena.mycoachassistant.di

import com.lucreziacarena.mycoachassistant.repository.Repository
import com.lucreziacarena.mycoachassistant.repository.RepositoryImpl
import com.lucreziacarena.mycoachassistant.repository.api.ApiHelper
import com.lucreziacarena.mycoachassistant.repository.api.ApiHelperImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class AppModule {

    /*@Binds
    @Singleton
    abstract fun bindsRemoteDataSource(remoteDataSource: ApiHelperImpl): ApiHelper

    @Binds
    @Singleton
    abstract fun bindsRepository(repository: RepositoryImpl) : Repository
*/
}

