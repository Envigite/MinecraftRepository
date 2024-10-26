package com.envigite.minecraftaplication.ui.view

import android.os.Bundle
import android.view.View
import android.widget.SearchView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.envigite.minecraftaplication.R
import com.envigite.minecraftaplication.databinding.ActivityMainBinding
import com.envigite.minecraftaplication.ui.adapter.ItemAdapter
import com.envigite.minecraftaplication.ui.viewmodel.ItemViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private lateinit var viewModel: ItemViewModel
    private lateinit var binding: ActivityMainBinding
    private lateinit var itemAdapter: ItemAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        initUI()
    }

    private fun initUI() {
        initAdapter()
        initViewModel()
        initListeners()
    }

    private fun initListeners() {
        setupBack()
        setupSearchView()
    }

    private fun setupBack() {
        binding.ivTitleMinecraft.setOnClickListener {
            onBackPressed()
        }
    }

    private fun setupSearchView() {
        binding.svItems.setOnQueryTextFocusChangeListener { _, hasFocus ->
            if (hasFocus) {
                binding.rvMinecraft.visibility = View.VISIBLE
                itemAdapter.filter(null)
            }
        }

        binding.svItems.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                binding.svItems.clearFocus()
                return true
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                if (newText.isNullOrEmpty()) {
                    itemAdapter.filter(null)
                } else {
                    itemAdapter.filter(newText)
                }
                return true
            }
        })
    }

    override fun onBackPressed() {
        if (binding.svItems.isIconified) {
            super.onBackPressedDispatcher.onBackPressed()
        } else {
            binding.svItems.isIconified = true
            binding.svItems.clearFocus()
            binding.rvMinecraft.visibility = View.GONE
        }
    }

    private fun initViewModel() {
        viewModel = ViewModelProvider(this)[ItemViewModel::class.java]
        viewModel.itemsLiveData.observe(this) { items ->
            itemAdapter.updateItems(items)
        }

        viewModel.errorLiveData.observe(this) {
            Toast.makeText(this, getString(R.string.error_message), Toast.LENGTH_LONG).show()
        }

        val itemsPath = getString(R.string.minecraft_items_path)
        viewModel.fetchItems(itemsPath)
    }

    private fun initAdapter() {
        itemAdapter = ItemAdapter(emptyList())
        binding.rvMinecraft.adapter = itemAdapter
        binding.rvMinecraft.layoutManager = GridLayoutManager(this, 2)
    }
}