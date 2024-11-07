package com.envigite.minecraftaplication.domain.model

data class CraftingEntityDomain(
    val id: Long = 0L,
    val name: String,
    val slots: List<String>
)