package mx.edu.utez.cafeteria.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Logout
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.History
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import mx.edu.utez.cafeteria.data.local.PlatilloConVariantes
import mx.edu.utez.cafeteria.data.local.entity.MenuDelDiaEntity
import mx.edu.utez.cafeteria.data.repository.ItemCarrito
import mx.edu.utez.cafeteria.data.repository.TIPO_MENU
import mx.edu.utez.cafeteria.data.repository.TIPO_VARIANTE
import mx.edu.utez.cafeteria.ui.components.EditarMenuDelDiaDialog
import mx.edu.utez.cafeteria.ui.components.MenuDelDiaBanner
import mx.edu.utez.cafeteria.ui.components.PlatilloCard
import mx.edu.utez.cafeteria.ui.components.VarianteSelectorDialog
import mx.edu.utez.cafeteria.ui.components.formatoPrecio
import mx.edu.utez.cafeteria.ui.theme.AgotadoText
import mx.edu.utez.cafeteria.ui.theme.BackgroundLight
import mx.edu.utez.cafeteria.ui.theme.BorderLight
import mx.edu.utez.cafeteria.ui.theme.CardSurface
import mx.edu.utez.cafeteria.ui.theme.EmeraldAccent
import mx.edu.utez.cafeteria.ui.theme.NavyPrimary
import mx.edu.utez.cafeteria.ui.theme.NavySurfaceVariant
import mx.edu.utez.cafeteria.ui.theme.TextMuted
import mx.edu.utez.cafeteria.ui.viewmodels.MenuViewModel
import mx.edu.utez.cafeteria.ui.viewmodels.PedidoViewModel

