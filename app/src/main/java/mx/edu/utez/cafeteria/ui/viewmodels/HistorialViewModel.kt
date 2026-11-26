package mx.edu.utez.cafeteria.ui.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import mx.edu.utez.cafeteria.data.local.TicketConDetalles
import mx.edu.utez.cafeteria.data.local.UserPreferencesRepository
import mx.edu.utez.cafeteria.data.local.entity.TicketEntity
import mx.edu.utez.cafeteria.data.repository.PedidoRepository

data class EstadoTicket(val cargando: Boolean = true, val ticket: TicketConDetalles? = null)

@OptIn(ExperimentalCoroutinesApi::class)
class HistorialViewModel(
    private val pedidoRepository: PedidoRepository,
    private val prefs: UserPreferencesRepository
) : ViewModel() {

    val tickets: StateFlow<List<TicketEntity>> = prefs.sesion
        .flatMapLatest { s -> if (s == null) flowOf(emptyList()) else pedidoRepository.historial(s.matricula) }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    private val _detalle = MutableStateFlow(EstadoTicket())
    val detalle: StateFlow<EstadoTicket> = _detalle.asStateFlow()

    init {
        viewModelScope.launch {
            prefs.sesion.collectLatest { s -> if (s != null) pedidoRepository.sincronizarHistorial(s.matricula) }
        }
    }

    fun cargarTicket(numero: Int) {
        viewModelScope.launch {
            _detalle.value = EstadoTicket(cargando = true)
            val sesion = prefs.sesion.first()
            val t = pedidoRepository.obtenerTicket(numero)
            val visible = if (t != null && sesion != null && t.ticket.matriculaCajero == sesion.matricula) t else null
            _detalle.value = EstadoTicket(cargando = false, ticket = visible)
        }
    }
}
