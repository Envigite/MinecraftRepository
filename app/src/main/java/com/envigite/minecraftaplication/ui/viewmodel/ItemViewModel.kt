package com.envigite.minecraftaplication.ui.viewmodel

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.envigite.minecraftaplication.domain.model.ItemMinecraft
import com.envigite.minecraftaplication.data.repositoy.ItemRepository
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
}