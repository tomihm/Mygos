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
    val id: Int,
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
