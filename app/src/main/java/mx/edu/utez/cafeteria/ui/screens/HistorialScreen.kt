package mx.edu.utez.cafeteria.ui.screens

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import mx.edu.utez.cafeteria.ui.components.AppTopBar
import mx.edu.utez.cafeteria.ui.components.formatoPrecio
import mx.edu.utez.cafeteria.ui.theme.BackgroundLight
import mx.edu.utez.cafeteria.ui.theme.CardSurface
import mx.edu.utez.cafeteria.ui.theme.EmeraldAccent
import mx.edu.utez.cafeteria.ui.theme.NavyPrimary
import mx.edu.utez.cafeteria.ui.theme.TextMuted
import mx.edu.utez.cafeteria.ui.viewmodels.HistorialViewModel

@Composable
fun HistorialScreen(
    viewModel: HistorialViewModel,
    onVolver: () -> Unit,
    onAbrirTicket: (Int) -> Unit
) {
    val tickets by viewModel.tickets.collectAsState()

    Scaffold(containerColor = BackgroundLight, topBar = { AppTopBar("Historial de pedidos", onVolver) }) { padding ->
        if (tickets.isEmpty()) {
            Box(Modifier.padding(padding).fillMaxSize(), contentAlignment = Alignment.Center) {
                Text("Aún no hay tickets registrados", color = TextMuted)
            }
        } else {
            LazyColumn(
                Modifier.padding(padding),
                contentPadding = PaddingValues(16.dp),
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                items(tickets, key = { it.numeroPedido }) { t ->
                    Card(
                        modifier = Modifier.fillMaxWidth().clip(RoundedCornerShape(14.dp)).clickable { onAbrirTicket(t.numeroPedido) },
                        shape = RoundedCornerShape(14.dp),
                        colors = CardDefaults.cardColors(containerColor = CardSurface)
                    ) {
                        Row(
                            Modifier.padding(16.dp).fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Column {
                                Text("Pedido #${t.numeroPedido}", color = NavyPrimary, fontWeight = FontWeight.Bold)
                                Text(t.fechaHora, color = TextMuted, fontSize = 12.sp)
                                Text(t.metodoPago, color = TextMuted, fontSize = 12.sp)
                            }
                            Text(formatoPrecio(t.precioTotal), color = EmeraldAccent, fontWeight = FontWeight.Bold, fontSize = 18.sp)
                        }
                    }
                }
            }
        }
    }
}
