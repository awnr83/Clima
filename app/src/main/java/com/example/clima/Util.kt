package com.climaApp.clima

import android.content.res.Resources

fun convertirDoubleToTemperatura(temp: Double, resources: Resources): String {
    return  resources.getString(R.string.temperatura,temp)
}

fun convertirIntToPrecipitaciones(pre: Int, resources: Resources): String {
    val presion: Float= (pre.toFloat())/1000
    return  resources.getString(R.string.presion,presion)
}

fun convertirIntToHumedad(hum: Int, resources: Resources): String {
    return  resources.getString(R.string.humedad,hum)
}