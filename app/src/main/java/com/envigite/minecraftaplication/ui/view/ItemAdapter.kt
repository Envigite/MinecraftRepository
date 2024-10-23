package com.envigite.minecraftaplication.ui.view

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.envigite.minecraftaplication.R
import com.envigite.minecraftaplication.data.model.ItemMinecraft

class ItemAdapter(private var itemsMinecraft: List<ItemMinecraft>
) : RecyclerView.Adapter<ItemViewHolder>(){
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_minecraft, parent, false) //Esta línea utiliza un LayoutInflater para crear una instancia de la vista definida en el archivo XML item_minecraft.xml. El inflate convierte el XML en un objeto View que puede ser utilizado en la aplicación.
        return ItemViewHolder(view)
    }

    override fun onBindViewHolder(holder: ItemViewHolder, position: Int) {
        val item = itemsMinecraft[position] // Obtiene el elemento específico de la lista itemsMinecraft.
        holder.render(item) // Llama al método render del ItemViewHolder pasando el item obtenido.
        // Aquí se pueden configurar otros elementos de la vista si es necesario.
    }

    override fun getItemCount() = itemsMinecraft.size /*itemsMinecraft.size es el número total de objetos en la lista itemsMinecraft.
                                                           Esto le indica al RecyclerView cuántos elementos debe mostrar.*/

    fun updateItems(newItems: List<ItemMinecraft>){
        itemsMinecraft = newItems // Se actualiza la lista interna del Adapter, esto permite que el adapter tenga la información más reciente que debe mostrar.
        notifyDataSetChanged() // Se notifica al RecyclerView que los datos han cambiado.
    }
}