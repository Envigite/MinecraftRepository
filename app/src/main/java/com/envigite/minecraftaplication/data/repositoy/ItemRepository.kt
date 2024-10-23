package com.envigite.minecraftaplication.data.repositoy

import com.envigite.minecraftaplication.data.model.ItemMinecraft
import com.envigite.minecraftaplication.data.network.api.MinecraftApiService
import javax.inject.Inject

class ItemRepository @Inject constructor(
    private val apiService: MinecraftApiService
) {
    suspend fun getMinecraftItems(items: String): List<ItemMinecraft> {
        return apiService.getMinecraftItems(items)
    }
}