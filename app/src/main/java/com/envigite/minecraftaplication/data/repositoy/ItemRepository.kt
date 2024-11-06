package com.envigite.minecraftaplication.data.repositoy

import com.envigite.minecraftaplication.data.database.dao.CraftingDao
import com.envigite.minecraftaplication.data.database.entities.CraftingEntity
import com.envigite.minecraftaplication.data.network.MinecraftApiService
import com.envigite.minecraftaplication.domain.model.ItemMinecraft
import javax.inject.Inject

class ItemRepository @Inject constructor(
    private val apiService: MinecraftApiService,
    private val craftingDao: CraftingDao
) {
    suspend fun getMinecraftItems(items: String): List<ItemMinecraft> {
        val responseItems = apiService.getMinecraftItems(items)
        return responseItems.map { it.toDomain() }
    }

    suspend fun saveCrafting(crafting: CraftingEntity) {
        craftingDao.insert(crafting)
    }

    suspend fun loadCraftings(): List<CraftingEntity> {
        return craftingDao.getAllCraftings()
    }

    suspend fun deleteCrafting(craftingId: Long) {
        craftingDao.deleteCraftingById(craftingId)
    }
}