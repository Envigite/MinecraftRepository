package com.envigite.minecraftaplication.ui.adapter

import androidx.recyclerview.widget.DiffUtil
import com.envigite.minecraftaplication.domain.model.ItemMinecraft

//Esta clase se encargará de comparar los elementos de la lista anterior con los de la lista nueva. Reemplaza notifyDataSetChanged()
class ItemDiffCallback(private val oldList:List<ItemMinecraft>, private val newList:List<ItemMinecraft>): DiffUtil.Callback() {

    override fun getOldListSize(): Int = oldList.size

    override fun getNewListSize(): Int = newList.size

    override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        return oldList[oldItemPosition].namespacedId == newList[newItemPosition].namespacedId
    }

    override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        return oldList[oldItemPosition] == newList[newItemPosition]
    }
}