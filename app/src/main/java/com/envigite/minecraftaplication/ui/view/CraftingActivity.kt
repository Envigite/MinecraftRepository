package com.envigite.minecraftaplication.ui.view

import android.content.pm.ActivityInfo
import android.os.Bundle
import android.transition.TransitionManager
import android.view.View
import android.widget.EditText
import android.widget.ImageButton
import android.widget.SearchView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintSet
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.envigite.minecraftaplication.R
import com.envigite.minecraftaplication.databinding.ActivityCraftingBinding
import com.envigite.minecraftaplication.domain.model.CraftingEntityDomain
import com.envigite.minecraftaplication.extensions.observeOnce
import com.envigite.minecraftaplication.ui.adapter.ItemAdapter
import com.envigite.minecraftaplication.ui.viewmodel.ItemViewModel
import com.squareup.picasso.Picasso
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class CraftingActivity : AppCompatActivity() {

    private lateinit var binding: ActivityCraftingBinding
    private val viewModel: ItemViewModel by viewModels()
    private lateinit var itemAdapter: ItemAdapter
    private lateinit var imageButtons: List<ImageButton>

    private var selectedButton: ImageButton? = null
    private val constraintSetStart = ConstraintSet()
    private val constraintSetEnd = ConstraintSet()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityCraftingBinding.inflate(layoutInflater)
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        initUI()
    }

    private fun initUI() {
        temporaryScreenOrientation()
        observeViewModel()
        initListeners()
        loadCraftings()
        initAdapter()
        setupRecyclerView()
        constraintSet()
        setupImageButtons()
    }

    private fun initListeners() {
        setUpCraftingButtons()
        setupSearchView()
        backToMainActivity()
    }

    private fun temporaryScreenOrientation() {
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT)
    }

    private fun loadCraftings() {
        viewModel.loadCraftingsFromDb()
    }

    private fun setupImageButtons() {
        imageButtons = listOf(
            binding.imageButton1, binding.imageButton2, binding.imageButton3,
            binding.imageButton4, binding.imageButton5, binding.imageButton6,
            binding.imageButton7, binding.imageButton8, binding.imageButton9
        )

        imageButtons.forEach { button ->
            button.setOnLongClickListener {
                button.setImageResource(0)
                button.tag = null
                true
            }
        }

        binding.saveButton.setOnClickListener { promptForCraftingName() }
        binding.loadButton.setOnClickListener { showLoadCraftingDialog() }
        binding.deleteButton.setOnClickListener { showDeleteCraftingDialog() }
    }

    private fun promptForCraftingName() {
        val input = EditText(this)
        AlertDialog.Builder(this)
            .setTitle("Save crafting")
            .setMessage("Enter a name for the craft:")
            .setView(input)
            .setPositiveButton("Save") { _, _ ->
                val craftingName = input.text.toString()
                if (craftingName.isNotBlank()) {
                    val images = imageButtons.map { it.tag?.toString() ?: "" }
                    viewModel.saveCrafting(CraftingEntityDomain(name = craftingName, slots = images))
                    Toast.makeText(this, "Crafting Saved", Toast.LENGTH_SHORT).show()
                    resetImageButtons()
                } else {
                    Toast.makeText(this, "Name cannot be empty", Toast.LENGTH_SHORT).show()
                }
            }
            .setNegativeButton("Cancel", null)
            .create()
            .show()
    }

    private fun resetImageButtons() {
        imageButtons.forEach { button ->
            button.setImageResource(0)
            button.tag = null
        }
    }

    private fun showLoadCraftingDialog() {
        viewModel.getCraftingList().observeOnce(this) { craftingList ->
            if (craftingList.isNotEmpty()) {
                val craftingNames = craftingList.map { it.name }
                AlertDialog.Builder(this)
                    .setTitle("Select a craft to load")
                    .setItems(craftingNames.toTypedArray()) { _, which ->
                        loadCrafting(craftingList[which])
                    }
                    .setNegativeButton("Cancel", null)
                    .show()
            } else {
                Toast.makeText(this, "There are no saved crafts", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun loadCrafting(crafting: CraftingEntityDomain) {
        crafting.slots.forEachIndexed { index, imageUrl ->
            if (imageUrl.isNotEmpty()) {
                Picasso.get()
                    .load(imageUrl)
                    .resize(150, 150)
                    .into(imageButtons[index])
            } else {
                imageButtons[index].setImageResource(0)
            }
        }
    }

    private fun showDeleteCraftingDialog() {
        viewModel.getCraftingList().observeOnce(this) { craftingList ->
            if (craftingList.isNotEmpty()) {
                val craftingNames = craftingList.map { it.name }
                AlertDialog.Builder(this)
                    .setTitle("Select a craft to delete")
                    .setItems(craftingNames.toTypedArray()) { _, which ->
                        val craftingToDelete = craftingList[which]
                        viewModel.deleteCrafting(craftingToDelete.id)
                        Toast.makeText(this, "Crafting removed", Toast.LENGTH_SHORT).show()
                        imageButtons.forEach { button ->
                            button.setImageResource(0)
                            button.tag = null
                        }
                    }
                    .setNegativeButton("Cancel", null)
                    .show()
            } else {
                Toast.makeText(this, "There are no saved crafts", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun backToMainActivity() {
        binding.toolbar.setOnClickListener {
            finish()
        }
    }

    private fun constraintSet() {
        constraintSetStart.clone(binding.main)
        constraintSetEnd.clone(binding.main)

        constraintSetEnd.connect(
            R.id.craftingTable,
            ConstraintSet.TOP,
            R.id.buttons,
            ConstraintSet.BOTTOM,
            16
        )
        constraintSetEnd.clear(R.id.craftingTable, ConstraintSet.BOTTOM)
        constraintSetEnd.setVisibility(R.id.svCraftingTable, View.VISIBLE)
        constraintSetEnd.setVisibility(R.id.rvItems, View.VISIBLE)
    }

    private fun setupSearchView() {
        binding.svCraftingTable.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                if (itemAdapter.itemCount == 0) {
                    Toast.makeText(
                        this@CraftingActivity,
                        "No results found",
                        Toast.LENGTH_SHORT
                    ).show()
                    binding.svCraftingTable.clearFocus()
                } else {
                    binding.svCraftingTable.clearFocus()
                }
                return false
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                itemAdapter.filter(newText)
                return false
            }
        })
    }

    private fun setUpCraftingButtons() {
        val buttons = listOf(
            binding.imageButton1, binding.imageButton2, binding.imageButton3,
            binding.imageButton4, binding.imageButton5, binding.imageButton6,
            binding.imageButton7, binding.imageButton8, binding.imageButton9
        )

        buttons.forEach { button ->
            button.setOnClickListener {
                selectedButton = button
                binding.rvItems.visibility = View.VISIBLE
                binding.svCraftingTable.visibility = View.VISIBLE
                TransitionManager.beginDelayedTransition(binding.main)
                constraintSetEnd.applyTo(binding.main)
            }
        }
    }

    private fun observeViewModel() {
        viewModel.itemsLiveData.observe(this) { items ->
            itemAdapter.updateItems(items)
            itemAdapter.filter(null)
        }

        viewModel.errorLiveData.observe(this) { error ->
            Toast.makeText(this, error, Toast.LENGTH_SHORT).show()
        }

        viewModel.fetchItems(getString(R.string.minecraft_items_path))
    }

    private fun setupRecyclerView() {
        itemAdapter.setOnClickListener { selectedImageUrl ->
            selectedButton?.let { button ->
                button.tag = selectedImageUrl
                Picasso.get()
                    .load(selectedImageUrl)
                    .resize(150, 150)
                    .into(button)
            }
            binding.rvItems.visibility = View.VISIBLE
            binding.svCraftingTable.visibility = View.VISIBLE
            TransitionManager.beginDelayedTransition(binding.main)
            constraintSetStart.applyTo(binding.main)
        }
    }

    override fun onBackPressed() {
        if (binding.svCraftingTable.isIconified) {
            super.onBackPressedDispatcher.onBackPressed()
        } else {
            binding.svCraftingTable.isIconified = true
            binding.svCraftingTable.clearFocus()
            TransitionManager.beginDelayedTransition(binding.main)
            constraintSetStart.applyTo(binding.main)
            binding.svCraftingTable.visibility = View.GONE
            binding.rvItems.visibility = View.GONE
        }
    }

    private fun initAdapter() {
        itemAdapter =
            ItemAdapter(emptyList(), supportFragmentManager, ItemAdapter.VIEW_TYPE_2)
        binding.rvItems.apply {
            adapter = itemAdapter
            layoutManager = LinearLayoutManager(this@CraftingActivity)
        }
    }
}