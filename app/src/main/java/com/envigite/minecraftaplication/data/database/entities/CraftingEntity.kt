package com.envigite.minecraftaplication.data.database.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.envigite.minecraftaplication.domain.model.CraftingEntityDomain

@Entity(tableName = "crafting_table")
data class CraftingEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    @ColumnInfo(name = "name") val name: String,
    @ColumnInfo(name = "slots") val slots: List<String>
)

fun CraftingEntity.toDomain() = CraftingEntityDomain(id, name, slots)
fun CraftingEntityDomain.toEntity() = CraftingEntity(id, name, slots)