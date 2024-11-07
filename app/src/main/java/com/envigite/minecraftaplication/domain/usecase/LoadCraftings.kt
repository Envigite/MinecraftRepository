package com.envigite.minecraftaplication.domain.usecase

import com.envigite.minecraftaplication.domain.model.CraftingEntityDomain
import com.envigite.minecraftaplication.domain.repository.ItemRepository
import javax.inject.Inject

class LoadCraftings @Inject constructor(
    private val repository: ItemRepository
) {
    suspend operator fun invoke(): List<CraftingEntityDomain> {
        return repository.loadCraftings()
    }
}