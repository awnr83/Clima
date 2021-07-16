package com.climaApp.clima.database

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.climaApp.clima.network.Weather
import kotlinx.android.parcel.Parcelize

@Parcelize
@Entity(tableName = "ciudad")
data class Ciudad(
    @PrimaryKey(autoGenerate = true)
    val id: Long= 0L,
    var name: String? = null,
    var temp: Double? = null,
    var feels_like: Double? = null,
    var temp_min: Double? = null,
    var temp_max: Double? = null,
    var pressure: Int? = null,
    var humidity: Int? = null,
    var ultimaActualizacion: String?= null
):Parcelable