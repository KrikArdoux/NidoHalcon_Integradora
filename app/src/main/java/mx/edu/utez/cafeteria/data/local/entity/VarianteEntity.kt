package mx.edu.utez.cafeteria.data.local.entity

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    tableName = "variantes",
    foreignKeys = [ForeignKey(
        entity = PlatilloEntity::class,
        parentColumns = ["idPlatillo"],
        childColumns = ["idPlatillo"],
        onDelete = ForeignKey.CASCADE
    )],
    indices = [Index("idPlatillo")]
)
data class VarianteEntity(
    @PrimaryKey(autoGenerate = true) val idVariante: Int = 0,
    val idPlatillo: Int,
    val nombreVariante: String,
    val detalle: String? = null,
    val precioExtra: Double,
    val stock: Int,
    val activo: Boolean = true
)
