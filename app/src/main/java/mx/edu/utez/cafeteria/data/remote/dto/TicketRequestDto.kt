package mx.edu.utez.cafeteria.data.remote.dto

data class DetalleTicketDto(
    val nombrePlatillo: String = "",
    val nombreVariante: String = "",
    val cantidad: Int = 0,
    val precioUnitario: Double = 0.0,
    val importe: Double = 0.0
)

data class TicketRequestDto(
    val matriculaCajero: String = "",
    val nombreCajero: String = "",
    val fechaHora: String = "",
    val metodoPago: String = "",
    val precioTotal: Double = 0.0,
    val detalles: List<DetalleTicketDto> = emptyList()
)
