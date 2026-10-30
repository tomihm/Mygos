package com.example.mygos.navegacion

import androidx.compose.animation.AnimatedContentTransitionScope
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.mygos.Usuario
import com.example.mygos.repository.CuentaRepository
import com.example.mygos.ui.componentes.Seccion
import com.example.mygos.ui.cuenta.BienvenidaScreen
import com.example.mygos.ui.cuenta.CrearCuentaRuta
import com.example.mygos.ui.cuenta.InicioSesionRuta
import com.example.mygos.ui.eventos.EventoRuta
import com.example.mygos.ui.eventos.ExplorarRuta
import com.example.mygos.ui.eventos.MisEventosRuta
import com.example.mygos.ui.eventos.PublicarEventoRuta
import com.example.mygos.ui.perfil.PerfilRuta
import com.example.mygos.ui.tribus.ChatRuta
import com.example.mygos.ui.tribus.EncuentroRuta
import com.example.mygos.ui.tribus.MisTribusRuta
import com.example.mygos.ui.tribus.MuroRuta
import com.example.mygos.ui.tribus.TribuDetalleRuta
import com.example.mygos.ui.tribus.TribusRuta
import com.example.mygos.ui.tribus.YaTienesTribuRuta

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
    const val CHAT = "chat/{tribuId}"
    const val MURO = "muro/{tribuId}"
    const val ENCUENTRO = "encuentro/{tribuId}"
    const val PERFIL = "perfil"
    const val MIS_EVENTOS = "mis_eventos"
    const val PUBLICAR_EVENTO = "publicar_evento"

    fun evento(id: Int) = "evento/$id"
    fun tribus(id: Int) = "tribus/$id"
    fun tribu(id: Int) = "tribu/$id"
    fun yaTienesTribu(id: Int) = "ya_tienes_tribu/$id"
    fun chat(id: Int) = "chat/$id"
    fun muro(id: Int) = "muro/$id"
    fun encuentro(id: Int) = "encuentro/$id"
}

@Composable
fun MygosNavHost(navController: NavHostController = rememberNavController()) {
    // Al avanzar la pantalla nueva entra desde la derecha; al volver sale hacia la derecha
    NavHost(
        navController = navController,
        startDestination = Rutas.BIENVENIDA,
        enterTransition = {
            slideIntoContainer(AnimatedContentTransitionScope.SlideDirection.Start, tween(300)) + fadeIn(tween(300))
        },
        exitTransition = { fadeOut(tween(200)) },
        popEnterTransition = { fadeIn(tween(300)) },
        popExitTransition = {
            slideOutOfContainer(AnimatedContentTransitionScope.SlideDirection.End, tween(300)) + fadeOut(tween(300))
        }
    ) {

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
                    onIrASeccion = { irASeccion(navController, it) }
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
                    onUnido = { navController.navigate(Rutas.yaTienesTribu(it.id)) },
                    onChat = { navController.navigate(Rutas.chat(id)) },
                    onMuro = { navController.navigate(Rutas.muro(id)) }
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
                    // el chat reemplaza a esta pantalla: al volver del chat no se ve otra vez la celebracion
                    onSaludar = {
                        navController.navigate(Rutas.chat(id)) {
                            popUpTo(Rutas.YA_TIENES_TRIBU) { inclusive = true }
                        }
                    },
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
                    onIrASeccion = { irASeccion(navController, it) }
                )
            }
        }

        composable(
            route = Rutas.CHAT,
            arguments = listOf(navArgument("tribuId") { type = NavType.IntType })
        ) { entrada ->
            val id = entrada.arguments?.getInt("tribuId") ?: 0
            ConSesion(navController) { usuario ->
                ChatRuta(
                    tribuId = id,
                    usuario = usuario,
                    onVolver = { navController.popBackStack() },
                    onModoEncuentro = { navController.navigate(Rutas.encuentro(id)) },
                    onVerMuro = { navController.navigate(Rutas.muro(id)) }
                )
            }
        }

        composable(
            route = Rutas.MURO,
            arguments = listOf(navArgument("tribuId") { type = NavType.IntType })
        ) { entrada ->
            val id = entrada.arguments?.getInt("tribuId") ?: 0
            ConSesion(navController) { usuario ->
                MuroRuta(tribuId = id, usuario = usuario, onVolver = { navController.popBackStack() })
            }
        }

        composable(
            route = Rutas.ENCUENTRO,
            arguments = listOf(navArgument("tribuId") { type = NavType.IntType })
        ) { entrada ->
            val id = entrada.arguments?.getInt("tribuId") ?: 0
            ConSesion(navController) { usuario ->
                EncuentroRuta(tribuId = id, usuario = usuario, onVolver = { navController.popBackStack() })
            }
        }

        composable(Rutas.PERFIL) {
            ConSesion(navController) { usuario ->
                PerfilRuta(
                    usuario = usuario,
                    onIrASeccion = { irASeccion(navController, it) },
                    onMisEventos = { navController.navigate(Rutas.MIS_EVENTOS) },
                    onPublicar = { navController.navigate(Rutas.PUBLICAR_EVENTO) },
                    // borra toda la pila: con "atras" no se puede volver a una pantalla de la sesion cerrada
                    onSesionCerrada = {
                        navController.navigate(Rutas.BIENVENIDA) {
                            popUpTo(0) { inclusive = true }
                        }
                    }
                )
            }
        }

        composable(Rutas.MIS_EVENTOS) {
            ConSesion(navController) { usuario ->
                MisEventosRuta(
                    usuario = usuario,
                    onVolver = { navController.popBackStack() },
                    onVerEvento = { navController.navigate(Rutas.evento(it.id)) },
                    onPublicar = { navController.navigate(Rutas.PUBLICAR_EVENTO) }
                )
            }
        }

        composable(Rutas.PUBLICAR_EVENTO) {
            ConSesion(navController) { usuario ->
                PublicarEventoRuta(
                    usuario = usuario,
                    onVolver = { navController.popBackStack() },
                    // al publicar se cierra el formulario y se muestra la lista con el evento nuevo
                    onPublicado = {
                        navController.navigate(Rutas.MIS_EVENTOS) {
                            popUpTo(Rutas.PUBLICAR_EVENTO) { inclusive = true }
                            launchSingleTop = true
                        }
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

// Explorar queda siempre al fondo de la pila: desde Mis tribus o Perfil, "atras" vuelve a Explorar.
// launchSingleTop evita apilar la misma pantalla dos veces si se toca otra vez
private fun irASeccion(navController: NavHostController, seccion: Seccion) {
    when (seccion) {
        Seccion.EVENTOS -> navController.navigate(Rutas.EXPLORAR) {
            popUpTo(Rutas.EXPLORAR) { inclusive = true }
            launchSingleTop = true
        }
        Seccion.TRIBUS -> navController.navigate(Rutas.MIS_TRIBUS) {
            popUpTo(Rutas.EXPLORAR)
            launchSingleTop = true
        }
        Seccion.PERFIL -> navController.navigate(Rutas.PERFIL) {
            popUpTo(Rutas.EXPLORAR)
            launchSingleTop = true
        }
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
