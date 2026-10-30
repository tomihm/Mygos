package com.example.mygos

import androidx.compose.ui.graphics.Color
import com.example.mygos.R
import com.example.mygos.ui.theme.Azul
import com.example.mygos.ui.theme.AzulClaro
import com.example.mygos.ui.theme.AzulGris
import com.example.mygos.ui.theme.Dorado
import com.example.mygos.ui.theme.Ladrillo
import com.example.mygos.ui.theme.Morado
import com.example.mygos.ui.theme.Rosado
import com.example.mygos.ui.theme.Turquesa
import com.example.mygos.ui.theme.VerdeBosque

// foto es una imagen de res/drawable; null si el usuario no tiene (se muestran sus iniciales)
data class Usuario(val id: Int, val nombre: String, val iniciales: String, val color: Color, val foto: Int? = null)

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
    // Coordenadas del lugar, para medir la distancia en el modo encuentro
    val latitud: Double,
    val longitud: Double,
    val detalleLugar: String,
    val descripcion: String,
    val buscanGrupo: Int
)

data class Tribu(
    val id: Int,
    val eventoId: Int,
    val nombre: String,
    val integrantes: List<Usuario>,
    val cupos: Int,
    val intereses: List<String>,
    val plan: String,
    val afinidad: Int
)

// foto es la direccion (Uri) de la foto tomada con la camara. Las de ejemplo no tienen foto y se pintan con el color del autor
data class Publicacion(val id: Int, val tribuId: Int, val autor: Usuario, val foto: String?)

data class Mensaje(val id: Int, val tribuId: Int, val autor: Usuario, val texto: String)

val valeM = Usuario(1, "Vale_M", "VM", Rosado, R.drawable.perfil_vale)
val nicoT = Usuario(2, "Nico.T", "NT", Azul, R.drawable.perfil_nico)
val camiR = Usuario(3, "Cami_R", "CR", Morado, R.drawable.perfil_cami)

// Estos no tienen cuenta. Parten en 101 para no chocar con los id que Room da a las cuentas nuevas (4, 5, 6...)
val anaL = Usuario(101, "Ana.L", "AL", Dorado, R.drawable.perfil_ana)
val sofiB = Usuario(102, "Sofi.B", "SB", Turquesa, R.drawable.perfil_sofi)
val rodriF = Usuario(103, "Rodri.F", "RF", Ladrillo, R.drawable.perfil_rodri)
val daniK = Usuario(104, "Dani.K", "DK", AzulGris, R.drawable.perfil_dani)
val juanP = Usuario(105, "Juan.P", "JP", VerdeBosque, R.drawable.perfil_juan)

val productoraSonora = Usuario(106, "Productora Sonora", "PS", AzulClaro)
val cineclubNorte = Usuario(107, "Cineclub Norte", "CN", Morado)
val ludotecaCentral = Usuario(108, "Ludoteca Central", "LC", VerdeBosque)
