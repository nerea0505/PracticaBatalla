package com.nerea.practica4

import java.io.Serializable

data class Personaje(val id: Int = 0, val nombre: String, var vida: Int, val vidaMaxima: Int, val ataque: Int, val fechaNacimiento: String
) : Serializable
