package com.envigite.minecraftaplication.data.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.envigite.minecraftaplication.data.database.entities.CraftingEntity

@Dao
interface CraftingDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(crafting: CraftingEntity)

    @Query("SELECT * FROM crafting_table")
    suspend fun getAllCraftings(): List<CraftingEntity>

    @Query("DELETE FROM crafting_table WHERE id = :craftingId")
    suspend fun deleteCraftingById(craftingId: Long)

    @Query("DELETE FROM crafting_table")
    suspend fun deletedAllCraftings()
}