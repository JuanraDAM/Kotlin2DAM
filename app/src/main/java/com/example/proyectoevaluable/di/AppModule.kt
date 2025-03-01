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

    @Provides
    @Singleton
    fun provideSharedPreferences(@ApplicationContext context: Context): SharedPreferences =
        context.getSharedPreferences("MyPrefs", Context.MODE_PRIVATE)

    @Provides
    @Singleton
    fun provideSharedPrefsDataSource(
        sharedPreferences: SharedPreferences,
        gson: Gson
    ): SharedPrefsDataSource = SharedPrefsDataSource(sharedPreferences, gson)
}
