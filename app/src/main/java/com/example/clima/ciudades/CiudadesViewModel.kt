package com.example.clima.ciudades

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.clima.database.Ciudad
import com.example.clima.database.CiudadDatabaseDao
import kotlinx.coroutines.*


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

    //boton agregar -> navega
    private val _navigate= MutableLiveData<Boolean>()
    val navigate:LiveData<Boolean>
        get()=_navigate
    fun onClickNavigate(){
        _navigate.value=true
    }
    fun onNavigate(){
        _navigate.value=false
    }

    init{
        _navigate.value= false
        _cantCiudades.value="Ciudades cargadas: "
    }

    fun eliminarCiudad(ciudad: Ciudad){
        uiScope.launch {
            withContext(Dispatchers.IO){
                db.eliminarCiudad(ciudad)
            }
            //jobViewModel.cancel()
        }
    }
}