package com.example.mygos.navegacion

import android.content.Context
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
import com.example.mygos.CrearCuentaRuta
import com.example.mygos.EventoRuta
import com.example.mygos.ExplorarRuta
import com.example.mygos.InicioSesionRuta
import com.example.mygos.MisTribusRuta
import com.example.mygos.Seccion
import com.example.mygos.TribuDetalleRuta
import com.example.mygos.TribusRuta
import com.example.mygos.Usuario
import com.example.mygos.YaTienesTribuRuta
import com.example.mygos.repository.CuentaRepository

object Rutas {
    const val BIENVENIDA = "bienvenida"
    const val INICIAR_SESION = "iniciar_sesion"
    const val CREAR_CUENTA = "crear_cuenta"
    const val EXPLORAR = "explorar"
    const val EVENTO = "evento/{eventoId}"
    const val TRIBUS = "tribus/{eventoId}"
    const val TRIBU = "tribu/{tribuId}"
    const val YA_TIENES_TRIBU = "ya_tienes_tribu/{tribuId}"
    const val MIS_TRIBUS = "mis_tribus"

    fun evento(id: Int) = "evento/$id"
    fun tribus(id: Int) = "tribus/$id"
    fun tribu(id: Int) = "tribu/$id"
    fun yaTienesTribu(id: Int) = "ya_tienes_tribu/$id"
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
            InicioSesionRuta(
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
            CrearCuentaRuta(
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
                ExplorarRuta(
                    usuario = usuario,
                    onVerEvento = { navController.navigate(Rutas.evento(it.id)) },
                    onIrASeccion = { irASeccion(navController, it, context) }
                )
            }
        }

        composable(
            route = Rutas.EVENTO,
            arguments = listOf(navArgument("eventoId") { type = NavType.IntType })
        ) { entrada ->
            val id = entrada.arguments?.getInt("eventoId") ?: 0
            EventoRuta(
                eventoId = id,
                onVolver = { navController.popBackStack() },
                onBuscarTribu = { navController.navigate(Rutas.tribus(id)) }
            )
        }

        composable(
            route = Rutas.TRIBUS,
            arguments = listOf(navArgument("eventoId") { type = NavType.IntType })
        ) { entrada ->
            val id = entrada.arguments?.getInt("eventoId") ?: 0
            ConSesion(navController) { usuario ->
                TribusRuta(
                    eventoId = id,
                    usuario = usuario,
                    onVolver = { navController.popBackStack() },
                    onUnirse = { navController.navigate(Rutas.yaTienesTribu(it.id)) },
                    onVerTribu = { navController.navigate(Rutas.tribu(it.id)) }
                )
            }
        }

        composable(
            route = Rutas.TRIBU,
            arguments = listOf(navArgument("tribuId") { type = NavType.IntType })
        ) { entrada ->
            val id = entrada.arguments?.getInt("tribuId") ?: 0
            ConSesion(navController) { usuario ->
                TribuDetalleRuta(
                    tribuId = id,
                    usuario = usuario,
                    onVolver = { navController.popBackStack() },
                    onUnido = { navController.navigate(Rutas.yaTienesTribu(it.id)) }
                )
            }
        }

        composable(
            route = Rutas.YA_TIENES_TRIBU,
            arguments = listOf(navArgument("tribuId") { type = NavType.IntType })
        ) { entrada ->
            val id = entrada.arguments?.getInt("tribuId") ?: 0
            ConSesion(navController) { usuario ->
                YaTienesTribuRuta(
                    tribuId = id,
                    usuario = usuario,
                    onVerMisTribus = { irASeccion(navController, Seccion.TRIBUS, context) },
                    onVerOtrasTribus = {
                        // vuelve al carrusel de tribus del evento; si no se llego desde ahi, solo vuelve atras
                        if (!navController.popBackStack(Rutas.TRIBUS, inclusive = false)) {
                            navController.popBackStack()
                        }
                    }
                )
            }
        }

        composable(Rutas.MIS_TRIBUS) {
            ConSesion(navController) { usuario ->
                MisTribusRuta(
                    usuario = usuario,
                    onVerTribu = { navController.navigate(Rutas.tribu(it.id)) },
                    onIrASeccion = { irASeccion(navController, it, context) }
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

// Explorar queda siempre al fondo de la pila: desde Mis tribus, "atras" vuelve a Explorar.
// launchSingleTop evita apilar la misma pantalla dos veces si se toca otra vez
private fun irASeccion(navController: NavHostController, seccion: Seccion, context: Context) {
    when (seccion) {
        Seccion.EVENTOS -> navController.navigate(Rutas.EXPLORAR) {
            popUpTo(Rutas.EXPLORAR) { inclusive = true }
            launchSingleTop = true
        }
        Seccion.TRIBUS -> navController.navigate(Rutas.MIS_TRIBUS) {
            popUpTo(Rutas.EXPLORAR)
            launchSingleTop = true
        }
        Seccion.PERFIL -> Toast.makeText(context, "Próximamente", Toast.LENGTH_SHORT).show()
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
