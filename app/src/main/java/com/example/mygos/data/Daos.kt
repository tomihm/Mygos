package com.example.mygos.data

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import kotlinx.coroutines.flow.Flow

// Un DAO dice que consultas se pueden hacer. Room escribe el codigo que las ejecuta

@Dao
interface CuentaDao {
    // Las 3 primeras cuentas son las de prueba que se precargan
    @Transaction
    @Query("SELECT * FROM cuentas ORDER BY rowid LIMIT 3")
    fun cuentasDePrueba(): Flow<List<CuentaConUsuario>>

    @Transaction
    @Query("SELECT * FROM cuentas WHERE correo = :correo")
    suspend fun buscar(correo: String): CuentaConUsuario?

    @Query("SELECT COUNT(*) FROM cuentas")
    suspend fun contarCuentas(): Int

    @Query("SELECT COUNT(*) FROM usuarios")
    suspend fun contarUsuarios(): Int

    @Insert
    suspend fun insertarUsuario(usuario: UsuarioEntity): Long

    @Insert
    suspend fun insertarUsuarios(usuarios: List<UsuarioEntity>)

    @Insert
    suspend fun insertarCuentas(cuentas: List<CuentaEntity>)

    // Crea el usuario y su cuenta juntos: si algo falla no queda ninguno de los dos
    @Transaction
    suspend fun crear(usuario: UsuarioEntity, correo: String, clave: String): Int {
        val id = insertarUsuario(usuario).toInt()
        insertarCuentas(listOf(CuentaEntity(correo, clave, id)))
        return id
    }
}

@Dao
interface EventoDao {
    @Transaction
    @Query("SELECT * FROM eventos ORDER BY id")
    fun todos(): Flow<List<EventoCompleto>>

    @Transaction
    @Query("SELECT * FROM eventos WHERE id = :id")
    fun porId(id: Int): Flow<EventoCompleto?>

    @Insert
    suspend fun insertarEventos(eventos: List<EventoEntity>)

    @Insert
    suspend fun insertarAsistentes(asistentes: List<EventoAsistenteRef>)
}

@Dao
interface TribuDao {
    @Transaction
    @Query("SELECT * FROM tribus WHERE eventoId = :eventoId ORDER BY id")
    fun deEvento(eventoId: Int): Flow<List<TribuConIntegrantes>>

    @Transaction
    @Query("SELECT * FROM tribus WHERE id = :id")
    suspend fun porId(id: Int): TribuConIntegrantes?

    @Insert
    suspend fun insertarTribus(tribus: List<TribuEntity>)

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertarIntegrantes(integrantes: List<TribuIntegranteRef>)
}
