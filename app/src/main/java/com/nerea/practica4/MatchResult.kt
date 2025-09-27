package com.nerea.practica4

// MatchResult.kt
data class MatchResult(
    val resultImage: Int,      // Imagen de la victoria, derrota o tablas
    val result: String,        // "Victoria", "Derrota" o "Tablas"
    val characters: String,    // "Personaje1 vs Personaje2"
    val date: String           // Fecha de la batalla
)
