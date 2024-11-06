package com.envigite.minecraftaplication.ui.view

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
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.envigite.minecraftaplication.R
import com.envigite.minecraftaplication.data.database.entities.CraftingEntity
import com.envigite.minecraftaplication.databinding.ActivityCraftingBinding
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
        observeViewModel()
    }

    private fun initUI() {
        loadCraftings()
        initAdapter()
        setupRecyclerView()
        initListeners()
        constraintSet()
        setupImageButtons()
    }

    private fun initListeners() {
        setUpCraftingButtons()
        setupSearchView()
        backToMainActivity()
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

        binding.saveButton.setOnClickListener {
            promptForCraftingName()
        }

        binding.loadButton.setOnClickListener {
            showLoadCraftingDialog()
        }

        binding.deleteButton.setOnClickListener {
            showDeleteCraftingDialog()
        }
    }

    private fun promptForCraftingName() {
        val input = EditText(this)
        val dialog = AlertDialog.Builder(this)
            .setTitle("Guardar crafteo")
            .setMessage("Introduce un nombre para el crafteo:")
            .setView(input)
            .setPositiveButton("Guardar") { _, _ ->
                val craftingName = input.text.toString()
                if (craftingName.isNotBlank()) {
                    saveCrafting(craftingName)
                } else {
                    Toast.makeText(this, "El nombre no puede estar vacío", Toast.LENGTH_SHORT)
                        .show()
                }
            }
            .setNegativeButton("Cancelar", null)
            .create()
        dialog.show()
    }

    private fun saveCrafting(name: String) {
        val images = imageButtons.map { button ->
            button.tag?.toString() ?: ""
        }

        val craftingEntity = CraftingEntity(
            name = name,
            slots = images
        )

        viewModel.saveCrafting(craftingEntity)
        Toast.makeText(this, "Crafteo guardado", Toast.LENGTH_SHORT).show()

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
                    .setTitle("Selecciona un crafteo a cargar")
                    .setItems(craftingNames.toTypedArray()) { _, which ->
                        loadCrafting(craftingList[which])
                    }
                    .setNegativeButton("Cancelar", null)
                    .show()
            } else {
                Toast.makeText(this, "No hay crafteos guardados", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun loadCrafting(crafting: CraftingEntity) {
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
                    .setTitle("Selecciona un crafteo a borrar")
                    .setItems(craftingNames.toTypedArray()) { _, which ->
                        val craftingToDelete = craftingList[which]
                        viewModel.deleteCrafting(craftingToDelete.id)
                        Toast.makeText(this, "Crafteo eliminado", Toast.LENGTH_SHORT).show()
                        imageButtons.forEach { button ->
                            button.setImageResource(0)
                            button.tag = null
                        }
                    }
                    .setNegativeButton("Cancelar", null)
                    .show()
            } else {
                Toast.makeText(this, "No hay crafteos guardados", Toast.LENGTH_SHORT).show()
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
                        "No se encontraron resultados",
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