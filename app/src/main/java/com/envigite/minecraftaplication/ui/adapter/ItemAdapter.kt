package com.envigite.minecraftaplication.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.fragment.app.FragmentManager
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.envigite.minecraftaplication.databinding.ItemMinecraftBinding
import com.envigite.minecraftaplication.databinding.ItemMinecraftCraftingBinding
import com.envigite.minecraftaplication.domain.model.ItemMinecraft
import com.l4digital.fastscroll.FastScroller

class ItemAdapter(
    private var itemsMinecraft: List<ItemMinecraft>,
    private val fragmentManager: FragmentManager,
    private val viewType: Int
) : RecyclerView.Adapter<RecyclerView.ViewHolder>(), FastScroller.SectionIndexer {

    private var fullItemList: List<ItemMinecraft> = itemsMinecraft
    private var onItemClickListener: ((String) -> Unit)? = null

    override fun getItemViewType(position: Int): Int {
        return viewType
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when (viewType) {
            VIEW_TYPE_1 -> {
                val binding =
                    ItemMinecraftBinding.inflate(LayoutInflater.from(parent.context), parent, false)
                ItemViewHolder(binding)
            }

            VIEW_TYPE_2 -> {
                val binding = ItemMinecraftCraftingBinding.inflate(
                    LayoutInflater.from(parent.context), parent, false)
                CraftingViewHolder(binding)
            }

            else -> throw IllegalArgumentException("Invalid view type")
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val item = itemsMinecraft[position]
        when (holder) {
            is ItemViewHolder -> holder.render(item, fragmentManager)
            is CraftingViewHolder -> holder.render(item) {
                onItemClickListener?.invoke(it.image)
            }
        }
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

    fun setOnClickListener(listener: (String) -> Unit) {
        onItemClickListener = listener
    }

    companion object {
        const val VIEW_TYPE_1 = 0
        const val VIEW_TYPE_2 = 1
    }
}