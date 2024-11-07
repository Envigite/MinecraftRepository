package com.envigite.minecraftaplication.domain.repository

import com.envigite.minecraftaplication.domain.model.CraftingEntityDomain
import com.envigite.minecraftaplication.domain.model.ItemMinecraft

interface ItemRepository {
    suspend fun getMinecraftItems(items: String): List<ItemMinecraft>
    suspend fun saveCrafting(crafting: CraftingEntityDomain)
    suspend fun loadCraftings(): List<CraftingEntityDomain>
    suspend fun deleteCrafting(craftingId: Long)
}