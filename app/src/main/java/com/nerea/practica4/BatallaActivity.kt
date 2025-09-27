package com.nerea.practica4

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class BatallaActivity : AppCompatActivity() {

    private lateinit var txtAtacante: TextView
    private lateinit var txtDefensor: TextView
    private lateinit var txtAtaqueAtacante: TextView
    private lateinit var txtAtaqueDefensor: TextView
    private lateinit var progressBarAtacante: ProgressBar
    private lateinit var progressBarDefensor: ProgressBar
    private lateinit var txtVidaAtacante: TextView
    private lateinit var txtVidaDefensor: TextView
    private lateinit var txtVidaAtacanteTotal: TextView
    private lateinit var txtVidaDefensorTotal: TextView
    private lateinit var btnAtacar: Button
    private lateinit var dbHelper: DatabaseHelper

    private var atacante: Personaje? = null
    private var defensor: Personaje? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.batalla_activity)

        dbHelper = DatabaseHelper(this)

        // Inicializamos las vistas
        txtAtacante = findViewById(R.id.txtAtacante)
        txtDefensor = findViewById(R.id.txtDefensor)
        txtAtaqueAtacante = findViewById(R.id.txtAtaqueAtacante)
        txtAtaqueDefensor = findViewById(R.id.txtAtaqueDefensor)
        progressBarAtacante = findViewById(R.id.progressBarAtacante)
        progressBarDefensor = findViewById(R.id.progressBarDefensor)
        txtVidaAtacante = findViewById(R.id.txtVidaAtacante)
        txtVidaDefensor = findViewById(R.id.txtVidaDefensor)
        txtVidaAtacanteTotal = findViewById(R.id.txtVidaAtacanteTotal)
        txtVidaDefensorTotal = findViewById(R.id.txtVidaDefensorTotal)
        btnAtacar = findViewById(R.id.btnAtacar)

        // Obtenemos los personajes de la intención
        atacante = intent.getSerializableExtra("atacante") as? Personaje

        // Aseguramos que el defensor no sea el mismo que el atacante
        if (atacante != null) {
            defensor = obtenerDefensorAleatorio(atacante!!)
        }

        if (atacante != null && defensor != null) {
            actualizarUI()
        }

        btnAtacar.setOnClickListener {
            if (atacante != null && defensor != null) {
                realizarAtaque()
            }
        }
    }

    private fun obtenerDefensorAleatorio(atacante: Personaje): Personaje? {
        // Obtenemos una lista de personajes con vida
        val listaDefensores = dbHelper.obtenerPersonajesConVida()

        // Filtramos para eliminar al atacante de la lista
        val listaDefensoresDisponibles = listaDefensores.filter { it != atacante }

        // Si hay defensores disponibles, devolvemos uno aleatorio, sino, mostramos un mensaje
        return if (listaDefensoresDisponibles.isNotEmpty()) {
            listaDefensoresDisponibles.random()
        } else {
            null // No hay defensores disponibles
        }
    }

    private fun actualizarUI() {
        // Mostramos los datos del atacante y defensor
        txtAtacante.text = atacante!!.nombre
        txtDefensor.text = defensor!!.nombre
        txtAtaqueAtacante.text = "Ataque: ${atacante!!.ataque}"
        txtAtaqueDefensor.text = "Ataque: ${defensor!!.ataque}"

        // Actualizamos las barras de vida y los textos de vida
        progressBarAtacante.max = atacante!!.vidaMaxima
        progressBarAtacante.progress = atacante!!.vida
        txtVidaAtacante.text = "Vida actual: "
        txtVidaAtacanteTotal.text = "${atacante!!.vida}/${atacante!!.vidaMaxima}"

        progressBarDefensor.max = defensor!!.vidaMaxima
        progressBarDefensor.progress = defensor!!.vida
        txtVidaDefensor.text = "Vida actual: "
        txtVidaDefensorTotal.text = "${defensor!!.vida}/${defensor!!.vidaMaxima}"
    }

    private fun realizarAtaque() {
        // El atacante le resta vida al defensor
        defensor!!.vida -= atacante!!.ataque
        if (defensor!!.vida < 0) defensor!!.vida = 0

        // El defensor le resta vida al atacante
        atacante!!.vida -= defensor!!.ataque
        if (atacante!!.vida < 0) atacante!!.vida = 0

        // Actualizamos la UI después del ataque
        actualizarUI()

        // Verificamos si alguno de los dos ha sido derrotado
        if (atacante!!.vida == 0 || defensor!!.vida == 0) {
            val ganador = if (atacante!!.vida > 0) atacante!!.nombre else defensor!!.nombre
            val mensaje = if (atacante!!.vida == 0) {
                "${atacante!!.nombre} ha sido derrotado!"
            } else {
                "${defensor!!.nombre} ha sido derrotado!"
            }

            // Obtener la fecha actual
            val fecha = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(Date())

            // Almacenamos el resultado en la base de datos
            dbHelper.almacenarResultadoBatalla(atacante!!, defensor!!, ganador, fecha)

            // Mostramos un mensaje con el resultado
            Toast.makeText(this, mensaje, Toast.LENGTH_SHORT).show()

            // Volvemos al fragmento de juego o actividad principal
            val intent = Intent(this, MainActivity::class.java) // O la actividad correspondiente
            startActivity(intent)
            finish() // Terminamos la actividad actual
        }
    }


}
