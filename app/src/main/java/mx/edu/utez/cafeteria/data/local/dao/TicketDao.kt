package mx.edu.utez.cafeteria.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Transaction
import kotlinx.coroutines.flow.Flow
import mx.edu.utez.cafeteria.data.local.TicketConDetalles
import mx.edu.utez.cafeteria.data.local.entity.DetalleTicketEntity
import mx.edu.utez.cafeteria.data.local.entity.TicketEntity

@Dao
abstract class TicketDao {
    @Insert
    abstract suspend fun insertarTicket(ticket: TicketEntity): Long

    @Insert
    abstract suspend fun insertarDetalles(detalles: List<DetalleTicketEntity>)

    @Transaction
    open suspend fun insertarTicketConDetalles(
        ticket: TicketEntity,
        detalles: List<DetalleTicketEntity>
    ): Int {
        val numero = insertarTicket(ticket).toInt()
        insertarDetalles(detalles.map { it.copy(numeroPedido = numero) })
        return numero
    }

    @Query("SELECT * FROM tickets WHERE matriculaCajero = :matricula ORDER BY numeroPedido DESC")
    abstract fun observarPorCajero(matricula: String): Flow<List<TicketEntity>>

    @Transaction
    @Query("SELECT * FROM tickets WHERE numeroPedido = :numero")
    abstract suspend fun obtenerConDetalles(numero: Int): TicketConDetalles?

    @Query("SELECT COUNT(*) FROM tickets WHERE numeroPedido = :numero")
    abstract suspend fun existe(numero: Int): Int

    @Query("SELECT COUNT(*) FROM tickets WHERE matriculaCajero = :matricula AND fechaHora = :fecha AND precioTotal = :total")
    abstract suspend fun existeSimilar(matricula: String, fecha: String, total: Double): Int
}
