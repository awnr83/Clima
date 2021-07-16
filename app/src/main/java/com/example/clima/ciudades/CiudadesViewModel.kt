package com.climaApp.clima.ciudades

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.climaApp.clima.database.Ciudad
import com.climaApp.clima.database.CiudadDatabaseDao
import com.climaApp.clima.repository.RepositoryCiudad
import kotlinx.coroutines.*


class CiudadesViewModel(private val db: CiudadDatabaseDao): ViewModel() {

    private val jobViewModel= Job()
    private val uiScope= CoroutineScope(Dispatchers.Main + jobViewModel)
    override fun onCleared() {
        super.onCleared()
        jobViewModel.cancel()
    }

    val repositorio = RepositoryCiudad(db)
    val allCiudades = repositorio.ciudades

    val _cant = repositorio.obtnenerCantidaCiudades()

    private val _title= MutableLiveData<String>()
    val title: LiveData<String>
        get()=_title

    //actualizacion
    val actualizo= MutableLiveData<Boolean>(false)
    private fun onActuralizo(){
        actualizo.value=true
    }
    fun actualizoC(){
        actualizo.value=false
    }

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

    //activacion -> mensaje eliminado
    private val _eliminado= MutableLiveData<Boolean>()
    val eliminado: LiveData<Boolean>
        get()= _eliminado
    private fun onEliminado(){
        _eliminado.value=true
    }
    fun eliminadoC(){
        _eliminado.value=false
    }

    init {

        _title.value="Cargando ciudades..."

        viewModelScope.launch {
            repositorio.actualizarTemperatura()
            onActuralizo()
        }

        _eliminado.value=false
        _navigate.value= false
    }

    //Button permite actualizar las temperaturas desde DB
    fun actualizar(){
        Log.i("alfredo", _cant.value.toString())
        viewModelScope.launch {
            repositorio.actualizarTemperatura()
            onActuralizo()
        }
    }

    //Button permite eliminar una ciudad
    fun eliminarCiudad(ciudad: Ciudad){
        uiScope.launch {
           repositorio.eliminarCiudad(ciudad)
           _eliminado.value=true
        }
    }
}