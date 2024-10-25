package com.envigite.minecraftaplication.data.repositoy

import android.util.Log
import com.envigite.minecraftaplication.domain.model.ItemMinecraft
import com.envigite.minecraftaplication.data.network.MinecraftApiService
import javax.inject.Inject

class ItemRepository @Inject constructor(
    private val apiService: MinecraftApiService
) {
    suspend fun getMinecraftItems(items: String): List<ItemMinecraft> {
        return try {
            val responseItems = apiService.getMinecraftItems(items) // Recibe List<ItemResponse>
            responseItems.map { it.toDomain() } // Convierte List<ItemResponse> a List<ItemMinecraft>
        } catch (e: Exception) {
            Log.e("ItemRepository", "Error fetching items: ${e.message}", e)
            emptyList() // Devuelve una lista vacía si hay un error.
        }
    }
}