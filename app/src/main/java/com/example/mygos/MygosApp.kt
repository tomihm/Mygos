package com.example.mygos

import android.app.Application
import androidx.room.Room
import com.example.mygos.data.MygosDatabase
import com.example.mygos.data.precargar
import com.example.mygos.repository.CuentaRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking

// Se crea una sola vez al abrir la app, antes que cualquier pantalla
class MygosApp : Application() {

    // Corrutinas que duran lo mismo que la app, no se cancelan al cambiar de pantalla
    private val scope = CoroutineScope(SupervisorJob() + Dispatchers.IO)

    override fun onCreate() {
        super.onCreate()
        val db = Room.databaseBuilder(this, MygosDatabase::class.java, "mygos.db").build()

        CuentaRepository.iniciar(db.cuentaDao(), getSharedPreferences("sesion", MODE_PRIVATE))

        // Se espera a que termine antes de abrir la primera pantalla, asi la navegacion ya sabe si hay sesion
        runBlocking { CuentaRepository.restaurarSesion() }

        scope.launch { precargar(db) }
    }
}
