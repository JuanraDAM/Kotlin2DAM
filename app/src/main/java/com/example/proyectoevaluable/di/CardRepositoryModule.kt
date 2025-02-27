package com.example.proyectoevaluable.di

import com.example.proyectoevaluable.domain.cards.repository.CardRepository
import com.example.proyectoevaluable.data.cards.repository.CardRepositoryImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class CardRepositoryModule {
    @Binds
    @Singleton
    abstract fun bindCardRepository(impl: CardRepositoryImpl): CardRepository
}
