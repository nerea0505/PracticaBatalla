package com.nerea.practica4

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.viewpager2.widget.ViewPager2
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Asociar ViewPager2 con el adapter
        val viewPager2: ViewPager2 = findViewById(R.id.mi_contenedor)
        val tabLayout: TabLayout = findViewById(R.id.mi_tab)

        val adapter = FragmentAdapter(this)
        viewPager2.adapter = adapter

        // Configurar el TabLayout para que se sincronice con el ViewPager2
        TabLayoutMediator(tabLayout, viewPager2) { tab, position ->
            when (position) {
                0 -> tab.text = "Bienvenido"
                1 -> tab.text = "Jugar"
                2 -> tab.text = "Estadísticas"
            }
        }.attach()
    }


}
