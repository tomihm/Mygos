package com.example.mygos

import androidx.compose.ui.graphics.Color
import com.example.mygos.ui.theme.Azul
import com.example.mygos.ui.theme.AzulClaro
import com.example.mygos.ui.theme.AzulGris
import com.example.mygos.ui.theme.Dorado
import com.example.mygos.ui.theme.Ladrillo
import com.example.mygos.ui.theme.Morado
import com.example.mygos.ui.theme.Rosado
import com.example.mygos.ui.theme.Turquesa
import com.example.mygos.ui.theme.VerdeBosque

data class Usuario(val nombre: String, val iniciales: String, val color: Color)

data class Cuenta(val correo: String, val clave: String, val usuario: Usuario)

data class Evento(
    val titulo: String,
    val organizador: Usuario,
    val hora: String,
    val mes: String,
    val dia: String,
    val categoria: String,
    val foto: Int,
    val tribus: Int,
    val yaTienesTribu: Boolean,
    val asistentes: List<Usuario>,
    val fecha: String,
    val horario: String,
    val lugar: String,
    val detalleLugar: String,
    val descripcion: String,
    val buscanGrupo: Int
)

data class Tribu(
    val evento: String,
    val nombre: String,
    val integrantes: List<Usuario>,
    val cupos: Int,
    val intereses: List<String>,
    val plan: String,
    val afinidad: Int
)

val valeM = Usuario("Vale_M", "VM", Rosado)
val nicoT = Usuario("Nico.T", "NT", Azul)
val camiR = Usuario("Cami_R", "CR", Morado)
val anaL = Usuario("Ana.L", "AL", Dorado)
val sofiB = Usuario("Sofi.B", "SB", Turquesa)
val rodriF = Usuario("Rodri.F", "RF", Ladrillo)
val daniK = Usuario("Dani.K", "DK", AzulGris)
val juanP = Usuario("Juan.P", "JP", VerdeBosque)

val productoraSonora = Usuario("Productora Sonora", "PS", AzulClaro)
val cineclubNorte = Usuario("Cineclub Norte", "CN", Morado)
val ludotecaCentral = Usuario("Ludoteca Central", "LC", VerdeBosque)

val tribus = listOf(
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
