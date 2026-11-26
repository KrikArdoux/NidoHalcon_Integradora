package mx.edu.utez.cafeteria.ui.screens

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import mx.edu.utez.cafeteria.data.local.entity.PlatilloEntity
import mx.edu.utez.cafeteria.data.local.entity.VarianteEntity
import mx.edu.utez.cafeteria.ui.components.AppTopBar
import mx.edu.utez.cafeteria.ui.components.CameraCaptureButton
import mx.edu.utez.cafeteria.ui.components.ChipCategoria
import mx.edu.utez.cafeteria.ui.theme.AgotadoText
import mx.edu.utez.cafeteria.ui.theme.BackgroundLight
import mx.edu.utez.cafeteria.ui.theme.BorderLight
import mx.edu.utez.cafeteria.ui.theme.CardSurface
import mx.edu.utez.cafeteria.ui.theme.EmeraldAccent
import mx.edu.utez.cafeteria.ui.theme.NavyPrimary
import mx.edu.utez.cafeteria.ui.theme.TextMuted
import mx.edu.utez.cafeteria.ui.viewmodels.MenuViewModel

private data class VarianteDraft(
    val id: Int = 0,
    val nombre: String = "",
    val detalle: String = "",
    val precioExtra: String = "0",
    val stock: String = "10",
    val activo: Boolean = true
)

