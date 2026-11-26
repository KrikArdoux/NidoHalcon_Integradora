package mx.edu.utez.cafeteria.ui.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import mx.edu.utez.cafeteria.data.local.UserPreferencesRepository
import mx.edu.utez.cafeteria.data.repository.ItemCarrito
import mx.edu.utez.cafeteria.data.repository.PedidoRepository
import mx.edu.utez.cafeteria.data.repository.StockInsuficienteException

val METODOS_PAGO = listOf("Efectivo", "Tarjeta", "Transferencia")

data class PedidoUiState(
    val items: List<ItemCarrito> = emptyList(),
    val metodoPago: String = "Efectivo",
    val procesando: Boolean = false,
    val error: String? = null,
    val ticketCreado: Int? = null
) {
    val total: Double get() = items.sumOf { it.precioUnitario * it.cantidad }
    val cantidadTotal: Int get() = items.sumOf { it.cantidad }
}

class PedidoViewModel(
    private val pedidoRepository: PedidoRepository,
    private val prefs: UserPreferencesRepository
) : ViewModel() {

    private val _state = MutableStateFlow(PedidoUiState())
    val state: StateFlow<PedidoUiState> = _state.asStateFlow()

    private val _mensaje = MutableStateFlow<String?>(null)
    val mensaje: StateFlow<String?> = _mensaje.asStateFlow()

    fun consumirMensaje() { _mensaje.value = null }
    fun consumirTicket() = _state.update { it.copy(ticketCreado = null) }

    fun agregar(item: ItemCarrito) {
        if (item.stockDisponible <= 0) {
            _mensaje.value = "AGOTADO HOY"
            return
        }
        val actual = _state.value.items
        val existente = actual.firstOrNull { it.key == item.key }
        if (existente == null) {
            _state.update { it.copy(items = actual + item.copy(cantidad = 1), error = null) }
            return
        }
        if (existente.cantidad + 1 > item.stockDisponible) {
            _mensaje.value = "Stock máximo disponible: ${item.stockDisponible}"
            return
        }
        _state.update {
            it.copy(
                items = actual.map { i ->
                    if (i.key == item.key) i.copy(cantidad = i.cantidad + 1, stockDisponible = item.stockDisponible) else i
                },
                error = null
            )
        }
    }

    fun incrementar(key: String) {
        val item = _state.value.items.firstOrNull { it.key == key } ?: return
        if (item.cantidad + 1 > item.stockDisponible) {
            _state.update { it.copy(error = "Stock máximo disponible: ${item.stockDisponible}") }
            return
        }
        _state.update { st ->
            st.copy(items = st.items.map { if (it.key == key) it.copy(cantidad = it.cantidad + 1) else it }, error = null)
        }
    }

    fun decrementar(key: String) {
        _state.update { st ->
            st.copy(
                items = st.items.mapNotNull {
                    if (it.key != key) it else if (it.cantidad <= 1) null else it.copy(cantidad = it.cantidad - 1)
                },
                error = null
            )
        }
    }

    fun quitar(key: String) = _state.update { st -> st.copy(items = st.items.filterNot { it.key == key }) }
    fun vaciar() { _state.value = PedidoUiState() }
    fun seleccionarMetodo(m: String) = _state.update { it.copy(metodoPago = m) }

    fun confirmar() {
        val st = _state.value
        if (st.items.isEmpty() || st.procesando) return
        viewModelScope.launch {
            val sesion = prefs.sesion.first()
            if (sesion == null) {
                _state.update { it.copy(error = "Sesión no válida. Inicia sesión de nuevo.") }
                return@launch
            }
            _state.update { it.copy(procesando = true, error = null) }
            try {
                val numero = pedidoRepository.registrarPedido(sesion.matricula, sesion.nombre, st.metodoPago, st.items)
                _state.update { it.copy(items = emptyList(), procesando = false, ticketCreado = numero) }
            } catch (e: CancellationException) {
                throw e
            } catch (e: StockInsuficienteException) {
                _state.update { it.copy(procesando = false, error = "Stock insuficiente para ${e.producto}") }
            } catch (e: Exception) {
                _state.update { it.copy(procesando = false, error = "No se pudo registrar el pedido") }
            }
        }
    }
}
