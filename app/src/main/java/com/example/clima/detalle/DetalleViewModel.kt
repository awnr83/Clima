package com.climaApp.clima.detalle

import android.util.Log
import android.view.View
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.climaApp.clima.database.Ciudad
import com.climaApp.clima.database.CiudadDatabaseDao
import com.climaApp.clima.repository.RepositoryCiudad
import kotlinx.coroutines.*

class DetalleViewModel(private val ciudad: Ciudad, val db: CiudadDatabaseDao): ViewModel(){

    private val jobViewModel= Job()
    private val uiScope= CoroutineScope(Dispatchers.Main + jobViewModel)
    override fun onCleared() {
        super.onCleared()
        jobViewModel.cancel()
    }

    private val repository= RepositoryCiudad(db)
    val weather=repository.obtenerCiudadLD(ciudad.name!!)

    //mensajes de muestra los datos de DB o para eliminar una ciudad inexistente
    private val _notificacion= MutableLiveData<Boolean>()
    val notificacion:LiveData<Boolean>
        get()=_notificacion

    fun actualizadoC(){
        _notificacion.value=false
    }

    fun actualizar(){
        viewModelScope.launch {
            repository.actualizarTemperaturaCiudad(ciudad)
            _notificacion.value=true
        }
    }

    init {
        viewModelScope.launch {
            repository.actualizarTemperaturaCiudad(ciudad)
        }
        _notificacion.value=false
    }
}