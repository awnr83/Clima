package com.example.clima

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.location.Location
import android.location.LocationListener
import android.location.LocationManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.PersistableBundle
import android.preference.PreferenceManager
import android.util.Log
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.navigation.findNavController
import androidx.navigation.ui.NavigationUI

const val KEY_LATITUD= "key_latitud"
const val KEY_LONGUITUD= "key_longuitud"

class MainActivity : AppCompatActivity() {
    private lateinit var locationManager: LocationManager
    private lateinit var locationListener: LocationListener
    private lateinit var location: Location

    val MY_PERMISSIONS_REQUEST_CODE = 33
    val LOCATION_METHOD = LocationManager.NETWORK_PROVIDER



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)


//Se agrega boton de retroceso
            val navCont= this.findNavController(R.id.myNavHostFragment)
        NavigationUI.setupActionBarWithNavController(this, navCont)

//permisos de localizacion
        locationManager = this.getSystemService(Context.LOCATION_SERVICE) as LocationManager
        locationListener = locListener()
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this,arrayOf(Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION), MY_PERMISSIONS_REQUEST_CODE)
        } else {
            locationManager.requestLocationUpdates(LOCATION_METHOD, 0, 0f, locationListener)
            location = locationManager.getLastKnownLocation(LOCATION_METHOD)!!
            showLocation(location, false)
        }
    }
    //habilitación buton up
    override fun onSupportNavigateUp(): Boolean {
        val navCont= this.findNavController(R.id.myNavHostFragment)
        return navCont.navigateUp()
    }

    override fun onSaveInstanceState(outState: Bundle, outPersistentState: PersistableBundle) {
        super.onSaveInstanceState(outState, outPersistentState)

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this,arrayOf(Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION), MY_PERMISSIONS_REQUEST_CODE)
        } else {
            locationManager.requestLocationUpdates(LOCATION_METHOD, 0, 0f, locationListener)
            location = locationManager.getLastKnownLocation(LOCATION_METHOD)!!
            outState.putDouble(KEY_LATITUD,location.latitude)
            outState.putDouble(KEY_LONGUITUD,location.longitude)
            showLocation(location, false)
        }

    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>, grantResults: IntArray) {
        when (requestCode) {
            MY_PERMISSIONS_REQUEST_CODE -> {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.size > 0) {
                    if (ActivityCompat.checkSelfPermission(
                            this,
                            Manifest.permission.ACCESS_FINE_LOCATION
                        ) == PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                            this,
                            Manifest.permission.ACCESS_COARSE_LOCATION
                        ) == PackageManager.PERMISSION_GRANTED
                    ) {
                        locationManager.requestLocationUpdates(LOCATION_METHOD, 0, 0f, locationListener)
                        location = locationManager.getLastKnownLocation(LOCATION_METHOD)!!
                        showLocation(location, false)
                    }
                } else {
                    Toast.makeText(this, "Tienes que dar permisos para usar esta app!", Toast.LENGTH_SHORT).show()
                }
                return
            }
        }
    }
    private fun showLocation(loc: Location?, changed: Boolean?) {

        val accurancy: Float
        if (loc != null) {
            val latitude = loc.latitude
            val longitude = loc.longitude
            accurancy = loc.accuracy
//            if ((!changed!!)) {
//                //Log.i("alfredo","Tu Location Es - \nLat: $latitude\nLong: $longitude\nAccurancy: $accurancy")
//            } else
//                //Log.i("alfredo", "Tu Location Ha Cambiado - \nLat: $latitude\nLong: $longitude\nAccurancy: $accurancy")
        }
    }
    private inner class locListener : LocationListener {

        override fun onLocationChanged(location: Location) {
            showLocation(location, true)
        }

        override fun onStatusChanged(s: String, i: Int, bundle: Bundle) {

        }

        override fun onProviderEnabled(s: String) {

        }

        override fun onProviderDisabled(s: String) {

        }
    }
}