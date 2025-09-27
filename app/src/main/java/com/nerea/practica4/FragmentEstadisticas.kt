package com.nerea.practica4

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class FragmentEstadisticas : Fragment() {
    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: MatchResultAdapter
    private val matchResults = mutableListOf<MatchResult>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val rootView = inflater.inflate(R.layout.fragment_estadisticas, container, false)

        // Configuración del RecyclerView
        recyclerView = rootView.findViewById(R.id.recyclerView)
        recyclerView.layoutManager = LinearLayoutManager(context)

        // Obtener los resultados de la base de datos
        val databaseHelper = DatabaseHelper(requireContext())
        val resultados = databaseHelper.obtenerResultadosBatalla()

        // Configurar el adaptador con los resultados
        adapter = MatchResultAdapter(resultados)
        recyclerView.adapter = adapter

        return rootView
    }
}
