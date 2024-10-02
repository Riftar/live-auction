package com.riftar.liveauctionapp.data.liveauction.di

import com.google.firebase.database.FirebaseDatabase
import com.riftar.liveauctionapp.data.liveauction.repository.LiveAuctionRepositoryImpl
import com.riftar.liveauctionapp.domain.liveauction.repository.LiveAuctionRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object RepositoryModule {

    @Provides
    @Singleton
    fun provideLiveAuctionRepository(
        firebaseDatabase: FirebaseDatabase
    ): LiveAuctionRepository {
        return LiveAuctionRepositoryImpl(firebaseDatabase)
    }
}