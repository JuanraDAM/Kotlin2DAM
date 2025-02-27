package com.example.proyectoevaluable.di

import android.content.Context
import android.content.SharedPreferences
import com.example.proyectoevaluable.data.cards.datasource.SharedPrefsDataSource
import com.google.gson.Gson
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    // Provisión de SharedPreferences para otros usos
    @Provides
    @Singleton
    fun provideSharedPreferences(@ApplicationContext context: Context): SharedPreferences =
        context.getSharedPreferences("MyPrefs", Context.MODE_PRIVATE)

    // Elimina o comenta el proveedor de Gson aquí:
    /*
    @Provides
    @Singleton
    fun provideGson(): Gson = Gson()
    */

    @Provides
    @Singleton
    fun provideSharedPrefsDataSource(
        sharedPreferences: SharedPreferences,
        gson: Gson // Esta instancia se obtendrá de NetworkModule
    ): SharedPrefsDataSource = SharedPrefsDataSource(sharedPreferences, gson)
}
