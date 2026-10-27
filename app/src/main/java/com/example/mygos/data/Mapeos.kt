package com.example.mygos.data

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import com.example.mygos.Cuenta
import com.example.mygos.Evento
import com.example.mygos.R
import com.example.mygos.Tribu
import com.example.mygos.Usuario

// Convierten entre las filas de la base de datos (Entity) y los modelos que usan las pantallas

// En la base se guarda el nombre de la foto y no R.drawable, porque esos numeros cambian al compilar
private val fotos = mapOf(
    "evento_jazz" to R.drawable.evento_jazz,
    "evento_cine" to R.drawable.evento_cine,
    "evento_juegos" to R.drawable.evento_juegos,
    "evento_vinilos" to R.drawable.evento_vinilos
)

fun UsuarioEntity.aModelo() = Usuario(id, nombre, iniciales, Color(color))

fun Usuario.aEntidad() = UsuarioEntity(id, nombre, iniciales, color.toArgb())

fun CuentaConUsuario.aModelo() = Cuenta(cuenta.correo, cuenta.clave, usuario.aModelo())

fun TribuConIntegrantes.aModelo() = Tribu(
    id = tribu.id,
    eventoId = tribu.eventoId,
    nombre = tribu.nombre,
    integrantes = integrantes.map { it.aModelo() },
    cupos = tribu.cupos,
    intereses = tribu.intereses,
    plan = tribu.plan,
    afinidad = tribu.afinidad
)

fun Tribu.aEntidad() = TribuEntity(id, eventoId, nombre, cupos, intereses, plan, afinidad)

// usuarioId es quien inicio sesion, para saber si ya tiene tribu en el evento
fun EventoCompleto.aModelo(usuarioId: Int?) = Evento(
    id = evento.id,
    titulo = evento.titulo,
    organizador = organizador.aModelo(),
    hora = evento.hora,
    mes = evento.mes,
    dia = evento.dia,
    categoria = evento.categoria,
    foto = fotos[evento.foto] ?: R.drawable.evento_jazz,
    tribus = tribus.size,
    yaTienesTribu = tribus.any { tribu -> tribu.integrantes.any { it.id == usuarioId } },
    asistentes = asistentes.map { it.aModelo() },
    fecha = evento.fecha,
    horario = evento.horario,
    lugar = evento.lugar,
    detalleLugar = evento.detalleLugar,
    descripcion = evento.descripcion,
    buscanGrupo = evento.buscanGrupo
)

fun Evento.aEntidad() = EventoEntity(
    id = id,
    titulo = titulo,
    organizadorId = organizador.id,
    hora = hora,
    mes = mes,
    dia = dia,
    categoria = categoria,
    foto = fotos.entries.first { it.value == foto }.key,
    fecha = fecha,
    horario = horario,
    lugar = lugar,
    detalleLugar = detalleLugar,
    descripcion = descripcion,
    buscanGrupo = buscanGrupo
)
