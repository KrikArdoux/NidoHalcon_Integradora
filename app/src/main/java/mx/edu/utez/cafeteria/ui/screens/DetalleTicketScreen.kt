package mx.edu.utez.cafeteria.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import mx.edu.utez.cafeteria.ui.components.AppTopBar
import mx.edu.utez.cafeteria.ui.components.TicketItemRow
import mx.edu.utez.cafeteria.ui.components.formatoPrecio
import mx.edu.utez.cafeteria.ui.theme.BackgroundLight
import mx.edu.utez.cafeteria.ui.theme.BorderLight
import mx.edu.utez.cafeteria.ui.theme.CardSurface
import mx.edu.utez.cafeteria.ui.theme.EmeraldAccent
import mx.edu.utez.cafeteria.ui.theme.NavyPrimary
import mx.edu.utez.cafeteria.ui.theme.TextMuted
import mx.edu.utez.cafeteria.ui.viewmodels.HistorialViewModel

@Composable
fun DetalleTicketScreen(
    viewModel: HistorialViewModel,
    numeroPedido: Int,
    esNuevo: Boolean,
    onVolver: () -> Unit,
    onNuevoPedido: () -> Unit
) {
    val estado by viewModel.detalle.collectAsState()

    LaunchedEffect(numeroPedido) { viewModel.cargarTicket(numeroPedido) }

    Scaffold(
        containerColor = BackgroundLight,
        topBar = { AppTopBar("Ticket #$numeroPedido", onVolver) }
    ) { padding ->
        val t = estado.ticket
        when {
            estado.cargando -> Box(Modifier.padding(padding).fillMaxSize(), contentAlignment = Alignment.Center) {
                CircularProgressIndicator(color = NavyPrimary)
            }
            t == null -> Box(Modifier.padding(padding).fillMaxSize(), contentAlignment = Alignment.Center) {
                Text("Ticket no encontrado", color = TextMuted)
            }
            else -> Column(
                Modifier.padding(padding).verticalScroll(rememberScrollState()).padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                if (esNuevo) {
                    Text(
                        "Pedido registrado correctamente",
                        color = Color.White,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.fillMaxWidth()
                            .background(EmeraldAccent, RoundedCornerShape(12.dp))
                            .padding(14.dp)
                    )
                }
                Card(
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(containerColor = CardSurface)
                ) {
                    Column(Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(4.dp)) {
                        Text("El Nido del Halcón", color = NavyPrimary, fontWeight = FontWeight.Bold, fontSize = 20.sp)
                        Text("Pedido #${t.ticket.numeroPedido}", color = EmeraldAccent, fontWeight = FontWeight.Bold)
                        Text("Fecha: ${t.ticket.fechaHora}", color = TextMuted, fontSize = 13.sp)
                        Text("Cajero: ${t.ticket.nombreCajero} (${t.ticket.matriculaCajero})", color = TextMuted, fontSize = 13.sp)
                        Text("Pago: ${t.ticket.metodoPago}", color = TextMuted, fontSize = 13.sp)
                        HorizontalDivider(Modifier.padding(vertical = 8.dp), color = BorderLight)
                        t.detalles.forEach { d ->
                            TicketItemRow(d.nombrePlatillo, d.nombreVariante, d.cantidad, d.precioUnitario, d.importe)
                            HorizontalDivider(color = BorderLight)
                        }
                        Row(
                            Modifier.fillMaxWidth().padding(top = 8.dp),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text("TOTAL", color = NavyPrimary, fontWeight = FontWeight.Bold, fontSize = 18.sp)
                            Text(formatoPrecio(t.ticket.precioTotal), color = EmeraldAccent, fontWeight = FontWeight.Bold, fontSize = 20.sp)
                        }
                    }
                }
                if (esNuevo) {
                    Button(
                        onClick = onNuevoPedido,
                        modifier = Modifier.fillMaxWidth().height(50.dp),
                        shape = RoundedCornerShape(14.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = NavyPrimary, contentColor = Color.White)
                    ) { Text("Nuevo pedido") }
                } else {
                    Text("Ticket inmutable · solo lectura", color = TextMuted, fontSize = 12.sp)
                }
            }
        }
    }
}
