package com.example.mygos.repository

import com.example.mygos.Evento
import com.example.mygos.R
import com.example.mygos.anaL
import com.example.mygos.camiR
import com.example.mygos.cineclubNorte
import com.example.mygos.daniK
import com.example.mygos.ludotecaCentral
import com.example.mygos.nicoT
import com.example.mygos.productoraSonora
import com.example.mygos.rodriF
import com.example.mygos.sofiB

object EventoRepository {

    private val categorias = listOf("Todos", "Música", "Cine", "Juegos")

    private val eventos = listOf(
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

    fun obtenerCategorias(): List<String> = categorias

    fun obtenerEventos(): List<Evento> = eventos

    fun eventosDeCategoria(categoria: String): List<Evento> {
        if (categoria == "Todos") return eventos
        return eventos.filter { it.categoria == categoria }
    }
}
