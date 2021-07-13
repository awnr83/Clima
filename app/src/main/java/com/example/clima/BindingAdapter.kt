package com.example.clima

import android.widget.TextView
import androidx.databinding.BindingAdapter
import com.example.clima.convertirDoubleToTemperatura

@BindingAdapter("doubleTemperatura")
fun TextView.setdoubleTemperatura(temp: Double?){
    temp?.let {
        text=  convertirDoubleToTemperatura(temp, context.resources)
    }
}

@BindingAdapter("intPrecipitaciones")
fun TextView.setintPrecipitaciones(temp: Int?){
    temp?.let {
        text=  convertirIntToPrecipitaciones(temp, context.resources)
    }
}

@BindingAdapter("intHumedad")
fun TextView.setintHumedad(temp: Int?){
    temp?.let {
        text=  convertirIntToHumedad(temp, context.resources)
    }
}