@Composable
fun PlatilloFormScreen(menuViewModel: MenuViewModel, idPlatillo: Int, onVolver: () -> Unit) {
    val ui by menuViewModel.uiState.collectAsState()
    var original by remember { mutableStateOf<PlatilloEntity?>(null) }
    var cargado by remember { mutableStateOf(idPlatillo <= 0) }
    var nombre by remember { mutableStateOf("") }
    var descripcion by remember { mutableStateOf("") }
    var categoria by remember { mutableStateOf("") }
    var precioBase by remember { mutableStateOf("0") }
    var imagenUri by remember { mutableStateOf<String?>(null) }
    var error by remember { mutableStateOf<String?>(null) }
    var guardando by remember { mutableStateOf(false) }
    var varianteAQuitar by remember { mutableStateOf<Int?>(null) }
    val variantes = remember { mutableStateListOf<VarianteDraft>() }

    LaunchedEffect(idPlatillo) {
        if (idPlatillo > 0) {
            menuViewModel.obtenerPlatillo(idPlatillo)?.let { p ->
                original = p.platillo
                nombre = p.platillo.nombre
                descripcion = p.platillo.descripcion
                categoria = p.platillo.categoria
                precioBase = p.platillo.precioBase.toString()
                imagenUri = p.platillo.imagenUri
                variantes.clear()
                variantes.addAll(
                    p.variantes.map {
                        VarianteDraft(it.idVariante, it.nombreVariante, it.detalle ?: "", it.precioExtra.toString(), it.stock.toString(), it.activo)
                    }
                )
            }
        }
        cargado = true
    }

    Scaffold(
        containerColor = BackgroundLight,
        topBar = { AppTopBar(if (idPlatillo > 0) "Editar platillo" else "Nuevo platillo", onVolver) }
    ) { padding ->
        if (!cargado) return@Scaffold
        Column(
            Modifier.padding(padding).verticalScroll(rememberScrollState()).padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Box(
                Modifier.fillMaxWidth().height(180.dp).clip(RoundedCornerShape(16.dp)).background(CardSurface),
                contentAlignment = Alignment.Center
            ) {
                if (imagenUri != null) {
                    AsyncImage(
                        model = imagenUri,
                        contentDescription = "Foto del platillo",
                        contentScale = ContentScale.Crop,
                        modifier = Modifier.fillMaxWidth().height(180.dp)
                    )
                } else {
                    Text("Sin foto", color = TextMuted)
                }
            }
            CameraCaptureButton(onFotoCapturada = { imagenUri = it }, modifier = Modifier.fillMaxWidth())

            Campo("Nombre del platillo", nombre) { nombre = it }
            Campo("Descripción", descripcion) { descripcion = it }
            Campo("Categoría", categoria) { categoria = it }
            LazyRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                items(ui.categorias) { c -> ChipCategoria(c, categoria == c) { categoria = c } }
            }
            Campo("Precio base", precioBase, KeyboardType.Decimal) { precioBase = it }

            Text("Variantes", color = NavyPrimary, fontWeight = FontWeight.Bold, fontSize = 18.sp)
            variantes.toList().forEachIndexed { index, d ->
                Card(
                    colors = CardDefaults.cardColors(containerColor = CardSurface),
                    border = BorderStroke(1.dp, BorderLight),
                    shape = RoundedCornerShape(14.dp)
                ) {
                    Column(Modifier.padding(12.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Text("Variante ${index + 1}", Modifier.weight(1f), color = NavyPrimary, fontWeight = FontWeight.SemiBold)
                            if (d.activo) {
                                IconButton(onClick = { varianteAQuitar = index }) { Icon(Icons.Default.Delete, "Quitar", tint = AgotadoText) }
                            } else {
                                TextButton(onClick = { variantes[index] = d.copy(activo = true) }) { Text("Restaurar", color = EmeraldAccent) }
                            }
                        }
                        if (!d.activo) {
                            Text("Variante desactivada (borrado lógico)", color = AgotadoText, fontSize = 13.sp)
                        } else {
                            Campo("Nombre de variante", d.nombre) { variantes[index] = d.copy(nombre = it) }
                            Campo("Detalle (opcional)", d.detalle) { variantes[index] = d.copy(detalle = it) }
                            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                                Box(Modifier.weight(1f)) { Campo("Precio extra", d.precioExtra, KeyboardType.Decimal) { variantes[index] = d.copy(precioExtra = it) } }
                                Box(Modifier.weight(1f)) { Campo("Stock", d.stock, KeyboardType.Number) { variantes[index] = d.copy(stock = it) } }
                            }
                        }
                    }
                }
            }
            OutlinedButton(onClick = { variantes.add(VarianteDraft()) }, modifier = Modifier.fillMaxWidth()) {
                Icon(Icons.Default.Add, contentDescription = null, tint = NavyPrimary)
                Text("  Agregar variante", color = NavyPrimary)
            }

            error?.let { Text(it, color = AgotadoText, fontSize = 13.sp) }

            Button(
                enabled = !guardando,
                modifier = Modifier.fillMaxWidth().height(50.dp),
                shape = RoundedCornerShape(14.dp),
                colors = ButtonDefaults.buttonColors(containerColor = NavyPrimary, contentColor = Color.White),
                onClick = {
                    val base = precioBase.replace(',', '.').toDoubleOrNull()
                    val activas = variantes.filter { it.activo }
                    when {
                        nombre.isBlank() -> error = "El nombre es obligatorio"
                        categoria.isBlank() -> error = "La categoría es obligatoria"
                        base == null || base < 0 -> error = "Precio base inválido"
                        activas.isEmpty() || activas.any { it.nombre.isBlank() } ->
                            error = "Agrega al menos una variante y asigna nombre a todas"
                        else -> {
                            error = null
                            guardando = true
                            val idP = original?.idPlatillo ?: 0
                            menuViewModel.guardarPlatillo(
                                PlatilloEntity(idP, nombre.trim(), descripcion.trim(), categoria.trim(), base, imagenUri, true),
                                variantes.map { d ->
                                    VarianteEntity(
                                        idVariante = d.id,
                                        idPlatillo = idP,
                                        nombreVariante = d.nombre.trim(),
                                        detalle = d.detalle.trim().ifBlank { null },
                                        precioExtra = d.precioExtra.replace(',', '.').toDoubleOrNull() ?: 0.0,
                                        stock = d.stock.toIntOrNull() ?: 0,
                                        activo = d.activo
                                    )
                                }
                            ) { onVolver() }
                        }
                    }
                }
            ) { Text("Guardar platillo") }
        }

        varianteAQuitar?.let { index ->
            val d = variantes.getOrNull(index)
            if (d == null) {
                varianteAQuitar = null
            } else {
                AlertDialog(
                    onDismissRequest = { varianteAQuitar = null },
                    containerColor = CardSurface,
                    title = { Text("Eliminar variante", color = AgotadoText, fontWeight = FontWeight.Bold) },
                    text = {
                        Text(
                            "¿Estás seguro de que deseas eliminar '${d.nombre.ifBlank { "Variante ${index + 1}" }}'?",
                            color = NavyPrimary
                        )
                    },
                    confirmButton = {
                        Button(
                            onClick = {
                                if (d.id == 0) variantes.removeAt(index) else variantes[index] = d.copy(activo = false)
                                varianteAQuitar = null
                            },
                            colors = ButtonDefaults.buttonColors(containerColor = AgotadoText, contentColor = Color.White)
                        ) { Text("Eliminar") }
                    },
                    dismissButton = { TextButton(onClick = { varianteAQuitar = null }) { Text("Cancelar", color = NavyPrimary) } }
                )
            }
        }
    }
}

@Composable
private fun Campo(
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
        keyboardOptions = KeyboardOptions(keyboardType = teclado)
    )
}