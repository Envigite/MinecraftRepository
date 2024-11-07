package com.envigite.minecraftaplication.domain.usecase

import com.envigite.minecraftaplication.domain.repository.ItemRepository
import javax.inject.Inject

class DeleteCrafting @Inject constructor(
    private val repository: ItemRepository
) {
    suspend operator fun invoke(craftingId: Long) {
        repository.deleteCrafting(craftingId)
    }
}