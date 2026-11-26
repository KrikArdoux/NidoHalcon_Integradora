package mx.edu.utez.cafeteria.ui.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import mx.edu.utez.cafeteria.data.local.entity.MenuDelDiaEntity
import mx.edu.utez.cafeteria.ui.theme.AgotadoText
import mx.edu.utez.cafeteria.ui.theme.CardSurface
import mx.edu.utez.cafeteria.ui.theme.EmeraldAccent
import mx.edu.utez.cafeteria.ui.theme.EmeraldContainer
import mx.edu.utez.cafeteria.ui.theme.NavyPrimary

@Composable
fun MenuDelDiaBanner(
    menu: MenuDelDiaEntity?,
    dias: List<String>,
    diaSeleccionado: String,
    onDiaSeleccionado: (String) -> Unit,
    onAgregar: (MenuDelDiaEntity) -> Unit,
    onEditar: (MenuDelDiaEntity) -> Unit,
    modifier: Modifier = Modifier
) {
    var expandido by rememberSaveable { mutableStateOf(true) }

    Card(
        modifier = modifier.fillMaxWidth(),
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(containerColor = EmeraldAccent)
    ) {
        Column(Modifier.padding(16.dp)) {
            Row(
                Modifier.fillMaxWidth().clip(RoundedCornerShape(8.dp)).clickable { expandido = !expandido },
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Text("MENÚ DEL DÍA", color = Color.White, fontWeight = FontWeight.Bold, fontSize = 18.sp)
                    Text(
                        "Lunes a Viernes · ${formatoPrecio(menu?.precio ?: 70.0)}",
                        color = Color.White.copy(alpha = 0.85f),
                        fontSize = 13.sp
                    )
                }
                Row(verticalAlignment = Alignment.CenterVertically) {
                    if (menu != null) {
                        IconButton(onClick = { onEditar(menu) }) {
                            Icon(Icons.Default.Edit, contentDescription = "Editar menú del día", tint = Color.White)
                        }
                    }
                    Icon(
                        if (expandido) Icons.Default.KeyboardArrowUp else Icons.Default.KeyboardArrowDown,
                        contentDescription = null,
                        tint = Color.White
                    )
                }
            }
            Spacer(Modifier.height(10.dp))
            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                dias.forEach { dia ->
                    val sel = dia == diaSeleccionado
                    Surface(
                        modifier = Modifier.clip(RoundedCornerShape(50)).clickable { onDiaSeleccionado(dia) },
                        shape = RoundedCornerShape(50),
                        color = if (sel) Color.White else EmeraldContainer
                    ) {
                        Text(
                            dia,
                            modifier = Modifier.padding(horizontal = 14.dp, vertical = 6.dp),
                            color = if (sel) EmeraldAccent else Color.White,
                            fontWeight = FontWeight.Bold,
                            fontSize = 13.sp
                        )
                    }
                }
            }
            AnimatedVisibility(visible = expandido) {
                Column(Modifier.padding(top = 12.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    if (menu == null) {
                        Text("Sin menú registrado para este día", color = Color.White)
                    } else {
                        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                            SeccionMenu("Sopa", menu.sopa, Modifier.weight(1f))
                            SeccionMenu("Plato fuerte", menu.platoFuerte, Modifier.weight(1f))
                        }
                        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                            SeccionMenu("Guarnición", menu.guarnicion, Modifier.weight(1f))
                            SeccionMenu("Frijoles", menu.frijolesPostre, Modifier.weight(1f))
                        }
                        Row(
                            Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            if (menu.stock <= 0) {
                                AgotadoBadge()
                            } else {
                                Text("Disponibles: ${menu.stock}", color = Color.White, fontSize = 13.sp)
                            }
                            Button(
                                onClick = { onAgregar(menu) },
                                enabled = menu.stock > 0,
                                colors = ButtonDefaults.buttonColors(containerColor = NavyPrimary, contentColor = Color.White)
                            ) { Text("Agregar al pedido") }
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun EditarMenuDelDiaDialog(
    menu: MenuDelDiaEntity,
    onGuardar: (MenuDelDiaEntity) -> Unit,
    onDismiss: () -> Unit
) {
    var sopa by remember(menu) { mutableStateOf(menu.sopa) }
    var platoFuerte by remember(menu) { mutableStateOf(menu.platoFuerte) }
    var guarnicion by remember(menu) { mutableStateOf(menu.guarnicion) }
    var frijolesPostre by remember(menu) { mutableStateOf(menu.frijolesPostre) }
    var precio by remember(menu) { mutableStateOf(menu.precio.toString()) }
    var stock by remember(menu) { mutableStateOf(menu.stock.toString()) }
    var error by remember(menu) { mutableStateOf<String?>(null) }

    AlertDialog(
        onDismissRequest = onDismiss,
        containerColor = CardSurface,
        title = {
            Text("Editar menú · ${menu.diaSemana}", color = NavyPrimary, fontWeight = FontWeight.Bold)
        },
        text = {
            Column(
                Modifier.verticalScroll(rememberScrollState()),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                CampoMenu("Sopa", sopa) { sopa = it }
                CampoMenu("Plato fuerte", platoFuerte) { platoFuerte = it }
                CampoMenu("Guarnición", guarnicion) { guarnicion = it }
                CampoMenu("Frijoles / Postre", frijolesPostre) { frijolesPostre = it }
                CampoMenu("Precio", precio, KeyboardType.Decimal) { precio = it }
                CampoMenu("Stock", stock, KeyboardType.Number) { stock = it }
                error?.let { Text(it, color = AgotadoText, fontSize = 13.sp) }
            }
        },
        confirmButton = {
            Button(
                onClick = {
                    val p = precio.replace(',', '.').toDoubleOrNull()
                    val st = stock.toIntOrNull()
                    when {
                        sopa.isBlank() || platoFuerte.isBlank() || guarnicion.isBlank() || frijolesPostre.isBlank() ->
                            error = "Todos los campos del menú son obligatorios"
                        p == null || p < 0 -> error = "Precio inválido"
                        st == null || st < 0 -> error = "Stock inválido"
                        else -> onGuardar(
                            menu.copy(
                                sopa = sopa.trim(),
                                platoFuerte = platoFuerte.trim(),
                                guarnicion = guarnicion.trim(),
                                frijolesPostre = frijolesPostre.trim(),
                                precio = p,
                                stock = st
                            )
                        )
                    }
                },
                colors = ButtonDefaults.buttonColors(containerColor = EmeraldAccent, contentColor = Color.White)
            ) { Text("Guardar") }
        },
        dismissButton = { TextButton(onClick = onDismiss) { Text("Cancelar", color = NavyPrimary) } }
    )
}

@Composable
private fun CampoMenu(
    etiqueta: String,
    valor: String,
    teclado: KeyboardType = KeyboardType.Text,
    onChange: (String) -> Unit
) {
    OutlinedTextField(
        value = valor,
        onValueChange = onChange,
        label = { Text(etiqueta) },
        singleLine = true,
        modifier = Modifier.fillMaxWidth(),
        keyboardOptions = KeyboardOptions(keyboardType = teclado),
        colors = OutlinedTextFieldDefaults.colors(
            focusedBorderColor = EmeraldAccent,
            focusedLabelColor = EmeraldAccent,
            cursorColor = EmeraldAccent,
            focusedTextColor = NavyPrimary,
            unfocusedTextColor = NavyPrimary
        )
    )
}

@Composable
private fun SeccionMenu(titulo: String, valor: String, modifier: Modifier = Modifier) {
    Column(
        modifier = modifier
            .clip(RoundedCornerShape(12.dp))
            .background(EmeraldContainer)
            .padding(10.dp)
    ) {
        Text(titulo, color = Color.White.copy(alpha = 0.85f), fontSize = 11.sp, fontWeight = FontWeight.Medium)
        Text(valor, color = Color.White, fontSize = 13.sp, fontWeight = FontWeight.SemiBold)
    }
}