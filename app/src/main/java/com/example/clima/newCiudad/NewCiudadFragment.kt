package com.climaApp.clima.newCiudad

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.climaApp.clima.R
import com.climaApp.clima.database.CiudadDatabase
import com.climaApp.clima.databinding.FragmentNewCiudadBinding


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

        viewModel.notification.observe(viewLifecycleOwner, Observer {
            if(it){
                viewModel.notificacionC()
                ocultarTecaldo(requireView())
            }
        })

        return binding.root
    }

    fun ocultarTecaldo(view: View) {
        val input: InputMethodManager = requireActivity().getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        input.hideSoftInputFromWindow(view.windowToken, 0)
    }
}