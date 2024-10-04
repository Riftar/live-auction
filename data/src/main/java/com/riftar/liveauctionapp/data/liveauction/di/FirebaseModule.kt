package com.riftar.liveauctionapp.data.liveauction.di

import com.google.firebase.Firebase
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.database
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object FirebaseModule {

    @Provides
    @Singleton
    fun provideFirebaseDatabase(): FirebaseDatabase {
        return Firebase.database.apply {
            // Optionally set the database URL if you're not using the default one
            // setPersistenceEnabled(true) // Enable offline persistence if needed
        }
    }
}