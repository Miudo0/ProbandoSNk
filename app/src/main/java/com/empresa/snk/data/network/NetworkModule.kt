package com.empresa.snk.data.network

import android.content.Context
import androidx.room.Room
import com.empresa.snk.data.room.isWatchedDatabase.IsWatchedDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
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
    @Singleton
    @Provides
    fun provideSNKApiOrganization(retrofit: Retrofit): SNKApiOrganization {
        return retrofit.create(SNKApiOrganization::class.java)

    }
    @Singleton
    @Provides
    fun provideSNKApiLocations(retrofit: Retrofit): SNKApiLocations {
        return retrofit.create(SNKApiLocations::class.java)
    }

@Singleton
@Provides
fun providesGetDatabaseIsWatched(@ApplicationContext context: Context): IsWatchedDatabase =
    Room.databaseBuilder(
        context,
        IsWatchedDatabase ::class.java,
        "baseDatosIsWatched"
    ).build()
}