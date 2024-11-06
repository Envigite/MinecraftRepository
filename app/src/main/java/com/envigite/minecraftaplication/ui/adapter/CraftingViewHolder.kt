package com.envigite.minecraftaplication.ui.adapter

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.envigite.minecraftaplication.R
import com.envigite.minecraftaplication.databinding.ItemMinecraftCraftingBinding
import com.envigite.minecraftaplication.domain.model.ItemMinecraft
import com.squareup.picasso.Picasso


class CraftingViewHolder(private val binding: ItemMinecraftCraftingBinding) :
    RecyclerView.ViewHolder(binding.root) {

    fun render(
        item: ItemMinecraft,
        onItemClickListener: ((ItemMinecraft) -> Unit)?
    ) {
        binding.tvName.text = item.name

        Picasso.get()
            .load(item.image)
            .resize(150, 150)
            .into(binding.ivCrafting, object : com.squareup.picasso.Callback {
                override fun onSuccess() {
                    binding.pbCrafting.visibility = View.GONE
                    binding.ivCrafting.visibility = View.VISIBLE
                }

                override fun onError(e: Exception?) {
                    binding.pbCrafting.visibility = View.GONE
                    binding.ivCrafting.setImageResource(R.drawable.error_image)
                }
            })

        binding.root.setOnClickListener {
            onItemClickListener?.invoke(item)
        }
    }
}
