package com.example.clima.newCiudad

import android.util.Log
import androidx.compose.ui.text.toLowerCase
import androidx.compose.ui.text.toUpperCase
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
    private var tiempoDB= Ciudad()
    private var tiempo= Weather()

//comprobacion de que el nombre no esta cargado
    fun buscarCiudad(){
        if(!editCiudad.value.isNullOrBlank()) {
            editCiudad.value= editCiudad.value!!.toUpperCase()
            uiScope.launch {
                tiempoDB = buscarDatosCiudad()?:Ciudad()
                //se comprueba si ya fue registrada
                if(tiempoDB.name==null){
                    //se busca en el webService y se maneja la DB
                    obtenerCiudad()
                }else{
                    //si existe AVISA de que ya esta cargada
                    _aviso.value= "La ciudad: \"${editCiudad.value}\" ya se encuentra cargada."
                }
            }
        }else{
            //AVISO de que no ingreso nada
            _aviso.value= "Debe escribir el nombre de una ciudad..."
        }
    }
    private suspend fun buscarDatosCiudad():Ciudad{
        return withContext(Dispatchers.IO){
            db.obtenerCiudad(editCiudad.value.toString())
        }
    }

//manejo de la DB - inserta la ciudad agregada
    private fun agregarCiudad(){
        if(!editCiudad.value.isNullOrEmpty()){
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

                tiempo= CiudadApi.retrofitService.getWeather(editCiudad.value,"710b9a74fd5943aff012c7e3e83be945")
                tiempoDB= Ciudad(
                    name = editCiudad.value,
                    temp= (tiempo.main!!.temp!! - 273.15),
                    temp_min = (tiempo.main!!.temp_min!! - 273.15),
                    temp_max = (tiempo.main!!.temp_max!! - 273.15),
                    feels_like = (tiempo.main!!.feels_like!! - 273.15),
                    pressure = tiempo.main!!.pressure,
                    humidity = tiempo.main!!.humidity,
                    ultimaActualizacion = currentDate.toString()
                )
                agregarCiudad()
                _aviso.value="La ciudad \"${editCiudad.value}\" fue agregada con exito."
            }catch (e: Exception){
                if(e.message!="HTTP 404 Not Found") { // situacion sin internet
                    //permite guardar la ciudad porque no tiene internet
                    tiempoDB = Ciudad(name = editCiudad.value)
                    agregarCiudad()

                    //aviso de que guardo la ciudad sin comprobar si existe
                    _aviso.value="NO tiene conexión con internet. Se guardo la ciudad: \"${editCiudad.value}\", sin poder verificar su existencia."
                    editCiudad.value=""
                }else{
                    _aviso.value="La ciudad \"${editCiudad.value}\" NO existe, verifique el nombre por favor."
                }
            }
        }
    }
}
/*
ESTO FUNCIONA BIEN
 private val jobViewModel= Job()
    private val uiScope= CoroutineScope(Dispatchers.Main + jobViewModel)
    override fun onCleared() {
        super.onCleared()
        jobViewModel.cancel()
    }

    var editCiudad= MutableLiveData<String>()

    //comprobacion de que el nombre no esta cargado


    //busca que retrofit devuelva un valor para la ciudad

    //si encuentra guarda en la db

    //si hay problema de internet solo guarda el nombre

    fun agregarCiudad(){
        if(!editCiudad.value.isNullOrEmpty()){
            uiScope.launch {
                insertar()
                editCiudad.value=""
            }
        }
    }
    private suspend fun insertar(){
        withContext(Dispatchers.IO){
            db.insertCiudad(Ciudad(name = "${editCiudad.value}"))
        }
    }
*/
