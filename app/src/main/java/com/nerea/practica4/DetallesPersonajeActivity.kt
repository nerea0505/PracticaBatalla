package com.nerea.practica4

import android.app.AlertDialog
import android.app.DatePickerDialog
import android.content.Intent
import android.os.Bundle
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import java.util.*
class DetallesPersonajeActivity : AppCompatActivity() {

    private lateinit var imagenPersonaje: ImageView
    private lateinit var txtNombre: TextView
    private lateinit var txtAtaque: TextView
    private lateinit var txtVida: TextView
    private lateinit var txtFechaNacimiento: TextView
    private lateinit var btnModificar: Button
    private lateinit var btnEliminar: Button
    private lateinit var btnAtacar: Button
    private lateinit var dbHelper: DatabaseHelper
    private var personaje: Personaje? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.detalles_activity)

        dbHelper = DatabaseHelper(this)

        // Vincular vistas
        imagenPersonaje = findViewById(R.id.imagenPersonaje)
        txtNombre = findViewById(R.id.txtNombrePersonaje)
        txtAtaque = findViewById(R.id.txtAtaque)
        txtVida = findViewById(R.id.txtVida)
        txtFechaNacimiento = findViewById(R.id.txtFechaSeleccionada)
        btnModificar = findViewById(R.id.btnModificar)
        btnEliminar = findViewById(R.id.btnEliminar)
        btnAtacar = findViewById(R.id.btnAtacar)

        // Obtener el personaje de la intención
        val nombrePersonaje = intent.getStringExtra("nombre_personaje")
        personaje = dbHelper.obtenerPersonajePorNombre(nombrePersonaje ?: "")

        personaje?.let { actualizarUI(it) }

        btnModificar.setOnClickListener {
            personaje?.let { mostrarDialogModificar(it) }
        }

        btnEliminar.setOnClickListener {
            personaje?.let { mostrarDialogEliminar(it) }
        }

        btnAtacar.setOnClickListener {
            personaje?.let {
                val intent = Intent(this, BatallaActivity::class.java)
                intent.putExtra("atacante", it)
                startActivity(intent)
                finish()
            }
        }
    }

    private fun actualizarUI(personaje: Personaje) {
        txtNombre.text = personaje.nombre
        txtAtaque.text = "Ataque: ${personaje.ataque}"
        txtVida.text = "Vida: ${personaje.vida}/${personaje.vidaMaxima}"
        txtFechaNacimiento.text = "Fecha de Nacimiento: ${personaje.fechaNacimiento}"
        imagenPersonaje.setImageResource(R.drawable.ic_launcher_background)
    }

    private fun mostrarDialogModificar(personaje: Personaje) {
        val dialogView = layoutInflater.inflate(R.layout.dialog_modificar_personaje, null)
        val editAtaque = dialogView.findViewById<EditText>(R.id.editAtaque)
        val editVida = dialogView.findViewById<EditText>(R.id.editVida)
        val txtFecha = dialogView.findViewById<TextView>(R.id.txtFechaDialog)
        val btnFecha = dialogView.findViewById<Button>(R.id.btnSeleccionarFecha)

        editAtaque.setText(personaje.ataque.toString())
        editVida.setText(personaje.vida.toString())
        txtFecha.text = personaje.fechaNacimiento

        btnFecha.setOnClickListener {
            mostrarDatePicker(txtFecha)
        }

        AlertDialog.Builder(this)
            .setTitle("Modificar Personaje")
            .setView(dialogView)
            .setPositiveButton("Aceptar") { _, _ ->
                val nuevoAtaque = editAtaque.text.toString().toIntOrNull() ?: personaje.ataque
                val nuevaVida = editVida.text.toString().toIntOrNull() ?: personaje.vida
                val nuevaFecha = txtFecha.text.toString()

                val personajeModificado = personaje.copy(ataque = nuevoAtaque, vida = nuevaVida, fechaNacimiento = nuevaFecha)
                dbHelper.modificarPersonaje(personajeModificado)

                // Actualizar la UI con el personaje modificado
                actualizarUI(personajeModificado)

                // Actualizar la lista en el fragmento
                val fragment = supportFragmentManager.findFragmentByTag("fragmentJuego") as FragmentJuego
                fragment.updateList()

                Toast.makeText(this, "Personaje modificado", Toast.LENGTH_SHORT).show()
            }
            .setNegativeButton("Cancelar", null)
            .show()
    }

    private fun mostrarDialogEliminar(personaje: Personaje) {
        AlertDialog.Builder(this)
            .setTitle("Eliminar Personaje")
            .setMessage("¿Estás seguro de que deseas eliminar a ${personaje.nombre}?")
            .setPositiveButton("Sí") { _, _ ->
                // Eliminar el personaje de la base de datos
                dbHelper.eliminarPersonaje(personaje.id)

                // Actualizar la lista del RecyclerView en FragmentJuego
                val fragment = supportFragmentManager.findFragmentByTag("fragmentJuego") as FragmentJuego
                fragment.updateList()

                Toast.makeText(this, "Personaje eliminado", Toast.LENGTH_SHORT).show()

                // Terminar la actividad después de eliminar el personaje
                finish()
            }
            .setNegativeButton("No", null)
            .show()
    }

    private fun mostrarDatePicker(textView: TextView) {
        val calendario = Calendar.getInstance()
        val anio = calendario.get(Calendar.YEAR)
        val mes = calendario.get(Calendar.MONTH)
        val dia = calendario.get(Calendar.DAY_OF_MONTH)

        val datePicker = DatePickerDialog(this, { _, año, mesDelAnio, diaDelMes ->
            val fechaSeleccionada = "$diaDelMes/${mesDelAnio + 1}/$año"
            textView.text = fechaSeleccionada
        }, anio, mes, dia)

        datePicker.show()
    }
}
