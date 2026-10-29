package com.example.mygos.data

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

// Un DAO dice que consultas se pueden hacer. Room escribe el codigo que las ejecuta
@Dao
interface CuentaDao {
    // Las 3 primeras cuentas son las de prueba que se precargan
    @Query("SELECT * FROM cuentas ORDER BY id LIMIT 3")
    fun cuentasDePrueba(): Flow<List<CuentaEntity>>

    @Query("SELECT * FROM cuentas WHERE correo = :correo")
    suspend fun buscar(correo: String): CuentaEntity?

    @Query("SELECT COUNT(*) FROM cuentas")
    suspend fun contarCuentas(): Int

    // Devuelve el id que Room le dio a la fila nueva
    @Insert
    suspend fun insertar(cuenta: CuentaEntity): Long

    @Insert
    suspend fun insertarCuentas(cuentas: List<CuentaEntity>)
}
