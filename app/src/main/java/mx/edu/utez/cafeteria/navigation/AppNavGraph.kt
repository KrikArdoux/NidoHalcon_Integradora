package mx.edu.utez.cafeteria.navigation

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.produceState
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import kotlinx.coroutines.flow.first
import mx.edu.utez.cafeteria.AppContainer
import mx.edu.utez.cafeteria.AppViewModelFactory
import mx.edu.utez.cafeteria.ui.screens.DetalleTicketScreen
import mx.edu.utez.cafeteria.ui.screens.HistorialScreen
import mx.edu.utez.cafeteria.ui.screens.LoginScreen
import mx.edu.utez.cafeteria.ui.screens.MenuScreen
import mx.edu.utez.cafeteria.ui.screens.PedidoScreen
import mx.edu.utez.cafeteria.ui.screens.PlatilloFormScreen
import mx.edu.utez.cafeteria.ui.viewmodels.HistorialViewModel
import mx.edu.utez.cafeteria.ui.viewmodels.LoginViewModel
import mx.edu.utez.cafeteria.ui.viewmodels.MenuViewModel
import mx.edu.utez.cafeteria.ui.viewmodels.PedidoViewModel

@Composable
fun AppNavGraph(container: AppContainer) {
    val navController = rememberNavController()
    val factory = remember { AppViewModelFactory(container) }

    val loginVm: LoginViewModel = viewModel(factory = factory)
    val menuVm: MenuViewModel = viewModel(factory = factory)
    val pedidoVm: PedidoViewModel = viewModel(factory = factory)
    val historialVm: HistorialViewModel = viewModel(factory = factory)

    val inicio by produceState<String?>(initialValue = null) {
        value = if (container.userPreferences.sesion.first() != null) AppRoutes.MENU else AppRoutes.LOGIN
    }
    val destino = inicio
    if (destino == null) {
        Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) { CircularProgressIndicator() }
        return
    }

    NavHost(navController = navController, startDestination = destino) {
        composable(AppRoutes.LOGIN) {
            LoginScreen(loginVm) {
                navController.navigate(AppRoutes.MENU) { popUpTo(AppRoutes.LOGIN) { inclusive = true } }
            }
        }
        composable(AppRoutes.MENU) {
            MenuScreen(
                menuViewModel = menuVm,
                pedidoViewModel = pedidoVm,
                onNuevoPlatillo = { navController.navigate(AppRoutes.platilloForm()) },
                onEditarPlatillo = { id -> navController.navigate(AppRoutes.platilloForm(id)) },
                onVerPedido = { navController.navigate(AppRoutes.PEDIDO) },
                onHistorial = { navController.navigate(AppRoutes.HISTORIAL) },
                onCerrarSesion = {
                    menuVm.cerrarSesion {
                        pedidoVm.vaciar()
                        loginVm.limpiar()
                        navController.navigate(AppRoutes.LOGIN) {
                            popUpTo(navController.graph.id) { inclusive = true }
                        }
                    }
                }
            )
        }
        composable(
            route = AppRoutes.PLATILLO_FORM_ROUTE,
            arguments = listOf(navArgument(AppRoutes.ARG_ID_PLATILLO) {
                type = NavType.IntType
                defaultValue = -1
            })
        ) { entry ->
            PlatilloFormScreen(
                menuViewModel = menuVm,
                idPlatillo = entry.arguments?.getInt(AppRoutes.ARG_ID_PLATILLO) ?: -1,
                onVolver = { navController.popBackStack() }
            )
        }
        composable(AppRoutes.PEDIDO) {
            PedidoScreen(
                pedidoViewModel = pedidoVm,
                onVolver = { navController.popBackStack() },
                onPedidoConfirmado = { numero ->
                    navController.navigate(AppRoutes.detalle(numero, true)) {
                        popUpTo(AppRoutes.MENU) { inclusive = false }
                    }
                }
            )
        }
        composable(AppRoutes.HISTORIAL) {
            HistorialScreen(
                viewModel = historialVm,
                onVolver = { navController.popBackStack() },
                onAbrirTicket = { numero -> navController.navigate(AppRoutes.detalle(numero)) }
            )
        }
        composable(
            route = AppRoutes.DETALLE_ROUTE,
            arguments = listOf(
                navArgument(AppRoutes.ARG_NUMERO) { type = NavType.IntType },
                navArgument(AppRoutes.ARG_NUEVO) {
                    type = NavType.BoolType
                    defaultValue = false
                }
            )
        ) { entry ->
            DetalleTicketScreen(
                viewModel = historialVm,
                numeroPedido = entry.arguments?.getInt(AppRoutes.ARG_NUMERO) ?: 0,
                esNuevo = entry.arguments?.getBoolean(AppRoutes.ARG_NUEVO) ?: false,
                onVolver = { navController.popBackStack() },
                onNuevoPedido = { navController.popBackStack(AppRoutes.MENU, inclusive = false) }
            )
        }
    }
}
