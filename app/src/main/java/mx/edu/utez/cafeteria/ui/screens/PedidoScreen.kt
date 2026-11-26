package mx.edu.utez.cafeteria.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Remove
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import mx.edu.utez.cafeteria.data.repository.ItemCarrito
import mx.edu.utez.cafeteria.ui.components.AppTopBar
import mx.edu.utez.cafeteria.ui.components.ChipCategoria
import mx.edu.utez.cafeteria.ui.components.TicketItemRow
import mx.edu.utez.cafeteria.ui.components.formatoPrecio
import mx.edu.utez.cafeteria.ui.theme.AgotadoText
import mx.edu.utez.cafeteria.ui.theme.BackgroundLight
import mx.edu.utez.cafeteria.ui.theme.CardSurface
import mx.edu.utez.cafeteria.ui.theme.EmeraldAccent
import mx.edu.utez.cafeteria.ui.theme.NavyPrimary
import mx.edu.utez.cafeteria.ui.theme.TextMuted
import mx.edu.utez.cafeteria.ui.viewmodels.METODOS_PAGO
import mx.edu.utez.cafeteria.ui.viewmodels.PedidoViewModel

@Composable
fun PedidoScreen(
    pedidoViewModel: PedidoViewModel,
    onVolver: () -> Unit,
    onPedidoConfirmado: (Int) -> Unit
) {
    val state by pedidoViewModel.state.collectAsState()
    var porQuitar by remember { mutableStateOf<ItemCarrito?>(null) }

    LaunchedEffect(state.ticketCreado) {
        state.ticketCreado?.let {
            pedidoViewModel.consumirTicket()
            onPedidoConfirmado(it)
        }
    }

    Scaffold(
        containerColor = BackgroundLight,
        topBar = {
            AppTopBar("Pedido actual", onVolver) {
                if (state.items.isNotEmpty()) TextButton(onClick = pedidoViewModel::vaciar) { Text("Vaciar", color = Color.White) }
            }
        },
        bottomBar = {
            Surface(shadowElevation = 8.dp, color = CardSurface) {
                Column(Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(10.dp)) {
                    Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                        Text("Total", color = NavyPrimary, fontWeight = FontWeight.Bold, fontSize = 18.sp)
                        Text(formatoPrecio(state.total), color = EmeraldAccent, fontWeight = FontWeight.Bold, fontSize = 20.sp)
                    }
                    Button(
                        onClick = pedidoViewModel::confirmar,
                        enabled = state.items.isNotEmpty() && !state.procesando,
                        modifier = Modifier.fillMaxWidth().height(50.dp),
                        shape = RoundedCornerShape(14.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = EmeraldAccent, contentColor = Color.White)
                    ) {
                        if (state.procesando) CircularProgressIndicator(color = Color.White, strokeWidth = 2.dp, modifier = Modifier.height(20.dp))
                        else Text("Confirmar pedido", fontWeight = FontWeight.Bold)
                    }
                }
            }
        }
    ) { padding ->
        if (state.items.isEmpty()) {
            Box(Modifier.padding(padding).fillMaxSize(), contentAlignment = Alignment.Center) {
                Text("Aún no hay productos en el pedido", color = TextMuted)
            }
        } else {
            LazyColumn(
                Modifier.padding(padding),
                contentPadding = PaddingValues(16.dp),
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                items(state.items, key = { it.key }) { item ->
                    Card(
                        shape = RoundedCornerShape(14.dp),
                        colors = CardDefaults.cardColors(containerColor = CardSurface)
                    ) {
                        TicketItemRow(
                            nombrePlatillo = item.nombrePlatillo,
                            nombreVariante = item.nombreVariante,
                            cantidad = item.cantidad,
                            precioUnitario = item.precioUnitario,
                            importe = item.precioUnitario * item.cantidad,
                            modifier = Modifier.padding(horizontal = 12.dp),
                            trailing = {
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    IconButton(onClick = { pedidoViewModel.decrementar(item.key) }) {
                                        Icon(Icons.Default.Remove, "Menos", tint = NavyPrimary)
                                    }
                                    Text("${item.cantidad}", color = NavyPrimary, fontWeight = FontWeight.Bold)
                                    IconButton(
                                        onClick = { pedidoViewModel.incrementar(item.key) },
                                        enabled = item.cantidad < item.stockDisponible
                                    ) { Icon(Icons.Default.Add, "Más", tint = NavyPrimary) }
                                    Box(Modifier.weight(1f))
                                    IconButton(onClick = { porQuitar = item }) {
                                        Icon(Icons.Default.Delete, "Quitar", tint = AgotadoText)
                                    }
                                }
                            }
                        )
                    }
                }
                item {
                    Text("Método de pago", color = NavyPrimary, fontWeight = FontWeight.Bold, modifier = Modifier.padding(top = 8.dp))
                }
                item {
                    Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                        METODOS_PAGO.forEach { m ->
                            ChipCategoria(m, state.metodoPago == m) { pedidoViewModel.seleccionarMetodo(m) }
                        }
                    }
                }
                state.error?.let { msg ->
                    item { Text(msg, color = AgotadoText, fontSize = 13.sp) }
                }
            }
        }
    }

    porQuitar?.let { item ->
        AlertDialog(
            onDismissRequest = { porQuitar = null },
            containerColor = CardSurface,
            title = { Text("Quitar del pedido", color = AgotadoText, fontWeight = FontWeight.Bold) },
            text = {
                Text(
                    "¿Estás seguro de que deseas quitar '${item.nombrePlatillo} · ${item.nombreVariante}' del pedido?",
                    color = NavyPrimary
                )
            },
            confirmButton = {
                Button(
                    onClick = {
                        pedidoViewModel.quitar(item.key)
                        porQuitar = null
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = AgotadoText, contentColor = Color.White)
                ) { Text("Quitar") }
            },
            dismissButton = { TextButton(onClick = { porQuitar = null }) { Text("Cancelar", color = NavyPrimary) } }
        )
    }
}