package com.envigite.minecraftaplication.ui.adapter

import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.envigite.minecraftaplication.R
import com.envigite.minecraftaplication.domain.model.ItemMinecraft
import com.squareup.picasso.Picasso

class ItemViewHolder(view: View) :RecyclerView.ViewHolder(view) { //Recibe una vista, (un objeto del layout inflado) como parámetro para poder acceder a sus elementos.

    private val tvName: TextView = view.findViewById(R.id.tvName) //Se inicializan las variables para cada elemento de la interfaz de usuario.
    private val tvDescription: TextView = view.findViewById(R.id.tvDescription)
    private val tvStackSize: TextView = view.findViewById(R.id.tvStackSize)
    private val tvRenewable: TextView = view.findViewById(R.id.tvRenewable)
    private val ivImage: ImageView = view.findViewById(R.id.ivImage)

    fun render(item: ItemMinecraft) { //Este método se encarga de actualizar la vista con la información del objeto ItemMinecraft que se le pasa como parámetro.
        tvName.text = item.name
        tvDescription.text = item.description
        tvStackSize.text = item.stackSize.toString() //Convierte el tamaño de la pila a String para poder asignarlo a un TextView.
        tvRenewable.text = if(item.renewable)"Renewable" else "Non-renewable"

        Picasso.get()
            .load(item.image)
            .into(ivImage)
    }
}