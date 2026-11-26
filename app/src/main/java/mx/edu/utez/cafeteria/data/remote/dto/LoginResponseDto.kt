package mx.edu.utez.cafeteria.data.remote.dto

data class LoginResponseDto(
    val matricula: String = "",
    val nombreCompleto: String = "",
    val token: String? = null
)
