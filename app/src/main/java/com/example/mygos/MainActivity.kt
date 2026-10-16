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
        // barras del sistema con iconos claros porque la app es oscura
        enableEdgeToEdge(
            statusBarStyle = SystemBarStyle.dark(Color.TRANSPARENT),
            navigationBarStyle = SystemBarStyle.dark(Color.TRANSPARENT)
        )
        setContent {
            MygosTheme {
                // null = pantalla de inicio, si hay usuario se muestra Explorar
                var usuario by remember { mutableStateOf<Usuario?>(null) }

                val actual = usuario
                if (actual == null) {
                    LoginScreen(onEntrar = { usuario = it })
                } else {
                    BackHandler { usuario = null }
                    ExplorarScreen(usuario = actual)
                }
            }
        }
    }
}
