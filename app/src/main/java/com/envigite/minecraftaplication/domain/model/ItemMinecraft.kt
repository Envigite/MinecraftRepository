package com.envigite.minecraftaplication.domain.model

data class ItemMinecraft(
    val name: String,
    val namespacedId: String,
    val description: String,
    val image: String,
    val stackSize: Int,
    val renewable: Boolean
) {
    fun getSectionIndex(): String = name.firstOrNull()?.uppercase() ?: "#"
}