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
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
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

    //Configura la interfaz de usuario.
    private fun initUI() {
        initAdapter()
        initViewModel()
        setupSearchView()
    }

    private fun setupSearchView() {
        binding.svItems.setOnQueryTextFocusChangeListener { _, hasFocus ->
            if (hasFocus) {
                binding.rvMinecraft.visibility = View.VISIBLE // Muestra el RecyclerView cuando el SearchView tiene foco
                itemAdapter.filter(null) // Muestra toda la lista cuando no hay filtro
            }
        }

        binding.svItems.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                binding.svItems.clearFocus() // Cierra el teclado cuando se hace submit
                return true
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                if (newText.isNullOrEmpty()) {
                    itemAdapter.filter(null) // Muestra toda la lista si no hay texto en el SearchView
                } else {
                    itemAdapter.filter(newText) // Aplica el filtro al cambiar el texto
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

    //Inicializa el ViewModel y su observador.
    private fun initViewModel() {
        viewModel = ViewModelProvider(this).get(ItemViewModel::class.java)
        viewModel.itemsLiveData.observe(this) { items ->
            itemAdapter.updateItems(items)
        }

        viewModel.errorLiveData.observe(this) {
            Toast.makeText(this, getString(R.string.error_message), Toast.LENGTH_LONG).show()
        }

        val itemsPath = getString(R.string.minecraft_items_path)
        viewModel.fetchItems(itemsPath) //Llama al método fetchItems() en el ViewModel, que inicia el proceso de obtención de datos desde el repositorio.
    }

    //Inicializa el adapter del RecyclerView
    private fun initAdapter() {
        itemAdapter = ItemAdapter(emptyList()) //Crea una nueva instancia de ItemAdapter, inicializándola con una lista vacía.
        binding.rvMinecraft.adapter = itemAdapter //Asocia el ItemAdapter al RecyclerView. Esto permite que el RecyclerView muestre los elementos del adaptador.
        binding.rvMinecraft.layoutManager = LinearLayoutManager(this) //Configura el LayoutManager del RecyclerView. En este caso, se utiliza LinearLayoutManager, que organiza los elementos en una lista vertical.
    }
}