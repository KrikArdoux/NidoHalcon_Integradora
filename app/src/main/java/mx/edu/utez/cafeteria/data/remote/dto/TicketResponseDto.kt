package mx.edu.utez.cafeteria.data.remote.dto

data class TicketResponseDto(
    val numeroPedido: Int = 0,
    val matriculaCajero: String = "",
    val nombreCajero: String = "",
    val fechaHora: String = "",
    val metodoPago: String = "",
    val precioTotal: Double = 0.0,
    val detalles: List<DetalleTicketDto> = emptyList()
)
