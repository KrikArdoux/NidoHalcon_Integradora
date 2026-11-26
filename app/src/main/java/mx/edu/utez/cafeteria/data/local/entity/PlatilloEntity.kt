package mx.edu.utez.cafeteria.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "platillos")
data class PlatilloEntity(
    @PrimaryKey(autoGenerate = true) val idPlatillo: Int = 0,
    val nombre: String,
    val descripcion: String,
    val categoria: String,
    val precioBase: Double,
    val imagenUri: String? = null,
    val activo: Boolean = true
)
