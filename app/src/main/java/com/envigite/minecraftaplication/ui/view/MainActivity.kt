package com.envigite.minecraftaplication.ui.view

import android.os.Bundle
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.envigite.minecraftaplication.R
import com.envigite.minecraftaplication.databinding.ActivityMainBinding
import com.envigite.minecraftaplication.ui.viewmodel.ItemViewModel
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

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
    }

    //Inicializa el ViewModel y su observador.
    private fun initViewModel() {
        viewModel = ViewModelProvider(this).get(ItemViewModel::class.java) //Esto asegura que el ViewModel esté vinculado al ciclo de vida de la MainActivity.
        viewModel.itemsLiveData.observe(this){items -> //Observa los cambios en itemsLiveData dentro del ViewModel.
            itemAdapter.updateItems(items) //Llama al método updateItems del adaptador para actualizar la lista de elementos mostrados en la interfaz de usuario.
        }

        viewModel.errorLiveData.observe(this) {
            val err = getString(R.string.error_message)
            Toast.makeText(this, err, Toast.LENGTH_LONG).show()
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