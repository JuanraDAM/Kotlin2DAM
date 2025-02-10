package com.example.proyectoevaluable.di

import android.content.Context
import android.content.SharedPreferences
import com.example.proyectoevaluable.data.cards.datasource.SharedPrefsDataSource
import com.example.proyectoevaluable.data.cards.repository.CardRepositoryImpl
import com.example.proyectoevaluable.domain.cards.repository.CardRepository
import com.example.proyectoevaluable.domain.cards.usecase.UseCaseLoadCards
import com.example.proyectoevaluable.domain.cards.usecase.UseCaseSaveCards
// import com.example.proyectoevaluable.domain.cards.usecase.UseCaseDeleteCard // Si lo deseas
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
    fun provideGson(): Gson = Gson()

    @Provides
    @Singleton
    fun provideSharedPrefsDataSource(
        sharedPreferences: SharedPreferences,
        gson: Gson
    ): SharedPrefsDataSource = SharedPrefsDataSource(sharedPreferences, gson)

    @Provides
    @Singleton
    fun provideCardRepository(
        dataSource: SharedPrefsDataSource
    ): CardRepository = CardRepositoryImpl(dataSource)

    @Provides
    fun provideUseCaseLoadCards(repository: CardRepository): UseCaseLoadCards =
        UseCaseLoadCards(repository)

    @Provides
    fun provideUseCaseSaveCards(repository: CardRepository): UseCaseSaveCards =
        UseCaseSaveCards(repository)

    // Si deseas proveer un caso de uso para eliminar:
    // @Provides
    // fun provideUseCaseDeleteCard(repository: CardRepository): UseCaseDeleteCard =
    //     UseCaseDeleteCard(repository)
}
