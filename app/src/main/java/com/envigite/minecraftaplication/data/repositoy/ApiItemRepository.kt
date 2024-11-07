package com.envigite.minecraftaplication.data.repositoy

import com.envigite.minecraftaplication.data.database.dao.CraftingDao
import com.envigite.minecraftaplication.data.database.entities.toDomain
import com.envigite.minecraftaplication.data.database.entities.toEntity
import com.envigite.minecraftaplication.data.network.MinecraftApiService
import com.envigite.minecraftaplication.domain.model.CraftingEntityDomain
import com.envigite.minecraftaplication.domain.model.ItemMinecraft
import com.envigite.minecraftaplication.domain.repository.ItemRepository
import javax.inject.Inject

class ApiItemRepository @Inject constructor(
    private val apiService: MinecraftApiService,
    private val craftingDao: CraftingDao
) : ItemRepository {
    override suspend fun getMinecraftItems(items: String): List<ItemMinecraft> {
        val responseItems = apiService.getMinecraftItems(items)
        return responseItems.map { it.toDomain() }
    }

    override suspend fun saveCrafting(crafting: CraftingEntityDomain) {
        craftingDao.insert(crafting.toEntity())
    }

    override suspend fun loadCraftings(): List<CraftingEntityDomain> {
        return craftingDao.getAllCraftings().map { it.toDomain() }
    }

    override suspend fun deleteCrafting(craftingId: Long) {
        craftingDao.deleteCraftingById(craftingId)
    }
}