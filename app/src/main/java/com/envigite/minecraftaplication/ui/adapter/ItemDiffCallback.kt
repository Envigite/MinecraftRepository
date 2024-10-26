package com.envigite.minecraftaplication.ui.adapter

import androidx.recyclerview.widget.DiffUtil
import com.envigite.minecraftaplication.domain.model.ItemMinecraft

class ItemDiffCallback(
    private val oldList: List<ItemMinecraft>,
    private val newList: List<ItemMinecraft>
) : DiffUtil.Callback() {

    override fun getOldListSize(): Int = oldList.size

    override fun getNewListSize(): Int = newList.size

    override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        return oldList[oldItemPosition].namespacedId == newList[newItemPosition].namespacedId
    }

    override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        return oldList[oldItemPosition] == newList[newItemPosition]
    }
}