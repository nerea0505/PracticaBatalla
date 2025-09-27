package com.nerea.practica4

class Batalla(private var atacante: Personaje, private var defensor: Personaje) {

    fun realizarAtaque() {
        // El atacante le resta vida al defensor
        defensor.vida -= atacante.ataque
        if (defensor.vida < 0) defensor.vida = 0

        // El defensor le resta vida al atacante
        atacante.vida -= defensor.ataque
        if (atacante.vida < 0) atacante.vida = 0
    }

    // Método para obtener la vida del atacante
    fun obtenerVidaAtacante(): Int {
        return atacante.vida
    }

    // Método para obtener la vida del defensor
    fun obtenerVidaDefensor(): Int {
        return defensor.vida
    }

    // Método para verificar si alguno de los personajes ha perdido
    fun verificarDerrota(): String {
        return when {
            atacante.vida == 0 -> "${atacante.nombre} ha sido derrotado!"
            defensor.vida == 0 -> "${defensor.nombre} ha sido derrotado!"
            else -> "¡La batalla continúa!"
        }
    }

    // Método para obtener los personajes involucrados en la batalla
    fun obtenerPersonajes(): Pair<Personaje, Personaje> {
        return Pair(atacante, defensor)
    }
}
