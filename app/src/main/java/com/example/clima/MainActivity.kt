package com.example.clima

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import androidx.navigation.findNavController
import androidx.navigation.ui.NavigationUI

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)


        //Se agrega boton de retroceso
        val navCont= this.findNavController(R.id.myNavHostFragment)
        NavigationUI.setupActionBarWithNavController(this, navCont)
    }

    override fun onSupportNavigateUp(): Boolean {
        val navCont= this.findNavController(R.id.myNavHostFragment)
        return navCont.navigateUp()
    }
}