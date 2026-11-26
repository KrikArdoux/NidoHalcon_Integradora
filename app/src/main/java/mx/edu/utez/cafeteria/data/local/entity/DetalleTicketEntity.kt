package mx.edu.utez.cafeteria.data.local.entity

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    tableName = "detalle_tickets",
    foreignKeys = [ForeignKey(
        entity = TicketEntity::class,
        parentColumns = ["numeroPedido"],
        childColumns = ["numeroPedido"],
        onDelete = ForeignKey.CASCADE
    )],
    indices = [Index("numeroPedido")]
)
data class DetalleTicketEntity(
    @PrimaryKey(autoGenerate = true) val idDetalle: Int = 0,
    val numeroPedido: Int,
    val nombrePlatillo: String,
    val nombreVariante: String,
    val cantidad: Int,
    val precioUnitario: Double,
    val importe: Double
)
