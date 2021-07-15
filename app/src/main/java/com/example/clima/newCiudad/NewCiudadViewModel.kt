package com.example.clima.newCiudad


import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.clima.database.Ciudad
import com.example.clima.database.CiudadDatabaseDao
import com.example.clima.network.CiudadApi
import com.example.clima.network.Weather
import com.example.clima.repository.RepositoryCiudad
import kotlinx.coroutines.*
import java.text.SimpleDateFormat
import java.util.*

class NewCiudadViewModel(private val db: CiudadDatabaseDao): ViewModel() {

    private val jobViewModel= Job()
    private val uiScope= CoroutineScope(Dispatchers.Main + jobViewModel)
    override fun onCleared() {
        super.onCleared()
        jobViewModel.cancel()
    }

    val repositorio= RepositoryCiudad(db)

    //se usa para dar avisos en pantalla
    private val _aviso= MutableLiveData<String>()
    val aviso: LiveData<String>
        get()=_aviso
    //notificaciones igual a los avisos
    private val _notificacion=MutableLiveData<Boolean>()
    val notification: LiveData<Boolean>
        get()= _notificacion
    private fun onNotificacion(){
        _notificacion.value=true
    }
    fun notificacionC(){
        _notificacion.value=false
    }

    var editCiudad= MutableLiveData<String>()

//comprobacion de que el nombre no esta cargado
    fun buscarCiudad() {
        if(editCiudad.value!="") {
            var nombre = editCiudad.value!!.toUpperCase()
            var ciudad = Ciudad()
            viewModelScope.launch {
                ciudad = repositorio.buscarCiudadDb(nombre)
                if (ciudad == null) { //no existe
                    val ciudad = repositorio.buscarCiudadWeather(nombre)
                    if (ciudad.name != null) {
                        repositorio.agregarCiudad(ciudad)
                        editCiudad.value = ""
                        onNotificacion()
                        _aviso.value = "La ciudad fue agregada existosamente."
                    } else {
                        onNotificacion()
                        _aviso.value = "Verifique el nombre ingresado y su conexcion con internet."
                    }
                } else {
                    onNotificacion()
                    _aviso.value = "La ciudad ya se encuentra cargada."
                }
            }
        }else {
            onNotificacion()
            _aviso.value = "Debe ingresar el nombre de una ciudad para agregar al listado."
        }
    }

    private fun existeCiudadW(nombre: String):Ciudad {
        var ciudad = Ciudad()
        viewModelScope.launch {
            ciudad= repositorio.buscarCiudadWeather(nombre)?: return@launch
        }
        return ciudad
    }

     init {
         _notificacion.value=false
     }
}