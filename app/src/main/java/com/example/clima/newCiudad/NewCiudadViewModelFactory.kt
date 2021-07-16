package com.climaApp.clima.newCiudad

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.climaApp.clima.database.CiudadDatabase
import com.climaApp.clima.database.CiudadDatabaseDao

class NewCiudadViewModelFactory(private val db: CiudadDatabaseDao):ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if(modelClass.isAssignableFrom(NewCiudadViewModel::class.java))
            return NewCiudadViewModel(db) as T
        throw IllegalArgumentException("Error no se pudo crear el NewViewModel")
    }
}