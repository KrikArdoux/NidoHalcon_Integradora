package mx.edu.utez.cafeteria.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "menu_del_dia")
data class MenuDelDiaEntity(
    @PrimaryKey(autoGenerate = true) val idMenuDia: Int = 0,
    val diaSemana: String,
    val numeroDia: Int,
    val sopa: String,
    val platoFuerte: String,
    val guarnicion: String,
    val frijolesPostre: String,
    val precio: Double = 70.0,
    val stock: Int,
    val activo: Boolean = true
)
