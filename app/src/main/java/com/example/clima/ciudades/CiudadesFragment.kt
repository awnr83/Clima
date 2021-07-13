package com.example.clima.ciudades

import android.os.Bundle
import android.view.*
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.NavigationUI
import com.example.clima.R
import com.example.clima.database.CiudadDatabase
import com.example.clima.databinding.FragmentCiudadesBinding

class CiudadesFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding= FragmentCiudadesBinding.inflate(inflater)

        //instancia de la DB
        val application= requireNotNull(this.activity).application
        val dataBase= CiudadDatabase.getInstance(application).ciudadDatabaseDao

        //se crea el viewModel de ciudad
        val viewModelFactory= CiudadViewModelFactory(dataBase)
        val viewModel= ViewModelProvider(this,viewModelFactory).get(CiudadesViewModel::class.java)

        //se asigna el viewModel y lifecycle
        binding.viewModel=viewModel
        binding.lifecycleOwner=this

        //se carga el RecyclerView
        val adapter= CiudadAdapter(CiudadAdapter.CiudadListener {
            findNavController().navigate(CiudadesFragmentDirections.actionCiudadesFragmentToDetalleFragment(it))
        })
        binding.listCiudades.adapter=adapter

        //
        viewModel.allCiudades.observe(viewLifecycleOwner, Observer {
            it?.let{
                adapter.submitList(it)
            }
        })

        //agrega el menu para agregar ciudades
        setHasOptionsMenu(true)

        return binding.root
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        inflater.inflate(R.menu.ciudad_menu,menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return NavigationUI.onNavDestinationSelected(item, view!!.findNavController())
                || super.onOptionsItemSelected(item)
    }
}