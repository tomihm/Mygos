package com.example.mygos.data

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverter
import androidx.room.TypeConverters

@Database(
    entities = [
        UsuarioEntity::class,
        CuentaEntity::class,
        EventoEntity::class,
        TribuEntity::class,
        EventoAsistenteRef::class,
        TribuIntegranteRef::class
    ],
    version = 1,
    exportSchema = false
)
@TypeConverters(Convertidores::class)
abstract class MygosDatabase : RoomDatabase() {
    abstract fun cuentaDao(): CuentaDao
    abstract fun eventoDao(): EventoDao
    abstract fun tribuDao(): TribuDao
}

// SQLite no guarda listas, asi que los intereses se guardan como un texto separado por |
class Convertidores {
    @TypeConverter
    fun deLista(lista: List<String>): String = lista.joinToString("|")

    @TypeConverter
    fun aLista(texto: String): List<String> = if (texto.isEmpty()) emptyList() else texto.split("|")
}
