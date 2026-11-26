package mx.edu.utez.cafeteria.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "cajeros")
data class CajeroEntity(
    @PrimaryKey val matricula: String,
    val nombreCompleto: String,
    val passwordHash: String
)
