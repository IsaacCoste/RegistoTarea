package com.example.registotarea.di

import android.content.Context
import androidx.room.Room
import com.example.registotarea.data.local.database.TareaDb
import com.example.registotarea.data.remote.TareaApi
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
object AppModule {
    @Provides
    @Singleton
    fun provideTareaDb(@ApplicationContext appContext: Context) =
        Room.databaseBuilder(
            appContext,
            TareaDb::class.java,
            "Tarea.db"
        ).fallbackToDestructiveMigration()
            .build()

    @Provides
    @Singleton
    fun provideTareaDao(tareaDb: TareaDb) = tareaDb.tareaDao()

    @Provides
    @Singleton
    fun provideUsuarioDao(tareaDb: TareaDb) = tareaDb.usuarioDao()

    @Provides
    @Singleton
    fun provideProyectoDao(tareaDb: TareaDb) = tareaDb.proyectoDao()

    const val BASE_URL = "https://apitareas-d2bcf9hqgtfebvdr.eastus2-01.azurewebsites.net/"

    @Singleton
    @Provides
    fun provicesMoshi(): Moshi =
        Moshi.Builder()
            .add(KotlinJsonAdapterFactory())
            .build()

    @Singleton
    @Provides
    fun provicesTareaApi(moshi: Moshi): TareaApi {
        return Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(MoshiConverterFactory.create(moshi))
            .build()
            .create(TareaApi::class.java)
    }
}