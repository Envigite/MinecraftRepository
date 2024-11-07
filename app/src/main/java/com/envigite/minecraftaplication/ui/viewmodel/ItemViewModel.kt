package com.envigite.minecraftaplication.ui.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.envigite.minecraftaplication.data.database.entities.CraftingEntity
import com.envigite.minecraftaplication.data.repositoy.ApiItemRepository
import com.envigite.minecraftaplication.domain.model.CraftingEntityDomain
import com.envigite.minecraftaplication.domain.model.ItemMinecraft
import com.envigite.minecraftaplication.domain.repository.ItemRepository
import com.envigite.minecraftaplication.domain.usecase.DeleteCrafting
import com.envigite.minecraftaplication.domain.usecase.LoadCraftings
import com.envigite.minecraftaplication.domain.usecase.SaveCrafting
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ItemViewModel @Inject constructor(
    private val repository: ItemRepository,
    private val savedCrafting: SaveCrafting,
    private val loadCraftings: LoadCraftings,
    private val deletedCrafting: DeleteCrafting
) : ViewModel() {

    val itemsLiveData = MutableLiveData<List<ItemMinecraft>>()
    private var allItems: List<ItemMinecraft> = emptyList()
    val errorLiveData = MutableLiveData<String>()
    private val _craftingList = MutableLiveData<List<CraftingEntityDomain>>(emptyList())
    val craftingLiveData: LiveData<List<CraftingEntityDomain>> = _craftingList

    fun fetchItems(items: String) {
        viewModelScope.launch {
            try {
                allItems = repository.getMinecraftItems(items)
                itemsLiveData.postValue(allItems)
            } catch (e: Exception) {
                Log.e("ItemViewModel", "Error fetching items: ${e.message}")
                errorLiveData.postValue("Error fetching items: ${e.message}")
            }
        }
    }

    fun loadCraftingsFromDb() {
        viewModelScope.launch {
            try {
                _craftingList.postValue(loadCraftings())
            } catch (e: Exception) {
                errorLiveData.postValue("Error loading craftings: ${e.message}")
            }
        }
    }

    fun saveCrafting(craftingEntity: CraftingEntityDomain) {
        viewModelScope.launch {
            try {
                savedCrafting(craftingEntity)
                loadCraftingsFromDb()
            } catch (e: Exception) {
                errorLiveData.postValue("Error saving crafting: ${e.message}")
            }
        }
    }

    fun getCraftingList(): LiveData<List<CraftingEntityDomain>> = craftingLiveData

    fun deleteCrafting(craftingId: Long) {
        viewModelScope.launch {
            try {
                deletedCrafting(craftingId)
                loadCraftingsFromDb()
            } catch (e: Exception) {
                errorLiveData.postValue("Error deleting crafting: ${e.message}")
            }
        }
    }
}
