package com.example.mygos.data

import androidx.room.withTransaction
import com.example.mygos.Cuenta
import com.example.mygos.Evento
import com.example.mygos.R
import com.example.mygos.Tribu
import com.example.mygos.anaL
import com.example.mygos.camiR
import com.example.mygos.cineclubNorte
import com.example.mygos.daniK
import com.example.mygos.juanP
import com.example.mygos.ludotecaCentral
import com.example.mygos.nicoT
import com.example.mygos.productoraSonora
import com.example.mygos.rodriF
import com.example.mygos.sofiB
import com.example.mygos.valeM

// Datos de ejemplo que se guardan en la base de datos la primera vez que se abre la app.
// Las previews de las pantallas tambien los usan

val usuariosIniciales = listOf(
    valeM, nicoT, camiR, anaL, sofiB, rodriF, daniK, juanP,
    productoraSonora, cineclubNorte, ludotecaCentral
)

val cuentasIniciales = listOf(
    Cuenta("vale@mygos.cl", "1234", valeM),
    Cuenta("nico@mygos.cl", "1234", nicoT),
    Cuenta("cami@mygos.cl", "1234", camiR)
)

val eventosIniciales = listOf(
    Evento(
        id = 1,
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
        id = 2,
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
        id = 3,
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
        id = 4,
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

val tribusIniciales = listOf(
    Tribu(
        1, 1, "Tribu Jazz & Picnic",
        listOf(nicoT, anaL, camiR, juanP, sofiB), 7,
        listOf("Jazz", "Picnic", "Fotografía"), "picnic 16:30 en Puerta norte", 92
    ),
    Tribu(
        2, 1, "Tribu Primera Fila",
        listOf(rodriF, daniK, valeM), 6,
        listOf("Jazz", "Baile"), "16:45 frente al escenario", 85
    ),
    Tribu(
        3, 1, "Tribu After",
        listOf(sofiB, juanP), 5,
        listOf("Cerveza artesanal", "Conversar"), "después del show en el bar de la esquina", 74
    ),
    Tribu(
        4, 2, "Tribu Mantas y Cabritas",
        listOf(camiR, sofiB, rodriF), 6,
        listOf("Cine", "Clásicos"), "20:00 en el estacionamiento", 88
    ),
    Tribu(
        5, 3, "Tribu Estrategia",
        listOf(daniK, sofiB), 5,
        listOf("Juegos de mesa", "Catan"), "19:00 en la entrada", 81
    ),
    Tribu(
        6, 4, "Tribu Lado B",
        listOf(nicoT, anaL, rodriF), 6,
        listOf("Vinilos", "Rock", "Soul"), "19:45 en la puerta de la disquería", 90
    )
)

suspend fun precargar(db: MygosDatabase) {
    if (db.cuentaDao().contarUsuarios() > 0) return

    // withTransaction: se guarda todo junto o nada
    db.withTransaction {
        db.cuentaDao().insertarUsuarios(usuariosIniciales.map { it.aEntidad() })
        db.cuentaDao().insertarCuentas(cuentasIniciales.map { CuentaEntity(it.correo, it.clave, it.usuario.id) })
        db.eventoDao().insertarEventos(eventosIniciales.map { it.aEntidad() })
        db.eventoDao().insertarAsistentes(
            eventosIniciales.flatMap { evento -> evento.asistentes.map { EventoAsistenteRef(evento.id, it.id) } }
        )
        db.tribuDao().insertarTribus(tribusIniciales.map { it.aEntidad() })
        db.tribuDao().insertarIntegrantes(
            tribusIniciales.flatMap { tribu -> tribu.integrantes.map { TribuIntegranteRef(tribu.id, it.id) } }
        )
    }
}
