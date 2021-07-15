package com.example.clima.detalle

import android.content.Intent
import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import android.widget.Toast
import androidx.core.app.ShareCompat
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import androidx.navigation.ui.NavigationUI
import com.example.clima.R
import com.example.clima.database.CiudadDatabase
import com.example.clima.databinding.FragmentDetalleBinding

class DetalleFragment : Fragment() {
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding= FragmentDetalleBinding.inflate(inflater)

        //se preparan los datos para crear el viewModel
        val arg= DetalleFragmentArgs.fromBundle(arguments!!).ciudad
        val application= requireNotNull(this.activity).application
        val db= CiudadDatabase.getInstance(application).ciudadDatabaseDao
        val viewModelFactory= DetalleViewModelFactory(arg, db)

        val viewModel= ViewModelProvider(this, viewModelFactory).get(DetalleViewModel::class.java)
        binding.viewModel=viewModel
        binding.lifecycleOwner=this

        viewModel.notificacion.observe(viewLifecycleOwner, Observer {
            if(it){
                Toast.makeText(context, getString(R.string.msg_actualizoCiudad), Toast.LENGTH_SHORT).show()
                viewModel.actualizadoC()
            }
        })
        return binding.root
    }
}