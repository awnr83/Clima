package com.climaApp.clima.ciudades

import android.content.ActivityNotFoundException
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.*
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.NavigationUI
import com.climaApp.clima.R
import com.climaApp.clima.database.CiudadDatabase
import com.climaApp.clima.databinding.FragmentCiudadesBinding

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

        //se observan los cambios de la cantidad de ciudades
        viewModel._cant.observe(viewLifecycleOwner, Observer { cantCiudades->
            when(cantCiudades){
                0-> {
                    //cambia titulo
                    binding.textView.text=getString(R.string.fc_title_vacio)
                    //se bloquea boton actualizar
                    binding.imageView.visibility =View.INVISIBLE
                }
                else->{
                    //cambia titulo
                    binding.textView.text=getString(R.string.fc_title_ciudades)
                    //se permite boton actualizar
                    binding.imageView.visibility=View.VISIBLE

                }
            }
        })

        //se navega a la pantalla de agregar ciudad
        viewModel.navigate.observe(viewLifecycleOwner, Observer {
            if(it){
                findNavController().navigate(CiudadesFragmentDirections.actionCiudadesFragmentToNewCiudadFragment())
                viewModel.onNavigate()
            }
        })

        //aviso de  eliminacion de ciudad
        viewModel.eliminado.observe(viewLifecycleOwner, Observer {
            if(it){
                Toast.makeText(context,R.string.msg_eliminado, Toast.LENGTH_SHORT).show()
                viewModel.eliminadoC()
            }
        })

        //aviso de actualizacion correcta
        viewModel.actualizo.observe(viewLifecycleOwner, Observer {
            if(it){
                if(viewModel._cant.value!! > 0) {
                    Toast.makeText(context, R.string.msg_actualizo, Toast.LENGTH_SHORT).show()
                }
                viewModel.actualizoC()
            }
        })

        //agrega el menu
        setHasOptionsMenu(true)

        return binding.root
    }

    //menu
    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        inflater.inflate(R.menu.ciudad_menu,menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId){
            R.id.correo-> emailSucces()
        }
        return NavigationUI.onNavDestinationSelected(item, view!!.findNavController())
                || super.onOptionsItemSelected(item)
    }

    private fun aboutSucces(){

    }

    private fun emailSucces(){
        try {
            startActivity(Intent.createChooser(getEmailIntent(),"Enviar email..."))
        }catch (e: ActivityNotFoundException){
            Toast.makeText(this.context, "NO existe ningún cliente de email instalado!.", Toast.LENGTH_SHORT).show()
        }

    }
    private fun getEmailIntent(): Intent {
        val email= Intent(Intent.ACTION_SENDTO, Uri.fromParts("mailto", "moappDev.Service@gmail.com", null))
        email.putExtra(Intent.EXTRA_SUBJECT, "Android APP - Operaciones!")
        email.putExtra(Intent.EXTRA_TEXT, "Escribe aquí tu mensaje")
        return email
    }
}