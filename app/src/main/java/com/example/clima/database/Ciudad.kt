package com.example.clima.database

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.clima.network.Weather


@Entity(tableName = "ciudad")
data class Ciudad(
    @PrimaryKey(autoGenerate = true)
    val id: Long= 0L,
    var name: String? = null,
    val temp: Double? = null,
    val feels_like: Double? = null,
    val temp_min: Double? = null,
    val temp_max: Double? = null,
    val pressure: Int? = null,
    val humidity: Int? = null,
    val ultimaActualizacion: String?= null
)