package mx.edu.utez.cafeteria.ui.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import mx.edu.utez.cafeteria.data.repository.AuthRepository

data class LoginUiState(
    val matricula: String = "",
    val password: String = "",
    val cargando: Boolean = false,
    val error: String? = null,
    val exito: Boolean = false
)

class LoginViewModel(private val authRepository: AuthRepository) : ViewModel() {
    private val _state = MutableStateFlow(LoginUiState())
    val state: StateFlow<LoginUiState> = _state.asStateFlow()

    fun onMatricula(v: String) = _state.update { it.copy(matricula = v, error = null) }
    fun onPassword(v: String) = _state.update { it.copy(password = v, error = null) }

    fun login() {
        val s = _state.value
        if (s.cargando) return
        if (s.matricula.isBlank() || s.password.isBlank()) {
            _state.update { it.copy(error = "Ingresa matrícula y contraseña") }
            return
        }
        viewModelScope.launch {
            _state.update { it.copy(cargando = true, error = null) }
            authRepository.login(s.matricula.trim(), s.password).fold(
                onSuccess = { _state.update { st -> st.copy(cargando = false, exito = true) } },
                onFailure = { e -> _state.update { st -> st.copy(cargando = false, error = e.message ?: "Error al iniciar sesión") } }
            )
        }
    }

    fun consumirExito() { _state.value = LoginUiState() }
    fun limpiar() { _state.value = LoginUiState() }
}
