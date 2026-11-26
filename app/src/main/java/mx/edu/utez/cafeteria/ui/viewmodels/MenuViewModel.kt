package mx.edu.utez.cafeteria.ui.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import java.text.Normalizer
import java.util.Calendar
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import mx.edu.utez.cafeteria.data.local.PlatilloConVariantes
import mx.edu.utez.cafeteria.data.local.Sesion
import mx.edu.utez.cafeteria.data.local.UserPreferencesRepository
import mx.edu.utez.cafeteria.data.local.entity.MenuDelDiaEntity
import mx.edu.utez.cafeteria.data.local.entity.PlatilloEntity
import mx.edu.utez.cafeteria.data.local.entity.VarianteEntity
import mx.edu.utez.cafeteria.data.repository.MenuRepository

val DIAS_SEMANA = listOf("Lun", "Mar", "Mie", "Jue", "Vie")

data class MenuUiState(
    val categorias: List<String> = emptyList(),
    val busqueda: String = "",
    val platillos: List<PlatilloConVariantes> = emptyList(),
    val menuDelDia: MenuDelDiaEntity? = null,
    val dias: List<String> = DIAS_SEMANA,
    val diaSeleccionado: String = "Lun"
)

private fun String.normalizar(): String =
    Normalizer.normalize(this, Normalizer.Form.NFD)
        .replace(Regex("\\p{InCombiningDiacriticalMarks}+"), "")
        .lowercase()
        .trim()

private fun diaActual(): String = when (Calendar.getInstance().get(Calendar.DAY_OF_WEEK)) {
    Calendar.TUESDAY -> "Mar"
    Calendar.WEDNESDAY -> "Mie"
    Calendar.THURSDAY -> "Jue"
    Calendar.FRIDAY -> "Vie"
    else -> "Lun"
}

class MenuViewModel(
    private val menuRepository: MenuRepository,
    private val prefs: UserPreferencesRepository
) : ViewModel() {

    private val _busqueda = MutableStateFlow("")
    private val _dia = MutableStateFlow(diaActual())
    private val _mensaje = MutableStateFlow<String?>(null)
    val mensaje: StateFlow<String?> = _mensaje

    val uiState: StateFlow<MenuUiState> = combine(
        menuRepository.platillos, menuRepository.menuDelDia, _busqueda, _dia
    ) { platillos, menus, q, dia ->
        val activos = platillos
            .filter { it.platillo.activo }
            .map { it.copy(variantes = it.variantes.filter { v -> v.activo }) }
        val query = q.normalizar()
        val filtrados = activos.filter { p ->
            query.isEmpty() ||
                    p.platillo.nombre.normalizar().contains(query) ||
                    p.variantes.any { it.nombreVariante.normalizar().contains(query) }
        }
        MenuUiState(
            categorias = activos.map { it.platillo.categoria }.distinct(),
            busqueda = q,
            platillos = filtrados,
            menuDelDia = menus.firstOrNull { it.diaSemana == dia },
            dias = DIAS_SEMANA,
            diaSeleccionado = dia
        )
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), MenuUiState(diaSeleccionado = diaActual()))

    val sesion: StateFlow<Sesion?> =
        prefs.sesion.stateIn(viewModelScope, SharingStarted.Eagerly, null)

    init {
        refrescar()
    }

    fun refrescar() { viewModelScope.launch { menuRepository.sincronizar() } }
    fun onBusqueda(q: String) { _busqueda.value = q }
    fun onDia(d: String) { _dia.value = d }
    fun consumirMensaje() { _mensaje.value = null }

    suspend fun obtenerPlatillo(id: Int): PlatilloConVariantes? = menuRepository.obtenerPlatillo(id)

    fun guardarPlatillo(platillo: PlatilloEntity, variantes: List<VarianteEntity>, onListo: () -> Unit) {
        viewModelScope.launch {
            menuRepository.guardarPlatillo(platillo, variantes)
            _mensaje.value = "Platillo guardado"
            onListo()
        }
    }

    fun desactivarPlatillo(id: Int) {
        viewModelScope.launch {
            menuRepository.desactivarPlatillo(id)
            _mensaje.value = "Platillo desactivado"
        }
    }

    fun actualizarMenuDelDia(menu: MenuDelDiaEntity) {
        viewModelScope.launch {
            menuRepository.actualizarMenuDelDia(menu)
            _mensaje.value = "Menú del día actualizado"
        }
    }

    fun cerrarSesion(onListo: () -> Unit) {
        viewModelScope.launch {
            prefs.cerrarSesion()
            onListo()
        }
    }
}