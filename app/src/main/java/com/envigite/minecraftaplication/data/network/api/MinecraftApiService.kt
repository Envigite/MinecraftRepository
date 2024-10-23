package com.envigite.minecraftaplication.data.network.api

import com.envigite.minecraftaplication.data.model.ItemMinecraft
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Path

interface MinecraftApiService {
    @GET("{items}")
    suspend fun getMinecraftItems(@Path("items") items: String):List<ItemMinecraft>
}