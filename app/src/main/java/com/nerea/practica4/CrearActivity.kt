package com.nerea.practica4

import android.content.Intent
import android.os.Bundle
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
class CrearActivity : AppCompatActivity() {

    private lateinit var editNombre: EditText
    private lateinit var txtVida: TextView
    private lateinit var txtAtaque: TextView
    private lateinit var txtFechaSeleccionada: EditText
    private lateinit var btnSumarVida: Button
    private lateinit var btnRestarVida: Button
    private lateinit var btnSumarAtaque: Button
    private lateinit var btnRestarAtaque: Button
    private lateinit var btnCrear: Button
    private lateinit var btnLimpiar: Button
    private lateinit var dbHelper: DatabaseHelper

    private var vida = 70
    private var ataque = 30
    private var fechaNacimiento = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.crear_activity)

        dbHelper = DatabaseHelper(this)

        editNombre = findViewById(R.id.editNombre)
        txtVida = findViewById(R.id.txtVida)
        txtAtaque = findViewById(R.id.txtAtaque)
        txtFechaSeleccionada = findViewById(R.id.txtFechaSeleccionada)

        btnSumarVida = findViewById(R.id.btnSumarVida)
        btnRestarVida = findViewById(R.id.btnRestarVida)
        btnSumarAtaque = findViewById(R.id.btnSumarAtaque)
        btnRestarAtaque = findViewById(R.id.btnRestarAtaque)
        btnCrear = findViewById(R.id.btnCrear)
        btnLimpiar = findViewById(R.id.btnLimpiar)

        txtVida.text = vida.toString()
        txtAtaque.text = ataque.toString()

        btnSumarVida.setOnClickListener { actualizarVida(1) }
        btnRestarVida.setOnClickListener { actualizarVida(-1) }
        btnSumarAtaque.setOnClickListener { actualizarAtaque(1) }
        btnRestarAtaque.setOnClickListener { actualizarAtaque(-1) }

        txtFechaSeleccionada.setOnClickListener { mostrarDatePicker() }

        btnCrear.setOnClickListener { crearPersonaje() }
        btnLimpiar.setOnClickListener { limpiarCampos() }
    }

    private fun actualizarVida(valor: Int) {
        vida += valor
        if (vida < 0) vida = 0
        txtVida.text = vida.toString()
    }

    private fun actualizarAtaque(valor: Int) {
        ataque += valor
        if (ataque < 0) ataque = 0
        txtAtaque.text = ataque.toString()
    }

    private fun mostrarDatePicker() {
        val datePicker = DatePickerFragment { fecha ->
            fechaNacimiento = fecha
            txtFechaSeleccionada.setText(fechaNacimiento)
        }
        datePicker.show(supportFragmentManager, "datePicker")
    }

    private fun crearPersonaje() {
        val nombre = editNombre.text.toString()

        if (nombre.isEmpty() || fechaNacimiento.isEmpty()) {
            Toast.makeText(this, "Completa todos los campos", Toast.LENGTH_SHORT).show()
            return
        }

        val personaje = Personaje(0, nombre, vida, vida, ataque, fechaNacimiento)
        val id = dbHelper.anadirPersonaje(personaje)

        // Volver a MainActivity después de crear el personaje
        val intent = Intent(this, MainActivity::class.java)
        intent.putExtra("personaje_creado", true)
        startActivity(intent)

        Toast.makeText(this, "Personaje creado", Toast.LENGTH_SHORT).show()
        finish()
    }

    private fun limpiarCampos() {
        editNombre.text.clear()
        txtFechaSeleccionada.text.clear()
        vida = 70
        ataque = 30
        txtVida.text = vida.toString()
        txtAtaque.text = ataque.toString()
    }
}
