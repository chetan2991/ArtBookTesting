package com.techmobile.artbooktesting.dependencyinjection

import android.content.Context
import androidx.room.Room
import com.techmobile.artbooktesting.roomdb.ArtsDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Named

@Module
@InstallIn(SingletonComponent::class)
object TestAppModule {
    @Provides
    @Named("testDatabase")
    fun injectInmemoryRoom( @ApplicationContext context: Context)
       = Room.inMemoryDatabaseBuilder(
            context,
            ArtsDatabase::class.java
        ).allowMainThreadQueries()
            .build()

}