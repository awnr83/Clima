package com.example.clima.database

import androidx.lifecycle.LiveData
import androidx.room.*


@Dao
interface CiudadDatabaseDao {
    @Insert
    fun insertCiudad(ciudad: Ciudad)

    @Query("select * from ciudad order by name asc")
    fun listarCiudades(): LiveData <List<Ciudad>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun updateCiudades(ciudades: List<Ciudad>)

    @Query("select * from ciudad order by name asc")
    fun allCiudades(): List<Ciudad>

    @Query("select * from ciudad where name= :name")
    fun obtenerCiudad(name:String): Ciudad

    @Update
    fun actualizarCiudad(ciudad: Ciudad)

    @Delete
    fun eliminarCiudad(ciudad: Ciudad)
}