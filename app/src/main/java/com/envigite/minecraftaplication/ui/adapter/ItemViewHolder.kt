package com.envigite.minecraftaplication.ui.adapter

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.envigite.minecraftaplication.R
import com.envigite.minecraftaplication.databinding.ItemMinecraftBinding
import com.envigite.minecraftaplication.domain.model.ItemMinecraft
import com.squareup.picasso.Picasso

class ItemViewHolder(private val binding: ItemMinecraftBinding) :
    RecyclerView.ViewHolder(binding.root) {

    fun render(item: ItemMinecraft) {
        binding.tvName.text = item.name

        Picasso.get()
            .load(item.image)
            .into(binding.ivImage, object : com.squareup.picasso.Callback {
                override fun onSuccess() {
                    binding.pbItemMinecraft.visibility = View.GONE
                    binding.ivImage.visibility = View.VISIBLE
                }

                override fun onError(e: Exception?) {
                    binding.pbItemMinecraft.visibility = View.GONE
                    binding.ivImage.setImageResource(R.drawable.error_image)
                }
            })
    }
}