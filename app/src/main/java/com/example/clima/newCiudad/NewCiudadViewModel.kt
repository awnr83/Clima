package com.example.clima.newCiudad

import android.app.Application
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.clima.database.Ciudad
import com.example.clima.database.CiudadDatabaseDao
import kotlinx.coroutines.*

class NewCiudadViewModel(private val db: CiudadDatabaseDao): ViewModel() {

    private val jobViewModel= Job()
    private val uiScope= CoroutineScope(Dispatchers.Main + jobViewModel)
    override fun onCleared() {
        super.onCleared()
        jobViewModel.cancel()
    }

    var editCiudad= MutableLiveData<String>()

    fun agregarCiudad(){
        if(!editCiudad.value.isNullOrEmpty()){
            uiScope.launch {
                insertar()
                editCiudad.value=""
            }
        }
    }
    private suspend fun insertar(){
        withContext(Dispatchers.IO){
            db.insertCiudad(Ciudad(name = "${editCiudad.value}"))
        }
    }

}