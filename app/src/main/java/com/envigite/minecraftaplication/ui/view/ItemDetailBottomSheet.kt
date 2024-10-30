package com.envigite.minecraftaplication.ui.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.envigite.minecraftaplication.R
import com.envigite.minecraftaplication.databinding.BottomSheetItemDetailBinding
import com.envigite.minecraftaplication.domain.model.ItemMinecraft
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.squareup.picasso.Picasso

class ItemDetailBottomSheet(private val item: ItemMinecraft) : BottomSheetDialogFragment() {

    private var _binding: BottomSheetItemDetailBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = BottomSheetItemDetailBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setStyle(STYLE_NORMAL, R.style.TransparentBottomSheetDialog)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.tvItemDescription.text = item.description
        binding.tvStackSize.text = "Maximum stack: ${item.stackSize}"
        binding.tvrenewable.text =  "This item it's renewable?: ${if (item.renewable) "YES" else "NO"}"
        Picasso.get()
            .load(item.image)
            .into(binding.ivItemImage)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}