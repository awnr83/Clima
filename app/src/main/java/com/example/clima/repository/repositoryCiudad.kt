package com.example.clima.repository

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.clima.database.Ciudad
import com.example.clima.database.CiudadDatabase
import com.example.clima.database.CiudadDatabaseDao
import com.example.clima.network.CiudadApi
import com.example.clima.network.Weather
import kotlinx.coroutines.*
import java.text.SimpleDateFormat
import java.util.*

class RepositoryCiudad(private val database: CiudadDatabaseDao) {

    //variable lista todas las ciudades
    var ciudades: LiveData<List<Ciudad>> = database.listarCiudades()
    //variable muestra la informacion de una ciudad
    //var datos: Ciudad = database.obtenerCiudad(nomCiudad!!)


    //actualiza las temperatura de todas las ciudades
    suspend fun actualizarTemperatura(){
        withContext(Dispatchers.Default){
            val sdf = SimpleDateFormat("dd/M/yy HH:mm")
            val currentDate = sdf.format(Date())
            var listCiudades= mutableListOf<Ciudad>()
                try {
                    database.allCiudades().forEach{
                        var temp = CiudadApi.retrofitService.getWeather(it.name,"710b9a74fd5943aff012c7e3e83be945")
                        var ciudad = Ciudad(
                            id= it.id,
                            name=it.name,
                            temp= (temp.main!!.temp!! - 273.15),
                            temp_min = (temp.main!!.temp_min!! - 273.15),
                            temp_max = (temp.main!!.temp_max!! - 273.15),
                            feels_like = (temp.main!!.feels_like!! - 273.15),
                            pressure = temp.main!!.pressure,
                            humidity = temp.main!!.humidity,
                            ultimaActualizacion = currentDate.toString()
                        )
                        listCiudades.add(ciudad)
                    }
                    database.updateCiudades(listCiudades)
                }catch (e: Exception){
                    Log.i("alfredo","error: ${e.message}")
                }
        }
    }

    //actualiza la temperatura de una ciudad
    suspend fun actualizarTemperaturaCiudad(nomCiudad: String){
        withContext(Dispatchers.IO){
            if(!nomCiudad.isNullOrEmpty()) {
                try {
                    ciudades.value!!.forEach {
                        val temp = CiudadApi.retrofitService.getWeather(nomCiudad,"710b9a74fd5943aff012c7e3e83be945")
                        var ciudad= database.obtenerCiudad(nomCiudad)
                        ciudad = convertirWeatherCiudad(temp, ciudad)
                        database.actualizarCiudad(ciudad)
                    }
                }catch (e: Exception){
                    Log.i("alfredo","error: ${e.message}")
                }
            }
        }
    }

//permite crear una nueva ciudad
    suspend fun agregarCiudad(ciudad: Ciudad){
        withContext(Dispatchers.IO) {
            database.insertCiudad(ciudad)
        }
    }
    suspend fun buscarCiudadDb(nomCiudad: String):Ciudad{
        return withContext(Dispatchers.IO) {
             database.obtenerCiudad(nomCiudad)
        }
    }
    suspend fun buscarCiudadWeather(nombre: String):Ciudad{
        var tiempoDB= Ciudad()
        val sdf = SimpleDateFormat("dd/M/yy HH:mm")
        val currentDate = sdf.format(Date())
        try{
            val temp= CiudadApi.retrofitService.getWeather(nombre,"710b9a74fd5943aff012c7e3e83be945")
            tiempoDB= Ciudad(
                name=nombre,
                temp= (temp.main!!.temp!! - 273.15),
                temp_min = (temp.main!!.temp_min!! - 273.15),
                temp_max = (temp.main!!.temp_max!! - 273.15),
                feels_like = (temp.main!!.feels_like!! - 273.15),
                pressure = temp.main!!.pressure,
                humidity = temp.main!!.humidity,
                ultimaActualizacion = currentDate.toString()
            )
        }catch (e: Exception) {
            Log.i("alfredo","error repositoryCiudad: ${e.message}")
        }
        return tiempoDB
    }

    suspend fun eliminarCiudad(ciudad: Ciudad){
        withContext(Dispatchers.IO){
            database.eliminarCiudad(ciudad)
        }
    }


//permite cambiar de formato de Weather a Ciudad
    private fun convertirWeatherCiudad(temp:Weather, ciudad: Ciudad): Ciudad{
        val sdf = SimpleDateFormat("dd/M/yy HH:mm")
        val currentDate = sdf.format(Date())

        return Ciudad(
            id= ciudad.id,
            name=ciudad.name,
            temp= (temp.main!!.temp!! - 273.15),
            temp_min = (temp.main!!.temp_min!! - 273.15),
            temp_max = (temp.main!!.temp_max!! - 273.15),
            feels_like = (temp.main!!.feels_like!! - 273.15),
            pressure = temp.main!!.pressure,
            humidity = temp.main!!.humidity,
            ultimaActualizacion = currentDate.toString()
        )
    }
}
