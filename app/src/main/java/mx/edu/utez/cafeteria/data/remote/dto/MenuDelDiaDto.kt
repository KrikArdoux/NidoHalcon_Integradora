package mx.edu.utez.cafeteria.data.remote.dto

import mx.edu.utez.cafeteria.data.local.entity.MenuDelDiaEntity

data class MenuDelDiaDto(
    val idMenuDia: Int = 0,
    val diaSemana: String = "",
    val numeroDia: Int = 0,
    val sopa: String = "",
    val platoFuerte: String = "",
    val guarnicion: String = "",
    val frijolesPostre: String = "",
    val precio: Double = 70.0,
    val stock: Int = 0,
    val activo: Boolean = true
)

fun MenuDelDiaDto.toEntity() = MenuDelDiaEntity(
    idMenuDia, diaSemana, numeroDia, sopa, platoFuerte, guarnicion, frijolesPostre, precio, stock, activo
)

fun MenuDelDiaEntity.toDto() = MenuDelDiaDto(
    idMenuDia, diaSemana, numeroDia, sopa, platoFuerte, guarnicion, frijolesPostre, precio, stock, activo
)