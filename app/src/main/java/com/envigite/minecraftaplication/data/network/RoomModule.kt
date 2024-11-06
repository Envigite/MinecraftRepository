package com.envigite.minecraftaplication.data.network

import android.content.Context
import androidx.room.Room
import com.envigite.minecraftaplication.data.database.CraftingDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object RoomModule {

    @Singleton
    @Provides
    fun providerRoom(@ApplicationContext context: Context) =
        Room.databaseBuilder(context, CraftingDatabase::class.java, "crafting_database").build()

    @Singleton
    @Provides
    fun provideCraftingDao(db: CraftingDatabase) = db.getCraftingDao()
}