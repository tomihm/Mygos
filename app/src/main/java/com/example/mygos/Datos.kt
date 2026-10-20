package com.example.mygos

import androidx.compose.ui.graphics.Color
import com.example.mygos.ui.theme.Azul
import com.example.mygos.ui.theme.AzulClaro
import com.example.mygos.ui.theme.AzulGris
import com.example.mygos.ui.theme.Dorado
import com.example.mygos.ui.theme.Ladrillo
import com.example.mygos.ui.theme.Morado
import com.example.mygos.ui.theme.NaranjaOscuro
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

// No hay backend, las cuentas viven en memoria mientras la app esta abierta
val cuentas = mutableListOf(
    Cuenta("vale@mygos.cl", "1234", valeM),
    Cuenta("nico@mygos.cl", "1234", nicoT),
    Cuenta("cami@mygos.cl", "1234", camiR)
)

val coloresNuevos = listOf(Turquesa, Ladrillo, Dorado, AzulGris, NaranjaOscuro, VerdeBosque)

fun buscarCuenta(correo: String, clave: String): Usuario? {
    val cuenta = cuentas.find { it.correo == correo.trim().lowercase() && it.clave == clave }
    return cuenta?.usuario
}

fun existeCorreo(correo: String): Boolean {
    return cuentas.any { it.correo == correo.trim().lowercase() }
}

fun crearCuenta(nombre: String, correo: String, clave: String): Usuario {
    val palabras = nombre.trim().split(" ", ".", "_").filter { it.isNotEmpty() }
    val iniciales = if (palabras.size >= 2) {
        "${palabras[0][0]}${palabras[1][0]}"
    } else {
        palabras[0].take(2)
    }
    val color = coloresNuevos[cuentas.size % coloresNuevos.size]
    val usuario = Usuario(nombre.trim(), iniciales.uppercase(), color)
    cuentas.add(Cuenta(correo.trim().lowercase(), clave, usuario))
    return usuario
}

val categorias = listOf("Todos", "Música", "Cine", "Juegos")

val eventos = listOf(
    Evento(
        titulo = "Festival de Jazz en el Parque",
        organizador = productoraSonora,
        hora = "17:00", mes = "HOY", dia = "24",
        categoria = "Música",
        foto = R.drawable.evento_jazz,
        tribus = 3,
        yaTienesTribu = false,
        asistentes = listOf(nicoT, anaL, camiR),
        fecha = "Hoy · sábado 24 de octubre",
        horario = "17:00 – 22:00",
        lugar = "Parque Central (ficticio)",
        detalleLugar = "Escenario principal",
        descripcion = "Una tarde de jazz al aire libre con bandas locales. Trae algo para sentarte en el pasto; habrá foodtrucks.",
        buscanGrupo = 18
    ),
    Evento(
        titulo = "Cine bajo las estrellas",
        organizador = cineclubNorte,
        hora = "20:30", mes = "OCT", dia = "26",
        categoria = "Cine",
        foto = R.drawable.evento_cine,
        tribus = 1,
        yaTienesTribu = true,
        asistentes = listOf(camiR, sofiB, rodriF),
        fecha = "Lunes 26 de octubre",
        horario = "20:30 – 23:00",
        lugar = "Cerro Mirador (ficticio)",
        detalleLugar = "Explanada sur",
        descripcion = "Proyección al aire libre de un clásico del cine. Lleva una manta; hay cabritas a la venta.",
        buscanGrupo = 7
    ),
    Evento(
        titulo = "Noche de juegos de mesa",
        organizador = ludotecaCentral,
        hora = "19:00", mes = "NOV", dia = "03",
        categoria = "Juegos",
        foto = R.drawable.evento_juegos,
        tribus = 1,
        yaTienesTribu = false,
        asistentes = listOf(daniK, sofiB),
        fecha = "Martes 3 de noviembre",
        horario = "19:00 – 23:30",
        lugar = "Ludoteca Central (ficticia)",
        detalleLugar = "Sala 2",
        descripcion = "Mesas con más de 200 juegos y monitores que te enseñan las reglas. No necesitas saber jugar.",
        buscanGrupo = 6
    ),
    Evento(
        titulo = "Escucha de vinilos",
        organizador = productoraSonora,
        hora = "20:00", mes = "NOV", dia = "07",
        categoria = "Música",
        foto = R.drawable.evento_vinilos,
        tribus = 1,
        yaTienesTribu = false,
        asistentes = listOf(nicoT, anaL, rodriF),
        fecha = "Sábado 7 de noviembre",
        horario = "20:00 – 23:00",
        lugar = "Disquería El Surco (ficticia)",
        detalleLugar = "Segundo piso",
        descripcion = "Escuchamos discos completos de principio a fin y después se conversa. Puedes llevar tus vinilos.",
        buscanGrupo = 9
    )
)

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
