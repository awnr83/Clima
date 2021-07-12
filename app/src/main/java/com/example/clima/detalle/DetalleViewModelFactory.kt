package com.example.clima.detalle

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.clima.database.CiudadDatabaseDao

class DetalleViewModelFactory(private val nombre:String, private val db:CiudadDatabaseDao):ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if(modelClass.isAssignableFrom(DetalleViewModel::class.java))
            return DetalleViewModel(nombre, db) as T
        throw IllegalArgumentException("No se pudo crear el Detalle ViewModel")
    }
}