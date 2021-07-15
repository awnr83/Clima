package com.example.clima.repository

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.clima.database.Ciudad
import com.example.clima.database.CiudadDatabaseDao
import com.example.clima.network.CiudadApi
import com.example.clima.network.Weather
import kotlinx.coroutines.*
import java.text.SimpleDateFormat
import java.util.*

class RepositoryCiudad(private val database: CiudadDatabaseDao) {

    //variable lista todas las ciudades
    var ciudades: LiveData<List<Ciudad>> = database.listarCiudadesLD()

    fun obtnenerCantidaCiudades(): LiveData<Int> {
        return database.totalCiudades()
    }

//actualiza las temperatura de todas las ciudades
    suspend fun actualizarTemperatura(){
        withContext(Dispatchers.IO){
            val sdf = SimpleDateFormat("dd/M/yy HH:mm")
            val currentDate = sdf.format(Date())
            var listCiudades= mutableListOf<Ciudad>()
            try {
                database.allCiudadesDB().forEach{
                    var temp = CiudadApi.retrofitService.getWeather(it.name,"710b9a74fd5943aff012c7e3e83be945")
                    var ciudad = Ciudad(
                        id= it.id,
                        name=it.name,
                        temp= temp.main!!.temp!!,
                        temp_min = temp.main!!.temp_min,
                        temp_max = temp.main!!.temp_max,
                        feels_like = temp.main!!.feels_like,
                        pressure = temp.main!!.pressure,
                        humidity = temp.main!!.humidity,
                        ultimaActualizacion = currentDate.toString()
                    )
                    listCiudades.add(ciudad)
                }
                database.updateCiudadesDB(listCiudades)
            }catch (e: Exception){
                Log.i("alfredo","error: ${e.message}")
            }
        }
    }
    fun obtenerCiudadLD(nombre: String): LiveData<Ciudad> {
        return database.obtenerCiudadLD(nombre)
    }

//actualiza la temperatura de una ciudad
    suspend fun actualizarTemperaturaCiudad(ciudad:Ciudad){
        withContext(Dispatchers.Default){
            val sdf = SimpleDateFormat("dd/M/yy HH:mm")
            val currentDate = sdf.format(Date())
            try {
                var temp = CiudadApi.retrofitService.getWeather(ciudad.name,"710b9a74fd5943aff012c7e3e83be945")
                    ciudad.temp=temp.main!!.temp
                    ciudad.temp_min=temp.main!!.temp_min
                    ciudad.temp_max=temp.main!!.temp_max
                    ciudad.feels_like=temp.main!!.feels_like
                    ciudad.pressure=temp.main!!.pressure
                    ciudad.humidity=temp.main!!.humidity
                    ciudad.ultimaActualizacion=currentDate
                database.actualizarCiudadDB(ciudad)
            }catch (e: Exception){
                Log.i("alfredo","error: ${e.message}")
            }
        }
    }

//permite crear una nueva ciudad
    suspend fun agregarCiudad(ciudad: Ciudad){
        withContext(Dispatchers.IO) {
            database.insertCiudadDB(ciudad)
        }
    }
    suspend fun buscarCiudadDb(nomCiudad: String):Ciudad{
        return withContext(Dispatchers.IO) {
             database.obtenerCiudadDB(nomCiudad)
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
                temp= temp.main!!.temp,
                temp_min = temp.main!!.temp_min,
                temp_max = temp.main!!.temp_max,
                feels_like = temp.main!!.feels_like,
                pressure = temp.main!!.pressure,
                humidity = temp.main!!.humidity,
                ultimaActualizacion = currentDate.toString()
            )
        }catch (e: Exception) {
            Log.i("alfredo","error repositoryCiudad: ${e.message}")
        }
        return tiempoDB
    }

//eliminar ciudad de la db
    suspend fun eliminarCiudad(ciudad: Ciudad){
        withContext(Dispatchers.IO){
            database.eliminarCiudadDB(ciudad)
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
