package com.example.clima.detalle

import android.util.Log
import android.view.View
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.clima.database.Ciudad
import com.example.clima.database.CiudadDatabaseDao
import com.example.clima.network.CiudadApi
import com.example.clima.network.Weather
import kotlinx.coroutines.*
import java.lang.Exception
import java.text.SimpleDateFormat
import java.util.*

class DetalleViewModel(val nombre: String, val db: CiudadDatabaseDao): ViewModel(){

    private val jobViewModel= Job()
    private val uiScope= CoroutineScope(Dispatchers.Main + jobViewModel)
    override fun onCleared() {
        super.onCleared()
        jobViewModel.cancel()
    }

    private var error: Boolean = false

    //maneja la visibilidad del aviso por si no hay internet
    private val _aviso= MutableLiveData<Int>()
    val aviso: LiveData<Int>
        get()=_aviso

    //muestra la informacion del a ciudad
    private val _weather= MutableLiveData<Ciudad>()
    val weather: LiveData<Ciudad>
        get()= _weather

    private var tiempoDB= Ciudad()
    private var tiempo= Weather()

    private fun obtenerTiempo(){
        viewModelScope.launch {
            try {
                tiempo= CiudadApi
                    .retrofitService
                    .getWeather(nombre, "710b9a74fd5943aff012c7e3e83be945")
//conversiones
                tiempo.main!!.temp= (tiempo.main!!.temp!! - 273.15)
                tiempo.main!!.feels_like= (tiempo.main!!.feels_like!! - 273.15)
                tiempo.main!!.temp_min= (tiempo.main!!.temp_min!! - 273.15)
                tiempo.main!!.temp_max= (tiempo.main!!.temp_max!! - 273.15)
//actualizar la db
                onBuscarDatosCiudad()
                onActualizar()
//se muestra el tiempo desde retrofit
                _weather.value= tiempoDB
                _aviso.value= View.GONE
//se muestra desde la DB
            }catch (e: Exception){
                //si no tiene internet carga desde la DB
                _aviso.value= View.VISIBLE
                onBuscarDatosCiudad()
                _weather.value= tiempoDB
                Log.i("alfredo","error: $nombre + ${e.message.toString()}")
            }
        }
    }

    init {
        obtenerTiempo()
        _aviso.value= View.GONE

        //_weather.value=tiempo
    }

//Se traen los datos de la ciudad
    private fun onBuscarDatosCiudad(){
        uiScope.launch {
             tiempoDB= buscarDatosCiuadad()!!
            obtenerTiempo()
        }
    }
    private suspend fun buscarDatosCiuadad(): Ciudad{
        return withContext(Dispatchers.IO) {
            db.obtenerCiudad(nombre)
        }
    }

//se actualizan los registros
    fun onActualizar(){
        uiScope.launch {
            update()
        }
    }
    private suspend fun update(){
        withContext(Dispatchers.IO) {
            val sdf = SimpleDateFormat("dd/M/yyyy hh:mm:ss")
            val currentDate = sdf.format(Date())
            var ciudad = Ciudad(
                id= tiempoDB.id,
                name = tiempoDB.name,
                temp = tiempo.main!!.temp,
                temp_min = tiempo.main!!.temp_min,
                temp_max = tiempo.main!!.temp_max,
                feels_like = tiempo.main!!.feels_like,
                pressure = tiempo.main!!.pressure,
                humidity = tiempo.main!!.humidity,
                ultimaActualizacion = System.currentTimeMillis()
            )
            db.actualizarCiudad(ciudad)
        }
    }
}