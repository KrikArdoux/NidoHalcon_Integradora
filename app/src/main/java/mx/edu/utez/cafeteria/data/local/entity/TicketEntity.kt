package mx.edu.utez.cafeteria.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "tickets")
data class TicketEntity(
    @PrimaryKey(autoGenerate = true) val numeroPedido: Int = 0,
    val matriculaCajero: String,
    val nombreCajero: String,
    val fechaHora: String,
    val metodoPago: String,
    val precioTotal: Double
)
