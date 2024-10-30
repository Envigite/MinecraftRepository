package com.envigite.minecraftaplication.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.fragment.app.FragmentManager
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.envigite.minecraftaplication.databinding.ItemMinecraftBinding
import com.envigite.minecraftaplication.domain.model.ItemMinecraft
import com.l4digital.fastscroll.FastScroller

class ItemAdapter(private var itemsMinecraft: List<ItemMinecraft>, private val fragmentManager: FragmentManager) :
    RecyclerView.Adapter<ItemViewHolder>(), FastScroller.SectionIndexer {

    private var fullItemList: List<ItemMinecraft> = itemsMinecraft

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemViewHolder {
        val binding =
            ItemMinecraftBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ItemViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ItemViewHolder, position: Int) {
        val item = itemsMinecraft[position]
        holder.render(item, fragmentManager)
    }

    override fun getItemCount() = itemsMinecraft.size

    override fun getSectionText(position: Int) = itemsMinecraft[position].getSectionIndex()

    fun updateItems(newItems: List<ItemMinecraft>) {
        val diffCallback = ItemDiffCallback(itemsMinecraft, newItems)
        val diffResult = DiffUtil.calculateDiff(diffCallback)
        fullItemList = newItems
        diffResult.dispatchUpdatesTo(this)
    }

    fun filter(query: String?) {
        val filteredList = if (query.isNullOrEmpty()) {
            fullItemList
        } else {
            fullItemList.filter {
                it.name.contains(
                    query,
                    ignoreCase = true
                )
            }
        }

        val diffCallback = ItemDiffCallback(itemsMinecraft, filteredList)
        val diffResult = DiffUtil.calculateDiff(diffCallback)
        itemsMinecraft = filteredList
        diffResult.dispatchUpdatesTo(this)
    }
}