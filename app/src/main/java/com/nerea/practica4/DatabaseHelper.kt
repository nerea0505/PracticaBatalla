package com.nerea.practica4

import android.annotation.SuppressLint
import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.util.Log
class DatabaseHelper(context: Context) : SQLiteOpenHelper(context, "JuegoDB", null, 2) { // Versión 2 para actualizar la BD

    override fun onCreate(db: SQLiteDatabase?) {
        Log.d("DB", "Creando la base de datos...")

        val crearTablaPersonajes = """CREATE TABLE IF NOT EXISTS personajes (
                id INTEGER PRIMARY KEY AUTOINCREMENT,
                nombre TEXT NOT NULL,
                vida INTEGER NOT NULL,
                vidaMaxima INTEGER NOT NULL,
                ataque INTEGER NOT NULL,
                fechaNacimiento TEXT NOT NULL,
                muertes INTEGER DEFAULT 0)""" // Asegúrate de agregar la columna 'muertes'
        db?.execSQL(crearTablaPersonajes)


        // Crear la tabla resultados_batalla
        val crearTablaResultados = """CREATE TABLE IF NOT EXISTS resultados_batalla (
                id INTEGER PRIMARY KEY AUTOINCREMENT,
                atacante TEXT NOT NULL,
                defensor TEXT NOT NULL,
                resultado TEXT NOT NULL,
                fecha TEXT NOT NULL)"""
        db?.execSQL(crearTablaResultados)

        // Crear la tabla estadisticas
        val crearTablaEstadisticas = """CREATE TABLE IF NOT EXISTS estadisticas (
                id INTEGER PRIMARY KEY AUTOINCREMENT,
                nombre TEXT NOT NULL,
                muertes INTEGER NOT NULL)"""
        db?.execSQL(crearTablaEstadisticas)

        Log.d("DB", "Base de datos creada correctamente")
    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {

    }

    // 🔹 Añadir personaje a la BD
    fun anadirPersonaje(personaje: Personaje): Long {
        val db = writableDatabase
        return try {
            val values = ContentValues().apply {
                put("nombre", personaje.nombre)
                put("vida", personaje.vida)
                put("vidaMaxima", personaje.vidaMaxima)
                put("ataque", personaje.ataque)
                put("fechaNacimiento", personaje.fechaNacimiento)
                put("muertes", 0)  // Añadir valor de muertes (por defecto 0)
            }
            val id = db.insert("personajes", null, values)
            Log.d("DB", "Personaje agregado con ID: $id")
            id
        } finally {
            db.close()
        }
    }



    fun almacenarResultadoBatalla(atacante: Personaje, defensor: Personaje,ganador: String, fecha: String) {
        // Llamamos a la función calcularGanador para obtener el nombre del ganador
        val ganador = calcularGanador(atacante, defensor)

        // Si el atacante gana, el defensor pierde
        val perdedor = if (ganador == atacante.nombre) defensor.nombre else atacante.nombre

        // Actualizamos las muertes del perdedor
        actualizarMuertes(perdedor)

        val db = writableDatabase
        val values = ContentValues().apply {
            put("atacante", atacante.nombre)
            put("defensor", defensor.nombre)
            put("resultado", ganador)
            put("fecha", fecha)
        }

        db.insert("resultados_batalla", null, values)
        db.close()
    }

    // Función para incrementar las muertes de un personaje
    fun actualizarMuertes(personaje: String) {
        val db = writableDatabase

        // Consultamos si el personaje ya tiene muertes registradas
        val cursor = db.rawQuery("SELECT muertes FROM personajes WHERE nombre = ?", arrayOf(personaje))

        if (cursor.moveToFirst()) {
            // Si ya tiene muertes, las incrementamos
            val muertes = cursor.getInt(0) + 1
            val values = ContentValues().apply {
                put("muertes", muertes)
            }
            db.update("personajes", values, "nombre = ?", arrayOf(personaje))
        } else {
            // Si no tiene muertes, creamos un registro inicial con 1 muerte
            val values = ContentValues().apply {
                put("nombre", personaje)
                put("muertes", 1)
            }
            db.insert("personajes", null, values)
        }
        cursor.close()
        db.close()
    }


    @SuppressLint("Range")
    fun obtenerResultadosBatalla(): List<MatchResult> {
        val resultados = mutableListOf<MatchResult>()
        val db = readableDatabase
        val query = "SELECT * FROM resultados_batalla"
        val cursor = db.rawQuery(query, null)

        if (cursor.moveToFirst()) {
            do {
                val nombreAtacante = cursor.getString(cursor.getColumnIndex("atacante"))
                val nombreDefensor = cursor.getString(cursor.getColumnIndex("defensor"))
                val resultado = cursor.getString(cursor.getColumnIndex("resultado"))
                val fecha = cursor.getString(cursor.getColumnIndex("fecha"))

                // Determinar qué imagen poner según el ganador
                val resultImage: Int
                val batallaTexto: String

                if (resultado == nombreAtacante) {
                    resultImage = R.drawable.ic_victory  // El atacante ganó
                    batallaTexto = "$nombreAtacante vs $nombreDefensor"  // Mostrar ataque -> victoria
                } else {
                    resultImage = R.drawable.ic_defeat  // El atacante perdió
                    batallaTexto = "$nombreDefensor vs $nombreAtacante"  // Mostrar defensa -> derrota
                }

                resultados.add(MatchResult(resultImage, resultado, batallaTexto, fecha))
            } while (cursor.moveToNext())
        }
        cursor.close()
        db.close()

        return resultados
    }

    // Ejemplo de cómo determinar el ganador
    fun calcularGanador(atacante: Personaje, defensor: Personaje): String {
        // Aquí puedes agregar la lógica para determinar quién ganó
        // Por ejemplo, basado en el daño que cada uno puede hacer
        return if (atacante.ataque > defensor.vida) {
            atacante.nombre // El atacante gana
        } else {
            defensor.nombre // El defensor gana
        }
    }


    // 🔹 Obtener personajes con vida > 0
    fun obtenerPersonajesConVida(): List<Personaje> {
        val personajes = mutableListOf<Personaje>()
        val db = readableDatabase
        val cursor = db.rawQuery("SELECT * FROM personajes WHERE vida > 0", null)
        while (cursor.moveToNext()) {
            personajes.add(
                Personaje(
                    id = cursor.getInt(0),
                    nombre = cursor.getString(1),
                    vida = cursor.getInt(2),
                    vidaMaxima = cursor.getInt(3),
                    ataque = cursor.getInt(4),
                    fechaNacimiento = cursor.getString(5)
                )
            )
        }
        cursor.close()
        db.close()
        return personajes
    }

    // 🔹 Modificar un personaje existente
    fun modificarPersonaje(personaje: Personaje): Int {
        val db = writableDatabase
        val values = ContentValues().apply {
            put("nombre", personaje.nombre)
            put("vida", personaje.vida)
            put("vidaMaxima", personaje.vidaMaxima)
            put("ataque", personaje.ataque)
            put("fechaNacimiento", personaje.fechaNacimiento)
        }
        val filasActualizadas = db.update("personajes", values, "id = ?", arrayOf(personaje.id.toString()))
        db.close()
        return filasActualizadas
    }

    // 🔹 Reiniciar la vida de todos los personajes al máximo
    fun reiniciarVidaPersonajes() {
        val db = writableDatabase
        db.execSQL("UPDATE personajes SET vida = vidaMaxima")
        db.close()
    }

    // 🔹 Buscar personaje por nombre
    fun obtenerPersonajePorNombre(nombre: String): Personaje? {
        val db = readableDatabase
        val cursor = db.rawQuery("SELECT * FROM personajes WHERE nombre = ?", arrayOf(nombre))
        var personaje: Personaje? = null
        if (cursor.moveToFirst()) {
            personaje = Personaje(
                id = cursor.getInt(0),
                nombre = cursor.getString(1),
                vida = cursor.getInt(2),
                vidaMaxima = cursor.getInt(3),
                ataque = cursor.getInt(4),
                fechaNacimiento = cursor.getString(5)
            )
        }
        cursor.close()
        db.close()
        return personaje
    }

    // 🔹 Eliminar personaje por ID
    fun eliminarPersonaje(id: Int): Int {
        val db = writableDatabase
        val filasEliminadas = db.delete("personajes", "id = ?", arrayOf(id.toString()))
        db.close()
        return filasEliminadas
    }


    // Función para obtener las muertes de un personaje por su nombre
    fun obtenerMuertesDePersonaje(nombre: String): Int? {
        val db = readableDatabase
        val query = "SELECT muertes FROM personajes WHERE nombre = ?"
        val cursor = db.rawQuery(query, arrayOf(nombre))

        var muertes: Int? = null
        if (cursor.moveToFirst()) {
            val indexMuertes = cursor.getColumnIndex("muertes")
            if (indexMuertes != -1) {
                muertes = cursor.getInt(indexMuertes)
            }
        }
        cursor.close()
        db.close()

        return muertes
    }


    // 🔹 Resetear las muertes del personaje
    fun resetearMuertesPersonaje(nombre: String) {
        val db = writableDatabase
        val contentValues = ContentValues()
        contentValues.put("muertes", 0) // Establecemos las muertes a 0

        val rowsAffected = db.update("personajes", contentValues, "nombre = ?", arrayOf(nombre))

        db.close()
    }
}
