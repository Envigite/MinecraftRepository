package com.envigite.minecraftaplication.data.network

import com.envigite.minecraftaplication.data.network.response.ItemResponse
import retrofit2.http.GET
import retrofit2.http.Path

interface MinecraftApiService {
    @GET("{items}")
    suspend fun getMinecraftItems(@Path("items") items: String): List<ItemResponse>
}