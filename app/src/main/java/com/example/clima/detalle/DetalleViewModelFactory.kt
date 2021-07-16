package com.climaApp.clima.detalle

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.climaApp.clima.database.Ciudad
import com.climaApp.clima.database.CiudadDatabaseDao

class DetalleViewModelFactory(private val ciudad: Ciudad, private val db:CiudadDatabaseDao):ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if(modelClass.isAssignableFrom(DetalleViewModel::class.java))
            return DetalleViewModel(ciudad, db) as T
        throw IllegalArgumentException("No se pudo crear el Detalle ViewModel")
    }
}