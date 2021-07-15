package com.example.clima.ciudades

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.clima.database.Ciudad
import com.example.clima.database.CiudadDatabaseDao
import com.example.clima.repository.RepositoryCiudad
import kotlinx.coroutines.*


class CiudadesViewModel(private val db: CiudadDatabaseDao): ViewModel() {

    private val jobViewModel= Job()
    private val uiScope= CoroutineScope(Dispatchers.Main + jobViewModel)
    override fun onCleared() {
        super.onCleared()
        jobViewModel.cancel()
    }

    val repositorio=RepositoryCiudad(db)
    var allCiudades =repositorio.ciudades

    private val _cantCiudades= MutableLiveData<String>()
    val cantCiudades:LiveData<String>
        get()=_cantCiudades

    //boton agregar -> navega
    private val _navigate= MutableLiveData<Boolean>()
    val navigate:LiveData<Boolean>
        get()=_navigate
    fun onClickNavigate(){
        _navigate.value=true
    }
    fun onNavigate(){
        _navigate.value=false
    }

    //activacion -> mensaje
    private val _aviso=MutableLiveData<String>()
    val aviso: LiveData<String>
        get()= _aviso
    private val _notificacion= MutableLiveData<Boolean>()
    val notificacion: LiveData<Boolean>
        get()= _notificacion
    private fun onNotificacion(){
        _notificacion.value=true
    }
    fun notificacionC(){
        _notificacion.value=false
    }


    fun actualizar(){
        viewModelScope.launch {
            //se muestran las temperaturas desde DB
            repositorio.actualizarTemperatura()
        }
    }

    init {
        viewModelScope.launch {
            repositorio.actualizarTemperatura()
        }

        _notificacion.value=false
        _navigate.value= false
        _cantCiudades.value="Ciudades cargadas: "
    }

    fun eliminarCiudad(ciudad: Ciudad){
        uiScope.launch {
           repositorio.eliminarCiudad(ciudad)
           _notificacion.value=true
        }
    }
}