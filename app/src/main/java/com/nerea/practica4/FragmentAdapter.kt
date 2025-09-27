package com.nerea.practica4

import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter

class FragmentAdapter(activity: AppCompatActivity) : FragmentStateAdapter(activity) {

    override fun getItemCount(): Int {
        return 3  // Número de pestañas
    }

    override fun createFragment(position: Int): Fragment {
        return when (position) {
            0 -> FragmentBienvenido()  // Primer fragmento (Bienvenido)
            1 -> FragmentJuego()       // Segundo fragmento (Jugar)
            2 -> FragmentEstadisticas() // Tercer fragmento (Estadísticas)
            else -> throw IllegalStateException("Unexpected position $position")
        }
    }
}
