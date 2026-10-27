package com.example.mygos.repository

import com.example.mygos.Evento
import com.example.mygos.Tribu
import com.example.mygos.Usuario
import com.example.mygos.anaL
import com.example.mygos.camiR
import com.example.mygos.daniK
import com.example.mygos.juanP
import com.example.mygos.nicoT
import com.example.mygos.rodriF
import com.example.mygos.sofiB
import com.example.mygos.valeM

// Igual que las cuentas, las tribus viven en memoria mientras la app esta abierta
object TribuRepository {

    private val tribus = mutableListOf(
        Tribu(
            "Festival de Jazz en el Parque", "Tribu Jazz & Picnic",
            listOf(nicoT, anaL, camiR, juanP, sofiB), 7,
            listOf("Jazz", "Picnic", "Fotografía"), "picnic 16:30 en Puerta norte", 92
        ),
        Tribu(
            "Festival de Jazz en el Parque", "Tribu Primera Fila",
            listOf(rodriF, daniK, valeM), 6,
            listOf("Jazz", "Baile"), "16:45 frente al escenario", 85
        ),
        Tribu(
            "Festival de Jazz en el Parque", "Tribu After",
            listOf(sofiB, juanP), 5,
            listOf("Cerveza artesanal", "Conversar"), "después del show en el bar de la esquina", 74
        ),
        Tribu(
            "Cine bajo las estrellas", "Tribu Mantas y Cabritas",
            listOf(camiR, sofiB, rodriF), 6,
            listOf("Cine", "Clásicos"), "20:00 en el estacionamiento", 88
        ),
        Tribu(
            "Noche de juegos de mesa", "Tribu Estrategia",
            listOf(daniK, sofiB), 5,
            listOf("Juegos de mesa", "Catan"), "19:00 en la entrada", 81
        ),
        Tribu(
            "Escucha de vinilos", "Tribu Lado B",
            listOf(nicoT, anaL, rodriF), 6,
            listOf("Vinilos", "Rock", "Soul"), "19:45 en la puerta de la disquería", 90
        )
    )

    fun tribusDe(evento: Evento): List<Tribu> {
        return tribus.filter { it.evento == evento.titulo }
    }

    // Cambia la tribu en la lista por una copia que incluye al usuario.
    // Devuelve false si la tribu ya no tiene cupos
    fun unirse(tribu: Tribu, usuario: Usuario): Boolean {
        val posicion = tribus.indexOfFirst { it.evento == tribu.evento && it.nombre == tribu.nombre }
        val actual = tribus[posicion]
        if (usuario in actual.integrantes) return true
        if (actual.integrantes.size >= actual.cupos) return false
        tribus[posicion] = actual.copy(integrantes = actual.integrantes + usuario)
        return true
    }
}
