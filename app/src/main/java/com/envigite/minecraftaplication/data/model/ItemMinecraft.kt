package com.envigite.minecraftaplication.data.model

data class ItemMinecraft (
    val name: String,
    val namespacedId: String,
    val description: String,
    val image: String,
    val stackSize: Int,
    val renewable: Boolean
)
