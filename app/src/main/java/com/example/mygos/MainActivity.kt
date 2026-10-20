package com.example.mygos

import android.graphics.Color
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.SystemBarStyle
import androidx.activity.compose.BackHandler
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import com.example.mygos.ui.theme.MygosTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge(
            statusBarStyle = SystemBarStyle.dark(Color.TRANSPARENT),
            navigationBarStyle = SystemBarStyle.dark(Color.TRANSPARENT)
        )
        setContent {
            MygosTheme {
                var pantalla by remember { mutableStateOf("bienvenida") }
                var usuario by remember { mutableStateOf<Usuario?>(null) }

                BackHandler(enabled = pantalla != "bienvenida") {
                    pantalla = "bienvenida"
                }

                val actual = usuario
                when {
                    pantalla == "iniciar" -> InicioSesionScreen(
                        onVolver = { pantalla = "bienvenida" },
                        onEntrar = {
                            usuario = it
                            pantalla = "explorar"
                        },
                        onIrACrearCuenta = { pantalla = "crear" }
                    )
                    pantalla == "crear" -> CrearCuentaScreen(
                        onVolver = { pantalla = "bienvenida" },
                        onCuentaCreada = {
                            usuario = it
                            pantalla = "explorar"
                        },
                        onIrAIniciarSesion = { pantalla = "iniciar" }
                    )
                    actual == null || pantalla == "bienvenida" -> BienvenidaScreen(
                        onIniciarSesion = { pantalla = "iniciar" },
                        onCrearCuenta = { pantalla = "crear" }
                    )
                    else -> ExplorarScreen(
                        usuario = actual,
                        onVerEvento = {}
                    )
                }
            }
        }
    }
}
