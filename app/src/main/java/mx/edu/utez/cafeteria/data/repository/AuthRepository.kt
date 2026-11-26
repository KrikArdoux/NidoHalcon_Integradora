package mx.edu.utez.cafeteria.data.repository

import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import mx.edu.utez.cafeteria.data.local.AppDatabase
import mx.edu.utez.cafeteria.data.local.DataSeeder
import mx.edu.utez.cafeteria.data.local.UserPreferencesRepository
import mx.edu.utez.cafeteria.data.remote.ApiService
import mx.edu.utez.cafeteria.data.remote.dto.LoginRequestDto

class AuthRepository(
    private val db: AppDatabase,
    private val api: ApiService,
    private val prefs: UserPreferencesRepository
) {
    suspend fun login(matricula: String, password: String): Result<String> = withContext(Dispatchers.IO) {
        try {
            val resp = api.login(LoginRequestDto(matricula, password))
            if (resp.isSuccessful) {
                val body = resp.body()
                if (body != null) {
                    val m = body.matricula.ifBlank { matricula }
                    val n = body.nombreCompleto.ifBlank { m }
                    prefs.guardarSesion(m, n)
                    return@withContext Result.success(n)
                }
            } else if (resp.code() in 400..403) {
                return@withContext Result.failure(Exception("Matrícula o contraseña incorrectas"))
            }
        } catch (e: CancellationException) {
            throw e
        } catch (e: Exception) {
        }
        loginLocal(matricula, password)
    }

    private suspend fun loginLocal(matricula: String, password: String): Result<String> {
        DataSeeder.sembrarSiVacio(db)
        val cajero = db.cajeroDao().buscar(matricula)
        return if (cajero != null && cajero.passwordHash == password) {
            prefs.guardarSesion(cajero.matricula, cajero.nombreCompleto)
            Result.success(cajero.nombreCompleto)
        } else {
            Result.failure(Exception("Matrícula o contraseña incorrectas"))
        }
    }
}
