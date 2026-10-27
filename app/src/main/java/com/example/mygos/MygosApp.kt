package com.example.mygos

import android.app.Application
import androidx.room.Room
import com.example.mygos.data.MygosDatabase
import com.example.mygos.data.precargar
import com.example.mygos.repository.CuentaRepository
import com.example.mygos.repository.EventoRepository
import com.example.mygos.repository.TribuRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch

// Se crea una sola vez al abrir la app, antes que cualquier pantalla
class MygosApp : Application() {

    // Corrutinas que duran lo mismo que la app, no se cancelan al cambiar de pantalla
    private val scope = CoroutineScope(SupervisorJob() + Dispatchers.IO)

    override fun onCreate() {
        super.onCreate()
        val db = Room.databaseBuilder(this, MygosDatabase::class.java, "mygos.db").build()

        CuentaRepository.iniciar(db.cuentaDao())
        EventoRepository.iniciar(db.eventoDao())
        TribuRepository.iniciar(db.tribuDao())

        scope.launch { precargar(db) }
    }
}
