package com.example.mygos.data

import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.Index
import androidx.room.Junction
import androidx.room.PrimaryKey
import androidx.room.Relation

// Cada @Entity es una tabla de la base de datos

@Entity(tableName = "usuarios")
data class UsuarioEntity(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val nombre: String,
    val iniciales: String,
    val color: Int
)

@Entity(tableName = "cuentas")
data class CuentaEntity(
    @PrimaryKey val correo: String,
    val clave: String,
    val usuarioId: Int
)

@Entity(tableName = "eventos")
data class EventoEntity(
    @PrimaryKey val id: Int,
    val titulo: String,
    val organizadorId: Int,
    val hora: String,
    val mes: String,
    val dia: String,
    val categoria: String,
    val foto: String,
    val fecha: String,
    val horario: String,
    val lugar: String,
    val detalleLugar: String,
    val descripcion: String,
    val buscanGrupo: Int
)

@Entity(tableName = "tribus")
data class TribuEntity(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val eventoId: Int,
    val nombre: String,
    val cupos: Int,
    val intereses: List<String>,
    val plan: String,
    val afinidad: Int
)

// Tablas intermedias para las relaciones muchos a muchos

@Entity(
    tableName = "evento_asistentes",
    primaryKeys = ["eventoId", "usuarioId"],
    indices = [Index("usuarioId")]
)
data class EventoAsistenteRef(val eventoId: Int, val usuarioId: Int)

@Entity(
    tableName = "tribu_integrantes",
    primaryKeys = ["tribuId", "usuarioId"],
    indices = [Index("usuarioId")]
)
data class TribuIntegranteRef(val tribuId: Int, val usuarioId: Int)

// Clases que juntan una fila con sus filas relacionadas, Room las arma solo

data class CuentaConUsuario(
    @Embedded val cuenta: CuentaEntity,
    @Relation(parentColumn = "usuarioId", entityColumn = "id")
    val usuario: UsuarioEntity
)

data class TribuConIntegrantes(
    @Embedded val tribu: TribuEntity,
    @Relation(
        parentColumn = "id",
        entityColumn = "id",
        associateBy = Junction(TribuIntegranteRef::class, parentColumn = "tribuId", entityColumn = "usuarioId")
    )
    val integrantes: List<UsuarioEntity>
)

data class EventoCompleto(
    @Embedded val evento: EventoEntity,
    @Relation(parentColumn = "organizadorId", entityColumn = "id")
    val organizador: UsuarioEntity,
    @Relation(
        parentColumn = "id",
        entityColumn = "id",
        associateBy = Junction(EventoAsistenteRef::class, parentColumn = "eventoId", entityColumn = "usuarioId")
    )
    val asistentes: List<UsuarioEntity>,
    @Relation(entity = TribuEntity::class, parentColumn = "id", entityColumn = "eventoId")
    val tribus: List<TribuConIntegrantes>
)
