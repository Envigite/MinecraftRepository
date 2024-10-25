package com.envigite.minecraftaplication.ui.adapter

import android.content.Context
import android.util.TypedValue
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.envigite.minecraftaplication.R
import com.envigite.minecraftaplication.domain.model.ItemMinecraft

class ItemAdapter(private var itemsMinecraft: List<ItemMinecraft>
) : RecyclerView.Adapter<ItemViewHolder>() {

    private var fullItemList: List<ItemMinecraft> = itemsMinecraft // Almacena la lista completa

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_minecraft, parent, false) //Esta línea utiliza un LayoutInflater para crear una instancia de la vista definida en el archivo XML item_minecraft.xml. El inflate convierte el XML en un objeto View que puede ser utilizado en la aplicación.
        return ItemViewHolder(view)
    }

    override fun onBindViewHolder(holder: ItemViewHolder, position: Int) {
            val item = itemsMinecraft[position] // Obtiene el elemento específico de la lista itemsMinecraft.
            holder.render(item) // Llama al método render del ItemViewHolder pasando el item obtenido.
    }

    override fun getItemCount() = itemsMinecraft.size /*itemsMinecraft.size es el número total de objetos en la lista itemsMinecraft.
                                                           Esto le indica al RecyclerView cuántos elementos debe mostrar.*/

    // Método para actualizar los elementos
    fun updateItems(newItems: List<ItemMinecraft>){
        val diffCallback = ItemDiffCallback(itemsMinecraft, newItems)
        val diffResult = DiffUtil.calculateDiff(diffCallback)
        fullItemList = newItems // Actualiza la lista interna del Adapter.
        diffResult.dispatchUpdatesTo(this) // Notifica al adaptador sobre los cambios.
    }

    // Método para filtrar los elementos
    fun filter(query: String?) {
        val filteredList= if (query.isNullOrEmpty()){
            fullItemList
        } else {
            fullItemList.filter {it.name.contains(query, ignoreCase = true) }// Filtra por nombre.
        }

        val diffCallback = ItemDiffCallback(itemsMinecraft, filteredList)
        val diffResult = DiffUtil.calculateDiff(diffCallback)
        itemsMinecraft = filteredList // Actualiza la lista interna del Adapter.
        diffResult.dispatchUpdatesTo(this) // Notifica al adaptador sobre los cambios.
    }
}