@Composable
fun MenuScreen(
    menuViewModel: MenuViewModel,
    pedidoViewModel: PedidoViewModel,
    onNuevoPlatillo: () -> Unit,
    onEditarPlatillo: (Int) -> Unit,
    onVerPedido: () -> Unit,
    onHistorial: () -> Unit,
    onCerrarSesion: () -> Unit
) {
    val ui by menuViewModel.uiState.collectAsState()
    val pedido by pedidoViewModel.state.collectAsState()
    val sesion by menuViewModel.sesion.collectAsState()
    val msgMenu by menuViewModel.mensaje.collectAsState()
    val msgPedido by pedidoViewModel.mensaje.collectAsState()
    val snackbar = remember { SnackbarHostState() }

    LaunchedEffect(msgMenu) {
        msgMenu?.let {
            snackbar.showSnackbar(it)
            menuViewModel.consumirMensaje()
        }
    }
    LaunchedEffect(msgPedido) {
        msgPedido?.let {
            snackbar.showSnackbar(it)
            pedidoViewModel.consumirMensaje()
        }
    }

    var platilloAbierto by remember { mutableStateOf<Int?>(null) }
    var porDesactivar by remember { mutableStateOf<PlatilloConVariantes?>(null) }
    var menuEditando by remember { mutableStateOf<MenuDelDiaEntity?>(null) }
    var confirmarSalida by remember { mutableStateOf(false) }

    Scaffold(
        containerColor = BackgroundLight,
        snackbarHost = { SnackbarHost(snackbar) },
        topBar = { EncabezadoMenu(sesion?.nombre ?: "", onHistorial) { confirmarSalida = true } },
        floatingActionButton = {
            FloatingActionButton(onClick = onNuevoPlatillo, containerColor = NavyPrimary, contentColor = Color.White) {
                Icon(Icons.Default.Add, contentDescription = "Nuevo platillo")
            }
        },
        bottomBar = {
            if (pedido.items.isNotEmpty()) {
                Surface(shadowElevation = 8.dp, color = CardSurface) {
                    Button(
                        onClick = onVerPedido,
                        modifier = Modifier.fillMaxWidth().padding(12.dp),
                        shape = RoundedCornerShape(14.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = EmeraldAccent, contentColor = Color.White)
                    ) {
                        Icon(Icons.Default.ShoppingCart, contentDescription = null)
                        Text("  Ver pedido (${pedido.cantidadTotal}) · ${formatoPrecio(pedido.total)}")
                    }
                }
            }
        }
    ) { padding ->
        LazyColumn(
            modifier = Modifier.padding(padding),
            contentPadding = androidx.compose.foundation.layout.PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            item {
                OutlinedTextField(
                    value = ui.busqueda,
                    onValueChange = menuViewModel::onBusqueda,
                    modifier = Modifier.fillMaxWidth(),
                    placeholder = { Text("Buscar platillo o variante", color = TextMuted) },
                    leadingIcon = { Icon(Icons.Default.Search, null, tint = TextMuted) },
                    trailingIcon = {
                        if (ui.busqueda.isNotEmpty()) {
                            IconButton(onClick = { menuViewModel.onBusqueda("") }) {
                                Icon(Icons.Default.Close, contentDescription = "Limpiar", tint = TextMuted)
                            }
                        }
                    },
                    singleLine = true,
                    shape = RoundedCornerShape(14.dp),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedContainerColor = CardSurface,
                        unfocusedContainerColor = CardSurface,
                        unfocusedBorderColor = BorderLight,
                        focusedBorderColor = NavyPrimary
                    )
                )
            }
            item {
                MenuDelDiaBanner(
                    menu = ui.menuDelDia,
                    dias = ui.dias,
                    diaSeleccionado = ui.diaSeleccionado,
                    onDiaSeleccionado = menuViewModel::onDia,
                    onEditar = { menuEditando = it },
                    onAgregar = { m ->
                        pedidoViewModel.agregar(
                            ItemCarrito(
                                key = "M-${m.idMenuDia}",
                                tipo = TIPO_MENU,
                                idRef = m.idMenuDia,
                                nombrePlatillo = "Menú del Día",
                                nombreVariante = m.diaSemana,
                                precioUnitario = m.precio,
                                cantidad = 1,
                                stockDisponible = m.stock
                            )
                        )
                    }
                )
            }
            if (ui.platillos.isEmpty()) {
                item {
                    Text(
                        "No se encontraron platillos",
                        color = TextMuted,
                        modifier = Modifier.fillMaxWidth().padding(24.dp)
                    )
                }
            }
            items(ui.platillos, key = { it.platillo.idPlatillo }) { p ->
                PlatilloCard(
                    platillo = p,
                    onClick = { platilloAbierto = p.platillo.idPlatillo },
                    onEditar = { onEditarPlatillo(p.platillo.idPlatillo) },
                    onDesactivar = { porDesactivar = p }
                )
            }
            item { Spacer(Modifier.height(72.dp)) }
        }
    }

    val abierto = ui.platillos.firstOrNull { it.platillo.idPlatillo == platilloAbierto }
    if (abierto != null) {
        VarianteSelectorDialog(
            platillo = abierto,
            cantidadEnCarrito = { v -> pedido.items.firstOrNull { it.key == "V-${v.idVariante}" }?.cantidad ?: 0 },
            onAgregar = { v ->
                pedidoViewModel.agregar(
                    ItemCarrito(
                        key = "V-${v.idVariante}",
                        tipo = TIPO_VARIANTE,
                        idRef = v.idVariante,
                        nombrePlatillo = abierto.platillo.nombre,
                        nombreVariante = v.nombreVariante,
                        precioUnitario = abierto.platillo.precioBase + v.precioExtra,
                        cantidad = 1,
                        stockDisponible = v.stock
                    )
                )
            },
            onDismiss = { platilloAbierto = null }
        )
    }

    porDesactivar?.let { p ->
        AlertDialog(
            onDismissRequest = { porDesactivar = null },
            containerColor = CardSurface,
            title = { Text("Eliminar platillo", color = AgotadoText, fontWeight = FontWeight.Bold) },
            text = {
                Text(
                    "¿Estás seguro de que deseas eliminar '${p.platillo.nombre}' del menú?",
                    color = NavyPrimary
                )
            },
            confirmButton = {
                Button(
                    onClick = {
                        menuViewModel.desactivarPlatillo(p.platillo.idPlatillo)
                        porDesactivar = null
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = AgotadoText, contentColor = Color.White)
                ) { Text("Eliminar") }
            },
            dismissButton = { TextButton(onClick = { porDesactivar = null }) { Text("Cancelar", color = NavyPrimary) } }
        )
    }

    menuEditando?.let { m ->
        EditarMenuDelDiaDialog(
            menu = m,
            onGuardar = {
                menuViewModel.actualizarMenuDelDia(it)
                menuEditando = null
            },
            onDismiss = { menuEditando = null }
        )
    }

    if (confirmarSalida) {
        AlertDialog(
            onDismissRequest = { confirmarSalida = false },
            containerColor = CardSurface,
            title = { Text("Cerrar sesión", color = AgotadoText, fontWeight = FontWeight.Bold) },
            text = { Text("¿Estás seguro de que deseas salir de tu sesión actual?", color = NavyPrimary) },
            confirmButton = {
                Button(
                    onClick = {
                        confirmarSalida = false
                        onCerrarSesion()
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = AgotadoText, contentColor = Color.White)
                ) { Text("Cerrar sesión") }
            },
            dismissButton = { TextButton(onClick = { confirmarSalida = false }) { Text("Cancelar", color = NavyPrimary) } }
        )
    }
}

@Composable
private fun EncabezadoMenu(nombreCajero: String, onHistorial: () -> Unit, onCerrarSesion: () -> Unit) {
    val hora = remember { SimpleDateFormat("EEE HH:mm", Locale("es", "MX")).format(Date()) }
    Surface(color = NavyPrimary) {
        Column(Modifier.statusBarsPadding().padding(start = 16.dp, end = 4.dp, top = 8.dp, bottom = 12.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Column(Modifier.weight(1f)) {
                    Text("El Nido del Halcón", color = Color.White, fontSize = 22.sp, fontWeight = FontWeight.Bold)
                    Text("Punto de venta", color = Color.White.copy(alpha = 0.7f), fontSize = 13.sp)
                }
                IconButton(onClick = onHistorial) { Icon(Icons.Default.History, "Historial", tint = Color.White) }
                IconButton(onClick = onCerrarSesion) { Icon(Icons.AutoMirrored.Filled.Logout, "Cerrar sesión", tint = Color.White) }
            }
            Spacer(Modifier.height(8.dp))
            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                Pill("Cajero: $nombreCajero")
                Pill(hora)
            }
        }
    }
}

@Composable
private fun Pill(texto: String) {
    Text(
        texto,
        color = Color.White,
        fontSize = 12.sp,
        modifier = Modifier
            .clip(RoundedCornerShape(50))
            .background(NavySurfaceVariant)
            .padding(horizontal = 12.dp, vertical = 6.dp)
    )
}