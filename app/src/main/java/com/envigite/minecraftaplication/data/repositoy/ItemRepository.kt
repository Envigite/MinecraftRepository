package com.envigite.minecraftaplication.data.repositoy

import android.util.Log
import com.envigite.minecraftaplication.data.network.MinecraftApiService
import com.envigite.minecraftaplication.domain.model.ItemMinecraft
import javax.inject.Inject

class ItemRepository @Inject constructor(
    private val apiService: MinecraftApiService
) {
    suspend fun getMinecraftItems(items: String): List<ItemMinecraft> {
        return try {
            val responseItems = apiService.getMinecraftItems(items)
            responseItems.map { it.toDomain() }
        } catch (e: Exception) {
            Log.e("ItemRepository", "Error fetching items: ${e.message}", e)
            emptyList()
        }
    }
}