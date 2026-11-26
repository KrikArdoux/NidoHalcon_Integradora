package mx.edu.utez.cafeteria.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Restaurant
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import mx.edu.utez.cafeteria.data.local.PlatilloConVariantes
import mx.edu.utez.cafeteria.ui.theme.AgotadoBg
import mx.edu.utez.cafeteria.ui.theme.AgotadoText
import mx.edu.utez.cafeteria.ui.theme.BackgroundLight
import mx.edu.utez.cafeteria.ui.theme.CardSurface
import mx.edu.utez.cafeteria.ui.theme.EmeraldAccent
import mx.edu.utez.cafeteria.ui.theme.NavyPrimary
import mx.edu.utez.cafeteria.ui.theme.TextMuted

@Composable
fun AgotadoBadge(modifier: Modifier = Modifier) {
    Surface(modifier = modifier, shape = RoundedCornerShape(8.dp), color = AgotadoBg) {
        Text(
            "AGOTADO HOY",
            modifier = Modifier.padding(horizontal = 8.dp, vertical = 3.dp),
            color = AgotadoText,
            fontSize = 10.sp,
            fontWeight = FontWeight.Bold
        )
    }
}

@Composable
fun PlatilloCard(
    platillo: PlatilloConVariantes,
    onClick: () -> Unit,
    onEditar: () -> Unit,
    onDesactivar: () -> Unit,
    modifier: Modifier = Modifier
) {
    val p = platillo.platillo
    val activas = platillo.variantes.filter { it.activo }
    val disponibles = activas.filter { it.stock > 0 }
    val agotado = activas.isNotEmpty() && disponibles.isEmpty()
    val desde = (if (disponibles.isNotEmpty()) disponibles else activas)
        .minOfOrNull { p.precioBase + it.precioExtra } ?: p.precioBase

    Card(
        modifier = modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(16.dp))
            .clickable(enabled = !agotado, onClick = onClick),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = CardSurface),
        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
    ) {
        Row(Modifier.padding(12.dp), verticalAlignment = Alignment.CenterVertically) {
            Box(
                Modifier.size(76.dp).clip(RoundedCornerShape(12.dp)).background(BackgroundLight),
                contentAlignment = Alignment.Center
            ) {
                if (p.imagenUri != null) {
                    AsyncImage(
                        model = p.imagenUri,
                        contentDescription = p.nombre,
                        contentScale = ContentScale.Crop,
                        modifier = Modifier.size(76.dp)
                    )
                } else {
                    Icon(Icons.Default.Restaurant, contentDescription = null, tint = TextMuted)
                }
            }
            Column(Modifier.weight(1f).padding(horizontal = 12.dp), verticalArrangement = Arrangement.spacedBy(3.dp)) {
                Text(p.nombre, color = if (agotado) TextMuted else NavyPrimary, fontWeight = FontWeight.Bold, fontSize = 16.sp)
                if (p.descripcion.isNotBlank()) Text(p.descripcion, color = TextMuted, fontSize = 12.sp)
                Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    Text("Desde ${formatoPrecio(desde)}", color = EmeraldAccent, fontWeight = FontWeight.Bold, fontSize = 14.sp)
                    if (agotado) AgotadoBadge()
                }
                Text("${activas.size} opciones", color = TextMuted, fontSize = 11.sp)
            }
            Column {
                IconButton(onClick = onEditar) { Icon(Icons.Default.Edit, "Editar", tint = NavyPrimary) }
                IconButton(onClick = onDesactivar) { Icon(Icons.Default.Delete, "Desactivar", tint = AgotadoText) }
            }
        }
    }
}
