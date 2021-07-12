package com.example.clima.detalle

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.clima.database.CiudadDatabaseDao
import com.example.clima.network.CiudadApi
import com.example.clima.network.Weather
import kotlinx.coroutines.*
import java.lang.Exception

class DetalleViewModel(val nombre: String, val db: CiudadDatabaseDao): ViewModel(){

    private val jobViewModel= Job()
    private val uiScope= CoroutineScope(Dispatchers.Main + jobViewModel)
    override fun onCleared() {
        super.onCleared()
        jobViewModel.cancel()
    }

    private var error: Boolean = false

    private val _weather= MutableLiveData<Weather>()
    val weather: LiveData<Weather>
        get()= _weather

    private fun obtenerTiempo(){
        viewModelScope.launch {
            try {
                _weather.value = CiudadApi
                    .retrofitService
                    .getWeather(nombre, "710b9a74fd5943aff012c7e3e83be945")
                error= false
            }catch (e: Exception){
                error=true
                Log.i("alfredo","error: $nombre + ${e.message.toString()}")
            }
        }
    }

    init {
        obtenerTiempo()
    }

//
//    private fun onBuscar(){
//        uiScope.launch {
//            _ciudad.value= buscarDatosCiuadad()
//            obtenerTiempo()
//        }
//    }
//    private suspend fun buscarDatosCiuadad():Ciudad?{
//        return withContext(Dispatchers.IO) {
//            db.obtenerCiudad(nombre)
//        }
//    }
}