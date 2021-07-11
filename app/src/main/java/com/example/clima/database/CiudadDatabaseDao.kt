package com.example.clima.database

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query

@Dao
interface CiudadDatabaseDao {
    @Insert
    fun insertCiudad(ciudad: Ciudad)

    @Query("select * from ciudad order by nombre desc")
    fun listarCiudades(): LiveData <List<Ciudad>>
}