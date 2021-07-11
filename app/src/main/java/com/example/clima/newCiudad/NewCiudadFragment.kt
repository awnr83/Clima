package com.example.clima.newCiudad

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import com.example.clima.database.CiudadDatabase
import com.example.clima.databinding.FragmentNewCiudadBinding

class NewCiudadFragment : Fragment() {
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding= FragmentNewCiudadBinding.inflate(inflater)

        //instancia de db
        val application= requireNotNull(this.activity).application
        val db= CiudadDatabase.getInstance(application).ciudadDatabaseDao

        val viewModelFactory= NewCiudadViewModelFactory(db)
        val viewModel= ViewModelProvider(this, viewModelFactory).get(NewCiudadViewModel::class.java)
        binding.viewModel= viewModel
        binding.lifecycleOwner= this

        return binding.root
    }
}