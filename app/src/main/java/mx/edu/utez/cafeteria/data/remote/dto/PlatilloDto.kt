package mx.edu.utez.cafeteria.data.remote.dto

import mx.edu.utez.cafeteria.data.local.entity.PlatilloEntity
import mx.edu.utez.cafeteria.data.local.entity.VarianteEntity

data class PlatilloDto(
    val idPlatillo: Int = 0,
    val nombre: String = "",
    val descripcion: String = "",
    val categoria: String = "",
    val precioBase: Double = 0.0,
    val imagenUri: String? = null,
    val activo: Boolean = true,
    val variantes: List<VarianteDto> = emptyList()
)

fun PlatilloDto.toEntity() = PlatilloEntity(
    idPlatillo, nombre, descripcion, categoria, precioBase, imagenUri, activo
)

fun PlatilloEntity.toDto(variantes: List<VarianteEntity>) = PlatilloDto(
    idPlatillo, nombre, descripcion, categoria, precioBase, imagenUri, activo,
    variantes.map { it.toDto() }
)
