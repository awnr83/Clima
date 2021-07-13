package com.example.clima.ciudades

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.clima.database.Ciudad
import com.example.clima.databinding.ListaCiudadesBinding

class CiudadAdapter(private val click:CiudadListener, private var eliminar: CiudadEliminar): ListAdapter<Ciudad,CiudadAdapter.ViewHolder>(CiudadCallback()) {
    class ViewHolder private constructor(private val binding: ListaCiudadesBinding): RecyclerView.ViewHolder(binding.root) {
        fun bind(ciudad: Ciudad, click: CiudadListener, eliminar: CiudadEliminar) {
            binding.ciudad=ciudad
            binding.click=click
            binding.delete=eliminar
            binding.executePendingBindings()
        }
        companion object{
            fun from(parent: ViewGroup):ViewHolder{
                val layoutInflater= LayoutInflater.from(parent.context)
                val binding= ListaCiudadesBinding.inflate(layoutInflater, parent, false)
                return ViewHolder(binding)
            }
        }
    }

    class CiudadEliminar(val clickEliminar: (ciudad: Ciudad)-> Unit) {
        fun onEliminar(ciudad: Ciudad)= clickEliminar(ciudad)
    }

    class CiudadCallback:DiffUtil.ItemCallback<Ciudad>() {
        override fun areItemsTheSame(oldItem: Ciudad, newItem: Ciudad): Boolean {
            return oldItem===newItem
        }
        override fun areContentsTheSame(oldItem: Ciudad, newItem: Ciudad): Boolean {
            return  oldItem.id==newItem.id
        }
    }

    class CiudadListener(val clickListener: (ciudadNombre: String)-> Unit) {
        fun onClick(ciudad: Ciudad)= clickListener(ciudad.name!!)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder  {
        return ViewHolder.from(parent)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(getItem(position), click, eliminar)
    }
}