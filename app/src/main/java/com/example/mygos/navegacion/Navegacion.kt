package com.example.mygos.navegacion

import android.widget.Toast
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.mygos.BienvenidaScreen
import com.example.mygos.CrearCuentaScreen
import com.example.mygos.EventoScreen
import com.example.mygos.ExplorarScreen
import com.example.mygos.InicioSesionScreen
import com.example.mygos.TribusScreen
import com.example.mygos.Usuario
import com.example.mygos.repository.CuentaRepository
import com.example.mygos.repository.EventoRepository

object Rutas {
    const val BIENVENIDA = "bienvenida"
    const val INICIAR_SESION = "iniciar_sesion"
    const val CREAR_CUENTA = "crear_cuenta"
    const val EXPLORAR = "explorar"
    const val EVENTO = "evento/{eventoId}"
    const val TRIBUS = "tribus/{eventoId}"

    fun evento(id: Int) = "evento/$id"
    fun tribus(id: Int) = "tribus/$id"
}

@Composable
fun MygosNavHost(navController: NavHostController = rememberNavController()) {
    val context = LocalContext.current

    NavHost(navController = navController, startDestination = Rutas.BIENVENIDA) {

        composable(Rutas.BIENVENIDA) {
            BienvenidaScreen(
                onIniciarSesion = { navController.navigate(Rutas.INICIAR_SESION) },
                onCrearCuenta = { navController.navigate(Rutas.CREAR_CUENTA) }
            )
        }

        composable(Rutas.INICIAR_SESION) {
            InicioSesionScreen(
                onVolver = { navController.popBackStack() },
                onEntrar = { irAExplorar(navController) },
                onIrACrearCuenta = {
                    navController.navigate(Rutas.CREAR_CUENTA) {
                        popUpTo(Rutas.BIENVENIDA)
                    }
                }
            )
        }

        composable(Rutas.CREAR_CUENTA) {
            CrearCuentaScreen(
                onVolver = { navController.popBackStack() },
                onCuentaCreada = { irAExplorar(navController) },
                onIrAIniciarSesion = {
                    navController.navigate(Rutas.INICIAR_SESION) {
                        popUpTo(Rutas.BIENVENIDA)
                    }
                }
            )
        }

        composable(Rutas.EXPLORAR) {
            ConSesion(navController) { usuario ->
                ExplorarScreen(
                    usuario = usuario,
                    onVerEvento = { navController.navigate(Rutas.evento(it.id)) }
                )
            }
        }

        composable(
            route = Rutas.EVENTO,
            arguments = listOf(navArgument("eventoId") { type = NavType.IntType })
        ) { entrada ->
            val id = entrada.arguments?.getInt("eventoId") ?: 0
            val evento = EventoRepository.buscarEvento(id) ?: return@composable
            EventoScreen(
                evento = evento,
                onVolver = { navController.popBackStack() },
                onBuscarTribu = { navController.navigate(Rutas.tribus(evento.id)) }
            )
        }

        composable(
            route = Rutas.TRIBUS,
            arguments = listOf(navArgument("eventoId") { type = NavType.IntType })
        ) { entrada ->
            val id = entrada.arguments?.getInt("eventoId") ?: 0
            val evento = EventoRepository.buscarEvento(id) ?: return@composable
            ConSesion(navController) { usuario ->
                TribusScreen(
                    evento = evento,
                    usuario = usuario,
                    onVolver = { navController.popBackStack() },
                    onUnirse = {
                        Toast.makeText(context, "Te uniste a ${it.nombre}", Toast.LENGTH_SHORT).show()
                        navController.popBackStack(Rutas.EXPLORAR, inclusive = false)
                    }
                )
            }
        }
    }
}

// Explorar queda como primera pantalla: con "atrás" se sale de la app en vez de volver al login
private fun irAExplorar(navController: NavHostController) {
    navController.navigate(Rutas.EXPLORAR) {
        popUpTo(Rutas.BIENVENIDA) { inclusive = true }
    }
}

// Si Android cerro la app y se perdio la sesion, vuelve a la bienvenida en vez de fallar
@Composable
private fun ConSesion(navController: NavHostController, contenido: @Composable (Usuario) -> Unit) {
    val usuario = CuentaRepository.usuarioActual
    if (usuario != null) {
        contenido(usuario)
    } else {
        LaunchedEffect(Unit) {
            navController.navigate(Rutas.BIENVENIDA) {
                popUpTo(0) { inclusive = true }
            }
        }
    }
}
