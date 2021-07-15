package com.example.clima.ciudades

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.*
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.NavigationUI
import com.example.clima.KEY_LATITUD
import com.example.clima.KEY_LONGUITUD
import com.example.clima.R
import com.example.clima.database.CiudadDatabase
import com.example.clima.databinding.FragmentCiudadesBinding

class CiudadesFragment : Fragment() {


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding= FragmentCiudadesBinding.inflate(inflater)

        Log.i("alfredo","parametros: $savedInstanceState")
        if(savedInstanceState!=null){
            var lat= savedInstanceState.getDouble(KEY_LATITUD)
            var lon= savedInstanceState.getDouble(KEY_LONGUITUD)
            Log.i("alfredo","latitud: $lat longitud: $lon")
        }

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
        },CiudadAdapter.CiudadEliminar{
            viewModel.eliminarCiudad(it)
        })
        binding.listCiudades.adapter=adapter

        //se carga recyclerView
        viewModel.allCiudades.observe(viewLifecycleOwner, Observer {
            it?.let{
                adapter.submitList(it)
            }
        })

        viewModel.navigate.observe(viewLifecycleOwner, Observer {
            if(it){
                findNavController().navigate(CiudadesFragmentDirections.actionCiudadesFragmentToNewCiudadFragment())
                viewModel.onNavigate()
            }
        })

        viewModel.notificacion.observe(viewLifecycleOwner, Observer {
            if(it){
                //Toast.makeText(requireActivity(),viewModel.aviso, Toast.LENGTH_LONG).show()
                viewModel.notificacionC()
            }
        })

        //agrega el menu para agregar ciudades
        setHasOptionsMenu(true)

        return binding.root
    }

    //menu agregar ciudades
    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        inflater.inflate(R.menu.ciudad_menu,menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return NavigationUI.onNavDestinationSelected(item, view!!.findNavController())
                || super.onOptionsItemSelected(item)
    }

}