package com.example.clima.ciudades

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.clima.database.CiudadDatabaseDao
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job

class CiudadesViewModel(private val db: CiudadDatabaseDao): ViewModel() {

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