package com.envigite.minecraftaplication.ui.view

import android.content.Intent
import android.graphics.drawable.AnimationDrawable
import android.os.Bundle
import android.transition.TransitionManager
import android.view.View
import android.widget.SearchView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintSet
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.isEmpty
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import com.envigite.minecraftaplication.R
import com.envigite.minecraftaplication.databinding.ActivityMainBinding
import com.envigite.minecraftaplication.ui.adapter.ItemAdapter
import com.envigite.minecraftaplication.ui.viewmodel.ItemViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private val viewModel: ItemViewModel by viewModels()
    private lateinit var itemAdapter: ItemAdapter

    private val constraintSetStart = ConstraintSet()
    private val constraintSetEnd = ConstraintSet()

    override fun onCreate(savedInstanceState: Bundle?) {

        val screenSplash = installSplashScreen()

        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        Thread.sleep(500)
        screenSplash.setKeepOnScreenCondition { false }

        initUI()
    }

    private fun initUI() {
        initAdapter()
        initViewModel()
        initListeners()
        backgroundActivity()
    }

    private fun initListeners() {
        setupBack()
        setupSearchView()
        constraintSet()
        clearFocus()
        navigationCrafting()
    }

    private fun navigationCrafting() {
        binding.btnNavigate.setOnClickListener {
            val intent = Intent(this, CraftingActivity::class.java)
            startActivity(intent)
        }
    }

    private fun clearFocus() {
        binding.svItems.clearFocus()
    }

    private fun constraintSet() {
        constraintSetStart.clone(binding.main)
        constraintSetEnd.clone(binding.main)
        constraintSetEnd.connect(R.id.svItems, ConstraintSet.TOP, R.id.toolbar, ConstraintSet.BOTTOM, 16)
    }

    private fun setupBack() {
        binding.ivTitleMinecraft.setOnClickListener {
            onBackPressed()
        }
    }

    private fun setupSearchView() {
        binding.svItems.setOnQueryTextFocusChangeListener { _, hasFocus ->
            if (hasFocus) {
                TransitionManager.beginDelayedTransition(binding.main)
                constraintSetEnd.applyTo(binding.main)
                binding.rvMinecraft.visibility = View.VISIBLE


                binding.progressBar.visibility = View.VISIBLE
                viewModel.fetchItems(getString(R.string.minecraft_items_path))
            }
        }

        binding.svItems.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                if (itemAdapter.itemCount == 0) {
                    Toast.makeText(this@MainActivity, "No se encontraron resultados", Toast.LENGTH_SHORT).show()
                    clearFocus()
                } else {
                    clearFocus()
                }
                return true
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                TransitionManager.beginDelayedTransition(binding.main)
                if (newText.isNullOrEmpty()) {
                    itemAdapter.filter(null)
                    binding.rvMinecraft.scrollToPosition(0)
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
            clearFocus()
            TransitionManager.beginDelayedTransition(binding.main)
            constraintSetStart.applyTo(binding.main)
            binding.rvMinecraft.visibility = View.GONE
            binding.rvMinecraft.scrollToPosition(0)
        }
    }

    private fun initViewModel() {
        viewModel.itemsLiveData.observe(this) { items ->
            itemAdapter.updateItems(items)
            binding.progressBar.visibility = View.GONE
            itemAdapter.filter(null)
        }

        viewModel.errorLiveData.observe(this) {errorMessage ->
            errorMessage?.let {
                Toast.makeText(this, it, Toast.LENGTH_LONG).show()
            }
        }
    }

    private fun initAdapter() {
        itemAdapter = ItemAdapter(emptyList(), supportFragmentManager, ItemAdapter.VIEW_TYPE_1)
        binding.rvMinecraft.adapter = itemAdapter
        binding.rvMinecraft.layoutManager = GridLayoutManager(this, 2)
    }

    private fun backgroundActivity() {
        val background = binding.main.background as AnimationDrawable
        background.setEnterFadeDuration(2500)
        background.setExitFadeDuration(2500)
        background.start()
    }
}