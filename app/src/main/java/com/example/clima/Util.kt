package com.example.clima

import android.content.res.Resources

fun convertirDoubleToTemperatura(temp: Double, resources: Resources): String {
    return  resources.getString(R.string.temperatura,temp)
}

fun convertirIntToPrecipitaciones(pre: Int, resources: Resources): String {
    return  resources.getString(R.string.precipitaciones,pre)
}

fun convertirIntToHumedad(hum: Int, resources: Resources): String {
    return  resources.getString(R.string.humedad,hum)
}