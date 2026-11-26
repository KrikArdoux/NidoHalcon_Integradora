package mx.edu.utez.cafeteria.data.repository

import androidx.room.withTransaction
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import kotlinx.coroutines.flow.Flow
import mx.edu.utez.cafeteria.data.local.AppDatabase
import mx.edu.utez.cafeteria.data.local.TicketConDetalles
import mx.edu.utez.cafeteria.data.local.entity.DetalleTicketEntity
import mx.edu.utez.cafeteria.data.local.entity.TicketEntity
import mx.edu.utez.cafeteria.data.remote.ApiService
import mx.edu.utez.cafeteria.data.remote.dto.DetalleTicketDto
import mx.edu.utez.cafeteria.data.remote.dto.TicketRequestDto

const val TIPO_VARIANTE = "VARIANTE"
const val TIPO_MENU = "MENU"

data class ItemCarrito(
    val key: String,
    val tipo: String,
    val idRef: Int,
    val nombrePlatillo: String,
    val nombreVariante: String,
    val precioUnitario: Double,
    val cantidad: Int,
    val stockDisponible: Int
)

class StockInsuficienteException(val producto: String) : Exception("Stock insuficiente para $producto")

class PedidoRepository(private val db: AppDatabase, private val api: ApiService) {
    private val ticketDao = db.ticketDao()
    private val varianteDao = db.varianteDao()
    private val menuDao = db.menuDelDiaDao()

    fun historial(matricula: String): Flow<List<TicketEntity>> = ticketDao.observarPorCajero(matricula)

    suspend fun obtenerTicket(numero: Int): TicketConDetalles? = ticketDao.obtenerConDetalles(numero)

    suspend fun registrarPedido(
        matricula: String,
        nombreCajero: String,
        metodoPago: String,
        items: List<ItemCarrito>
    ): Int {
        val total = items.sumOf { it.precioUnitario * it.cantidad }
        val fecha = SimpleDateFormat("dd/MM/yyyy HH:mm", Locale("es", "MX")).format(Date())
        val ticket = TicketEntity(0, matricula, nombreCajero, fecha, metodoPago, total)
        val detalles = items.map {
            DetalleTicketEntity(
                numeroPedido = 0,
                nombrePlatillo = it.nombrePlatillo,
                nombreVariante = it.nombreVariante,
                cantidad = it.cantidad,
                precioUnitario = it.precioUnitario,
                importe = it.precioUnitario * it.cantidad
            )
        }
        val numero = db.withTransaction {
            val n = ticketDao.insertarTicketConDetalles(ticket, detalles)
            items.forEach { i ->
                val filas = if (i.tipo == TIPO_MENU) {
                    menuDao.descontarStock(i.idRef, i.cantidad)
                } else {
                    varianteDao.descontarStock(i.idRef, i.cantidad)
                }
                if (filas == 0) throw StockInsuficienteException(i.nombreVariante.ifBlank { i.nombrePlatillo })
            }
            n
        }
        remoto {
            api.createTicket(
                TicketRequestDto(
                    matriculaCajero = matricula,
                    nombreCajero = nombreCajero,
                    fechaHora = fecha,
                    metodoPago = metodoPago,
                    precioTotal = total,
                    detalles = detalles.map {
                        DetalleTicketDto(it.nombrePlatillo, it.nombreVariante, it.cantidad, it.precioUnitario, it.importe)
                    }
                )
            )
        }
        return numero
    }

    suspend fun sincronizarHistorial(matricula: String) {
        remoto {
            val r = api.getTicketsByCajero(matricula)
            if (!r.isSuccessful) return@remoto
            r.body().orEmpty().forEach { t ->
                if (t.numeroPedido > 0 &&
                    ticketDao.existe(t.numeroPedido) == 0 &&
                    ticketDao.existeSimilar(matricula, t.fechaHora, t.precioTotal) == 0
                ) {
                    db.withTransaction {
                        ticketDao.insertarTicket(
                            TicketEntity(
                                t.numeroPedido, t.matriculaCajero.ifBlank { matricula }, t.nombreCajero,
                                t.fechaHora, t.metodoPago, t.precioTotal
                            )
                        )
                        ticketDao.insertarDetalles(
                            t.detalles.map {
                                DetalleTicketEntity(
                                    numeroPedido = t.numeroPedido,
                                    nombrePlatillo = it.nombrePlatillo,
                                    nombreVariante = it.nombreVariante,
                                    cantidad = it.cantidad,
                                    precioUnitario = it.precioUnitario,
                                    importe = it.importe
                                )
                            }
                        )
                    }
                }
            }
        }
    }
}
