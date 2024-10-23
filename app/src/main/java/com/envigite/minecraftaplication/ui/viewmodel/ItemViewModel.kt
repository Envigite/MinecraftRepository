package com.envigite.minecraftaplication.ui.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.envigite.minecraftaplication.data.model.ItemMinecraft
import com.envigite.minecraftaplication.data.repositoy.ItemRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel //Indica que esta clase es un ViewModel que será gestionado por Hilt.
class ItemViewModel @Inject constructor(
    private val repository: ItemRepository
) : ViewModel() {

    val itemsLiveData = MutableLiveData<List<ItemMinecraft>>() //Es un tipo de dato que puede ser observado. Permite a la UI reaccionar automáticamente a los cambios en la lista de ItemMinecraft.
    val errorLiveData = MutableLiveData<String>()

    fun fetchItems(items: String) { //Este método se llama para obtener los elementos de Minecraft desde el repositorio.
        viewModelScope.launch {//Esto permite realizar operaciones asincrónicas.
            try {
                val items = repository.getMinecraftItems(items) // Llama al método del repositorio que obtiene la lista de elementos de Minecraft.
                itemsLiveData.postValue(items) //Actualiza itemsLiveData con la lista de elementos obtenida. Esto notifica a los observadores (como la UI) que hay nuevos datos disponibles.
            } catch (e: Exception) {
                errorLiveData.postValue("Error al cargar los datos: ${e.message}")
            }
        }
    }
}