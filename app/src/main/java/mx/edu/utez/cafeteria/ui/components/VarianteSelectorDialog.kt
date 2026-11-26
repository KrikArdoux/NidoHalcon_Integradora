package mx.edu.utez.cafeteria.ui.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import mx.edu.utez.cafeteria.data.local.PlatilloConVariantes
import mx.edu.utez.cafeteria.data.local.entity.VarianteEntity
import mx.edu.utez.cafeteria.ui.theme.BorderLight
import mx.edu.utez.cafeteria.ui.theme.CardSurface
import mx.edu.utez.cafeteria.ui.theme.EmeraldAccent
import mx.edu.utez.cafeteria.ui.theme.NavyPrimary
import mx.edu.utez.cafeteria.ui.theme.TextMuted

@Composable
fun VarianteSelectorDialog(
    platillo: PlatilloConVariantes,
    cantidadEnCarrito: (VarianteEntity) -> Int,
    onAgregar: (VarianteEntity) -> Unit,
    onDismiss: () -> Unit
) {
    val p = platillo.platillo
    AlertDialog(
        onDismissRequest = onDismiss,
        containerColor = CardSurface,
        title = { Text(p.nombre, color = NavyPrimary, fontWeight = FontWeight.Bold) },
        text = {
            Column(Modifier.heightIn(max = 420.dp).verticalScroll(rememberScrollState())) {
                platillo.variantes.filter { it.activo }.forEach { v ->
                    val agotada = v.stock <= 0
                    val enCarrito = cantidadEnCarrito(v)
                    Row(
                        Modifier.fillMaxWidth().padding(vertical = 8.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column(Modifier.weight(1f), verticalArrangement = Arrangement.spacedBy(2.dp)) {
                            Text(
                                v.nombreVariante,
                                color = if (agotada) TextMuted else NavyPrimary,
                                fontWeight = FontWeight.SemiBold
                            )
                            if (!v.detalle.isNullOrBlank()) Text(v.detalle, color = TextMuted, fontSize = 12.sp)
                            Text(
                                formatoPrecio(p.precioBase + v.precioExtra),
                                color = if (agotada) TextMuted else EmeraldAccent,
                                fontWeight = FontWeight.Bold
                            )
                            if (agotada) AgotadoBadge() else Text("Stock: ${v.stock}", color = TextMuted, fontSize = 11.sp)
                        }
                        if (!agotada) {
                            Button(
                                onClick = { onAgregar(v) },
                                enabled = enCarrito < v.stock,
                                contentPadding = PaddingValues(horizontal = 12.dp, vertical = 6.dp),
                                colors = ButtonDefaults.buttonColors(containerColor = EmeraldAccent, contentColor = Color.White)
                            ) {
                                Icon(Icons.Default.Add, contentDescription = "Agregar")
                                if (enCarrito > 0) Text(" $enCarrito")
                            }
                        }
                    }
                    HorizontalDivider(color = BorderLight)
                }
            }
        },
        confirmButton = { TextButton(onClick = onDismiss) { Text("Listo", color = NavyPrimary) } }
    )
}
