package com.example.clima.database

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "ciudad")
data class Ciudad (
    @PrimaryKey(autoGenerate = true)
    val id: Long= 0L,
    @ColumnInfo
    val nombre: String="",
    @ColumnInfo
    val ultimaTemperatura: String="",
    @ColumnInfo
    val ultimaActualizacion: String=""
)