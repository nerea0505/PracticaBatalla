package com.nerea.practica4

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.core.widget.addTextChangedListener
class FragmentBienvenido : Fragment() {
    private lateinit var textNombre: TextView
    private lateinit var textMuertes: TextView
    private lateinit var botonResetearVidas: Button
    private lateinit var editUser: EditText
    private lateinit var databaseHelper: DatabaseHelper

    // Lógica para resetear las muertes del personaje
    private fun resetearMuertes() {
        val nombrePersonaje = editUser.text.toString()

        if (nombrePersonaje.isNotEmpty()) {
            // Verificar si el personaje existe antes de resetear las muertes
            val personajeExistente = databaseHelper.obtenerPersonajePorNombre(nombrePersonaje)
            if (personajeExistente != null) {
                // Llamada a la función que resetea las muertes del personaje en la base de datos
                databaseHelper.resetearMuertesPersonaje(nombrePersonaje)
                // Actualizar la UI para mostrar las muertes reseteadas
                textMuertes.text = "Llevas 0 muertes"
            } else {
                textMuertes.text = "Este personaje no existe"
            }
        }
    }

    // Función que obtiene las muertes del personaje
    private fun obtenerMuertes() {
        val nombrePersonaje = editUser.text.toString()

        if (nombrePersonaje.isNotEmpty()) {
            // Llamada a la función que obtiene las muertes de la base de datos
            val muertes = databaseHelper.obtenerMuertesDePersonaje(nombrePersonaje)
            if (muertes != null) {
                textMuertes.text = "Llevas $muertes muertes"
            } else {
                textMuertes.text = "Este personaje no existe"
            }
        } else {
            // Si no se ha ingresado un nombre, mostrar un mensaje vacío o predeterminado
            textMuertes.text = ""
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val rootView = inflater.inflate(R.layout.fragment_bienvenido, container, false)

        // Referencias a los views
        textNombre = rootView.findViewById(R.id.text_NombreBienvenido)
        textMuertes = rootView.findViewById(R.id.Edit_muertes)
        botonResetearVidas = rootView.findViewById(R.id.Boton_resetHP)
        editUser = rootView.findViewById(R.id.Edit_user)

        // Inicializar DatabaseHelper
        databaseHelper = DatabaseHelper(requireContext())

        // Configurar el botón Resetear Vidas
        botonResetearVidas.setOnClickListener { resetearMuertes() }

        // Configurar el EditText para obtener las muertes al escribir el nombre del personaje
        editUser.addTextChangedListener {
            obtenerMuertes() // Se actualizan las muertes cada vez que el usuario cambia el texto
        }

        return rootView
    }
}
