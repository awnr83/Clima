package com.example.clima.ciudades

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.clima.database.CiudadDatabaseDao
import java.lang.IllegalArgumentException

class CiudadViewModelFactory(private val db: CiudadDatabaseDao):ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if(modelClass.isAssignableFrom(CiudadesViewModel::class.java)){
            return CiudadesViewModel(db) as T
        }
        throw IllegalArgumentException("Error al crear Ciudades ViewModel")
    }
}