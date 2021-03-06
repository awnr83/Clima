package com.climaApp.clima.network

import com.climaApp.clima.database.Ciudad
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class Weather(
    val name: String? = null,
    val main: Main? = null,
    val cod: Int?=null
)

@Json(name="main")
data class Main(
    var temp: Double? = null,
    var feels_like: Double? = null,
    var temp_min: Double? = null,
    var temp_max: Double? = null,
    val pressure: Int? = null,
    val humidity: Int? = null
)