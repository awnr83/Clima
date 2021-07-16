package com.climaApp.clima.database

import androidx.lifecycle.LiveData
import androidx.room.*


@Dao
interface CiudadDatabaseDao {

// devuelve Live Data
    @Query("select * from ciudad order by name asc")
    fun listarCiudadesLD(): LiveData <List<Ciudad>>

    @Query("select * from ciudad where name= :name")
    fun obtenerCiudadLD(name:String): LiveData<Ciudad>

//DB--
    @Query("select * from ciudad where name= :name")
    fun obtenerCiudadDB(name:String): Ciudad

    @Insert
    fun insertCiudadDB(ciudad: Ciudad)

    @Update
    fun actualizarCiudadDB(ciudad: Ciudad)
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun updateCiudadesDB(ciudades: List<Ciudad>)

    @Delete
    fun eliminarCiudadDB(ciudad: Ciudad)

    @Query("select * from ciudad order by name asc")
    fun allCiudadesDB(): List<Ciudad>

    @Query("select count(*) from ciudad")
    fun totalCiudades():LiveData<Int>
}