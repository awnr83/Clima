package com.example.clima.newCiudad


import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.clima.database.Ciudad
import com.example.clima.database.CiudadDatabaseDao
import com.example.clima.network.CiudadApi
import com.example.clima.network.Weather
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

    //se usa para dar avisos en pantalla
    private val _aviso= MutableLiveData<String>()
    val aviso: LiveData<String>
        get()=_aviso

    var editCiudad= MutableLiveData<String>()
    var nombreCiudad=String()
    private var tiempoDB= Ciudad()
    private var tiempo= Weather()

//comprobacion de que el nombre no esta cargado
    fun buscarCiudad(){
        if(!editCiudad.value.isNullOrBlank()) {
            nombreCiudad= editCiudad.value!!.toUpperCase()
            uiScope.launch {
                tiempoDB = buscarDatosCiudad()?:Ciudad()
                //se comprueba si ya fue registrada
                if(tiempoDB.name==null){
                    //se busca en el webService y se maneja la DB
                    obtenerCiudad()
                }else{
                    //si existe AVISA de que ya esta cargada
                    _aviso.value= "La ciudad: \"${nombreCiudad}\" ya se encuentra cargada."
                }
            }
        }else{
            //AVISO de que no ingreso nada
            _aviso.value= "Debe escribir el nombre de una ciudad..."
        }
    }
    private suspend fun buscarDatosCiudad():Ciudad{
        return withContext(Dispatchers.IO){
            db.obtenerCiudad(nombreCiudad)
        }
    }

//manejo de la DB - inserta la ciudad agregada
    private fun agregarCiudad(){
        if(!nombreCiudad.isNullOrEmpty()){
            uiScope.launch {
                insertar()
                editCiudad.value=""
            }
        }
    }
    private suspend fun insertar(){
        withContext(Dispatchers.IO){
            db.insertCiudad(tiempoDB)
        }
    }

//busca que retrofit devuelva un valor para la ciudad
    private fun obtenerCiudad(){
        viewModelScope.launch {
            try{
                val sdf = SimpleDateFormat("dd/M/yy HH:mm")
                val currentDate = sdf.format(Date())

                tiempo= CiudadApi.retrofitService.getWeather(nombreCiudad,"710b9a74fd5943aff012c7e3e83be945")
                tiempoDB= Ciudad(
                    name = nombreCiudad,
                    temp= (tiempo.main!!.temp!! - 273.15),
                    temp_min = (tiempo.main!!.temp_min!! - 273.15),
                    temp_max = (tiempo.main!!.temp_max!! - 273.15),
                    feels_like = (tiempo.main!!.feels_like!! - 273.15),
                    pressure = tiempo.main!!.pressure,
                    humidity = tiempo.main!!.humidity,
                    ultimaActualizacion = currentDate.toString()
                )
                agregarCiudad()
                _aviso.value="La ciudad \"${nombreCiudad}\" fue agregada con exito."
            }catch (e: Exception){
                if(e.message!="HTTP 404 Not Found") { // situacion sin internet
                    //permite guardar la ciudad porque no tiene internet
                    tiempoDB = Ciudad(name = nombreCiudad)
                    agregarCiudad()

                    //aviso de que guardo la ciudad sin comprobar si existe
                    _aviso.value="NO tiene conexión con internet. Se guardo la ciudad: \"${nombreCiudad}\", sin poder verificar su existencia."
                    editCiudad.value=""
                }else{
                    _aviso.value="La ciudad \"${nombreCiudad}\" NO existe, verifique el nombre por favor."
                }
            }
        }
    }
}