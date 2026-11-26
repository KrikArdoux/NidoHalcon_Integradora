package mx.edu.utez.cafeteria.data.remote.dto

import mx.edu.utez.cafeteria.data.local.entity.VarianteEntity

data class VarianteDto(
    val idVariante: Int = 0,
    val idPlatillo: Int = 0,
    val nombreVariante: String = "",
    val detalle: String? = null,
    val precioExtra: Double = 0.0,
    val stock: Int = 0,
    val activo: Boolean = true
)

fun VarianteDto.toEntity(idPlatilloPadre: Int) = VarianteEntity(
    idVariante, idPlatilloPadre, nombreVariante, detalle, precioExtra, stock, activo
)

fun VarianteEntity.toDto() = VarianteDto(
    idVariante, idPlatillo, nombreVariante, detalle, precioExtra, stock, activo
)
