package com.nerea.practica4
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.floatingactionbutton.FloatingActionButton

class FragmentJuego : Fragment() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var gameList: MutableList<MatchGame>
    private lateinit var dbHelper: DatabaseHelper
    private lateinit var adapter: MatchGameAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflamos el layout para este fragmento
        val viewJugar = inflater.inflate(R.layout.fragment_juego, container, false)

        // Inicializamos el RecyclerView y el DatabaseHelper
        recyclerView = viewJugar.findViewById(R.id.recyclerViewJugar)
        recyclerView.layoutManager = LinearLayoutManager(context)

        // Usamos el contexto de la actividad que contiene el fragmento
        dbHelper = DatabaseHelper(requireContext())  // Asegúrate de usar un contexto no nulo

        // Inicializamos la lista de personajes
        gameList = mutableListOf()
        adapter = MatchGameAdapter(gameList, requireContext())  // Pasamos el contexto aquí
        recyclerView.adapter = adapter

        // Actualizamos la lista con los personajes al cargar el fragmento
        updateList()

        // Configuramos el FloatingActionButton para crear un personaje
        val fab: FloatingActionButton = viewJugar.findViewById(R.id.fab)
        fab.setOnClickListener {
            // Abrir la actividad CrearActivity para crear un nuevo personaje
            val intent = Intent(requireContext(), CrearActivity::class.java)
            startActivity(intent)
        }

        return viewJugar
    }

    // Método para eliminar un personaje (si es necesario) del RecyclerView
    fun eliminarPersonaje(id: Int) {
        // Eliminar personaje de la base de datos
        dbHelper.eliminarPersonaje(id)

        // Actualizar la lista de personajes después de la eliminación
        updateList()
    }

    // Método para actualizar la lista de personajes desde la base de datos
    fun updateList() {
        // Obtener los personajes con vida desde la base de datos
        val personajes = dbHelper.obtenerPersonajesConVida()

        // Limpiamos la lista existente
        gameList.clear()

        // Añadimos los personajes a la lista
        for (personaje in personajes) {
            gameList.add(MatchGame(personaje.nombre, personaje.ataque, personaje.vida, personaje.fechaNacimiento)) // Usamos "date" aquí
        }

        // Notificamos al adaptador que la lista ha cambiado
        adapter.notifyDataSetChanged()
    }


}
