package com.example.clima.detalle

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

class DetalleViewModel(private val nombre: String, val db: CiudadDatabaseDao): ViewModel(){

    private val jobViewModel= Job()
    private val uiScope= CoroutineScope(Dispatchers.Main + jobViewModel)
    override fun onCleared() {
        super.onCleared()
        jobViewModel.cancel()
    }

    //maneja la visibilidad del aviso por si no hay internet
    private val _aviso= MutableLiveData<Int>()
    val aviso: LiveData<Int>
        get()=_aviso
    //mensajes de muestra los datos de DB o para eliminar una ciudad inexistente
    private val _notificacion= MutableLiveData<String>()
    val notificacion:LiveData<String>
        get()=_notificacion
    //navegar al listado
    private val _navigate= MutableLiveData<Boolean>()
    val navigate:LiveData<Boolean>
        get()=_navigate

    //muestra la informacion del a ciudad
    private val _weather= MutableLiveData<Ciudad>()
    val weather: LiveData<Ciudad>
        get()= _weather

    private var tiempoDB= Ciudad()
    private var tiempo= Weather()

    //Manejo del servidor del tiempo y DB
    private fun obtenerTiempo(){
        viewModelScope.launch {
            try {
                tiempo= CiudadApi
                    .retrofitService
                    .getWeather(nombre, "710b9a74fd5943aff012c7e3e83be945")
                _notificacion.value=""
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

            //En caso de no tener internet, se muestra desde la DB y si la ciudad no existe en el servidor se elimina
            }catch (e: Exception){
                //si no tiene internet carga desde la DB
                _notificacion.value="Sin conexion a Internet se muestran los datos cargados en el telefono."
                _aviso.value= View.VISIBLE
                onBuscarDatosCiudad()
                _weather.value= tiempoDB
                if(e.message=="HTTP 404 Not Found"){//como la ciudad no existe se procede a eliminar
                    _notificacion.value="Se elimino la ciudad:\"${_weather.value!!.name}\" porque NO existe en el servidor."
                    eliminarCiudad()
                }
            }
        }
    }

    init {
        obtenerTiempo()
        _aviso.value= View.GONE
        _notificacion.value=""
    }

//Se traen los datos de la ciudad
    private fun onBuscarDatosCiudad(){
        uiScope.launch {
             tiempoDB= buscarDatosCiuadad()
            obtenerTiempo()
        }
    }
    private suspend fun buscarDatosCiuadad(): Ciudad{
        return withContext(Dispatchers.IO) {
            db.obtenerCiudad(nombre)
        }
    }

//se actualizan los registros
    private fun onActualizar(){
        uiScope.launch {
            update()
        }
    }
    private suspend fun update(){
        withContext(Dispatchers.IO) {
            val sdf = SimpleDateFormat("dd/M/yy HH:mm")
            val currentDate = sdf.format(Date())
            val ciudad = Ciudad(
                id = tiempoDB.id,
                name = tiempoDB.name,
                temp = tiempo.main!!.temp,
                temp_min = tiempo.main!!.temp_min,
                temp_max = tiempo.main!!.temp_max,
                feels_like = tiempo.main!!.feels_like,
                pressure = tiempo.main!!.pressure,
                humidity = tiempo.main!!.humidity,
                ultimaActualizacion = currentDate.toString()
            )
            db.actualizarCiudad(ciudad)
        }
    }

//se Elimina ciudad inexistente
    fun eliminarCiudad(){
        uiScope.launch {
            delete()
            jobViewModel.cancel()
        }
    }
    private suspend fun delete(){
        withContext(Dispatchers.IO){
            val aux=tiempoDB
            db.eliminarCiudad(aux)
        }
    }
}