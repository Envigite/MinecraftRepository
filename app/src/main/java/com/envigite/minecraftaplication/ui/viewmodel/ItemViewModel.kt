package com.envigite.minecraftaplication.ui.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.envigite.minecraftaplication.data.database.entities.CraftingEntity
import com.envigite.minecraftaplication.data.repositoy.ItemRepository
import com.envigite.minecraftaplication.domain.model.ItemMinecraft
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ItemViewModel @Inject constructor(
    private val repository: ItemRepository
) : ViewModel() {

    val itemsLiveData = MutableLiveData<List<ItemMinecraft>>()
    private var allItems: List<ItemMinecraft> = emptyList()
    val errorLiveData = MutableLiveData<String>()
    private val _craftingList = MutableLiveData<List<CraftingEntity>>(emptyList())
    val craftingLiveData: LiveData<List<CraftingEntity>> = _craftingList

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
                val craftings = repository.loadCraftings()
                _craftingList.postValue(craftings)
            } catch (e: Exception) {
                errorLiveData.postValue("Error loading craftings: ${e.message}")
            }
        }
    }

    fun saveCrafting(craftingEntity: CraftingEntity) {
        viewModelScope.launch {
            try {
                repository.saveCrafting(craftingEntity)
                loadCraftingsFromDb()
            } catch (e: Exception) {
                errorLiveData.postValue("Error saving crafting: ${e.message}")
            }
        }
    }

    fun getCraftingList(): LiveData<List<CraftingEntity>> = craftingLiveData

    fun deleteCrafting(craftingId: Long) {
        viewModelScope.launch {
            try {
                repository.deleteCrafting(craftingId)
                loadCraftingsFromDb()
            } catch (e: Exception) {
                errorLiveData.postValue("Error deleting crafting: ${e.message}")
            }
        }
    }
}
