package mx.edu.utez.cafeteria.data.local

import android.content.Context
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

data class Sesion(val matricula: String, val nombre: String)

private val Context.sesionDataStore by preferencesDataStore(name = "sesion_cajero")

class UserPreferencesRepository(private val context: Context) {
    private val keyMatricula = stringPreferencesKey("matricula")
    private val keyNombre = stringPreferencesKey("nombre")

    val sesion: Flow<Sesion?> = context.sesionDataStore.data.map { p ->
        val m = p[keyMatricula]
        if (m.isNullOrBlank()) null else Sesion(m, p[keyNombre] ?: m)
    }

    suspend fun guardarSesion(matricula: String, nombre: String) {
        context.sesionDataStore.edit {
            it[keyMatricula] = matricula
            it[keyNombre] = nombre
        }
    }

    suspend fun cerrarSesion() {
        context.sesionDataStore.edit { it.clear() }
    }
}
