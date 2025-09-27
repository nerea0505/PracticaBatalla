package com.nerea.practica4

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Lifecycle
import androidx.viewpager2.adapter.FragmentStateAdapter


class AdaptadorFragmentos(private val listaFragmentos : List<Fragment>,
                            fragmentManager: FragmentManager, lifecycle: Lifecycle)
                            : FragmentStateAdapter(fragmentManager,lifecycle) {

    override fun getItemCount(): Int {
        return listaFragmentos.size
    }

    override fun createFragment(position: Int): Fragment {
        return listaFragmentos [position]
    }
}