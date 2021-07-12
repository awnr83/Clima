package com.example.clima.detalle

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.clima.database.CiudadDatabaseDao
import com.example.clima.network.CiudadApi
import com.example.clima.network.Main
import kotlinx.coroutines.launch
import java.lang.Exception

class DetalleViewModel(val nombre: String, val db: CiudadDatabaseDao): ViewModel(){

    private val _ciudad= MutableLiveData<String>()
    val ciudad: LiveData<String>
        get()= _ciudad

    private val _tiempo= MutableLiveData<Main>()
    val tiempo: LiveData<Main>
        get()= _tiempo

    fun obtenerTiempo(){
        viewModelScope.launch {
            try {
                _tiempo.value = CiudadApi.retrofitService.getWeather(nombre, "710b9a74fd5943aff012c7e3e83be945").main
            }catch (e: Exception){
                Log.i("alfredo","error: ${e.message.toString()}")
            }
        }
    }

    init {
        obtenerTiempo()
        _ciudad.value=nombre
    }
}