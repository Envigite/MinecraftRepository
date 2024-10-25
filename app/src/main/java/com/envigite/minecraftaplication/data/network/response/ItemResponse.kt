package com.envigite.minecraftaplication.data.network.response

import com.envigite.minecraftaplication.domain.model.ItemMinecraft
import com.google.gson.annotations.SerializedName

data class ItemResponse(
    @SerializedName("name") val name: String,
    @SerializedName("namespacedId") val namespacedId: String,
    @SerializedName("description") val description: String,
    @SerializedName("image") val image: String,
    @SerializedName("stackSize") val stackSize: Int,
    @SerializedName("renewable") val renewable: Boolean
) {
    fun toDomain(): ItemMinecraft {
        return ItemMinecraft(
            name = name,
            namespacedId = namespacedId,
            description = description,
            image = image,
            stackSize = stackSize,
            renewable = renewable
        )
    }
}