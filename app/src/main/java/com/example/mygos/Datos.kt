package com.example.mygos

import androidx.compose.ui.graphics.Color
import com.example.mygos.ui.theme.Azul
import com.example.mygos.ui.theme.AzulGris
import com.example.mygos.ui.theme.Dorado
import com.example.mygos.ui.theme.Ladrillo
import com.example.mygos.ui.theme.Morado
import com.example.mygos.ui.theme.Rosado
import com.example.mygos.ui.theme.Turquesa

data class Usuario(val nombre: String, val iniciales: String, val color: Color)

data class Evento(
    val titulo: String,
    val organizador: String,
    val hora: String,
    val mes: String,
    val dia: String,
    val categoria: String,
    val foto: Int,
    val tribus: Int,
    val yaTienesTribu: Boolean,
    val asistentes: List<Usuario>
)

val valeM = Usuario("Vale_M", "VM", Rosado)
val nicoT = Usuario("Nico.T", "NT", Azul)
val camiR = Usuario("Cami_R", "CR", Morado)
val anaL = Usuario("Ana.L", "AL", Dorado)
val sofiB = Usuario("Sofi.B", "SB", Turquesa)
val rodriF = Usuario("Rodri.F", "RF", Ladrillo)
val daniK = Usuario("Dani.K", "DK", AzulGris)

val usuariosDePrueba = listOf(valeM, nicoT, camiR)

val categorias = listOf("Todos", "Música", "Cine", "Juegos")

val eventos = listOf(
    Evento(
        "Festival de Jazz en el Parque", "Productora Sonora", "17:00", "HOY", "24",
        "Música", R.drawable.evento_jazz, 3, false, listOf(nicoT, anaL, camiR)
    ),
    Evento(
        "Cine bajo las estrellas", "Cineclub Norte", "20:30", "OCT", "26",
        "Cine", R.drawable.evento_cine, 1, true, listOf(camiR, sofiB, rodriF)
    ),
    Evento(
        "Noche de juegos de mesa", "Ludoteca Central", "19:00", "NOV", "03",
        "Juegos", R.drawable.evento_juegos, 1, false, listOf(daniK, sofiB)
    ),
    Evento(
        "Escucha de vinilos", "Productora Sonora", "20:00", "NOV", "07",
        "Música", R.drawable.evento_vinilos, 1, false, listOf(nicoT, anaL, rodriF)
    )
)
