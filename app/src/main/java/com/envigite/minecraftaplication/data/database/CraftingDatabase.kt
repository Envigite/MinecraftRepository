package com.envigite.minecraftaplication.data.database

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.envigite.minecraftaplication.data.database.dao.CraftingDao
import com.envigite.minecraftaplication.data.database.entities.CraftingEntity

@Database(entities = [CraftingEntity::class], version = 1)
@TypeConverters(Converters::class)
abstract class CraftingDatabase : RoomDatabase() {
    abstract fun getCraftingDao(): CraftingDao
}