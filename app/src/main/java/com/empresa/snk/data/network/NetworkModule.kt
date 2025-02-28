package com.empresa.snk.data.network

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {

    @Singleton
    @Provides
    fun provideRetrofit(): Retrofit {
        return Retrofit.Builder()
            .baseUrl("https://api.attackontitanapi.com/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()


    }
    @Singleton
    @Provides
    fun provideSNKApi(retrofit: Retrofit): SNKApi {
        return retrofit.create(SNKApi::class.java)
    }
}