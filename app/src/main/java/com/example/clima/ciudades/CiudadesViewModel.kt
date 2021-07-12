package com.example.clima.ciudades

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.clima.database.CiudadDatabaseDao
import com.example.clima.network.CiudadApi
import com.example.clima.network.Main
import com.example.clima.network.Weather
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import java.lang.Exception

class CiudadesViewModel(private val db: CiudadDatabaseDao, application: Application): AndroidViewModel(application) {

    private val jobViewModel= Job()
    private val uiScope= CoroutineScope(Dispatchers.Main + jobViewModel)
    override fun onCleared() {
        super.onCleared()
        jobViewModel.cancel()
    }


    val allCiudades= db.listarCiudades()

    private val _cantCiudades= MutableLiveData<String>()
    val cantCiudades:LiveData<String>
        get()=_cantCiudades
    init{
        _cantCiudades.value="Cantidad de Ciudades cargadas: ${allCiudades.value?.size}"
    }
}