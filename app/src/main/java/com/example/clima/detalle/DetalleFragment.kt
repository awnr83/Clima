package com.example.clima.detalle

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import com.example.clima.database.CiudadDatabase
import com.example.clima.databinding.FragmentDetalleBinding

class DetalleFragment : Fragment() {
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding= FragmentDetalleBinding.inflate(inflater)

        //se preparan los datos para crear el viewModel
        val arg= DetalleFragmentArgs.fromBundle(arguments!!).nombre
        val application= requireNotNull(this.activity).application
        val db= CiudadDatabase.getInstance(application).ciudadDatabaseDao
        val viewModelFactory= DetalleViewModelFactory(arg, db)

        val viewModel= ViewModelProvider(this, viewModelFactory).get(DetalleViewModel::class.java)
        binding.viewModel=viewModel
        binding.lifecycleOwner=this

        return binding.root
    }
}