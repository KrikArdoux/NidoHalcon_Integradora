package mx.edu.utez.cafeteria.data.repository

import androidx.room.withTransaction
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.withContext
import mx.edu.utez.cafeteria.data.local.AppDatabase
import mx.edu.utez.cafeteria.data.local.PlatilloConVariantes
import mx.edu.utez.cafeteria.data.local.entity.MenuDelDiaEntity
import mx.edu.utez.cafeteria.data.local.entity.PlatilloEntity
import mx.edu.utez.cafeteria.data.local.entity.VarianteEntity
import mx.edu.utez.cafeteria.data.remote.ApiService
import mx.edu.utez.cafeteria.data.remote.dto.toDto
import mx.edu.utez.cafeteria.data.remote.dto.toEntity

internal suspend fun remoto(bloque: suspend () -> Unit) {
    try {
        bloque()
    } catch (e: CancellationException) {
        throw e
    } catch (e: Exception) {
    }
}

class MenuRepository(private val db: AppDatabase, private val api: ApiService) {
    private val platilloDao = db.platilloDao()
    private val varianteDao = db.varianteDao()
    private val menuDao = db.menuDelDiaDao()

    val platillos: Flow<List<PlatilloConVariantes>> = platilloDao.observarActivos()
    val menuDelDia: Flow<List<MenuDelDiaEntity>> = menuDao.observarActivos()

    suspend fun sincronizar() = withContext(Dispatchers.IO) {
        remoto {
            val r = api.getPlatillos()
            val lista = if (r.isSuccessful) r.body().orEmpty() else emptyList()
            if (lista.isNotEmpty()) {
                db.withTransaction {
                    lista.filter { it.idPlatillo > 0 }.forEach { dto ->
                        platilloDao.upsert(dto.toEntity())
                        varianteDao.upsertTodas(
                            dto.variantes.filter { it.idVariante > 0 }.map { it.toEntity(dto.idPlatillo) }
                        )
                    }
                }
            }
        }
        remoto {
            val r = api.getMenuDelDia()
            val lista = if (r.isSuccessful) r.body().orEmpty() else emptyList()
            if (lista.isNotEmpty()) {
                menuDao.upsertTodos(lista.filter { it.idMenuDia > 0 }.map { it.toEntity() })
            }
        }
    }

    suspend fun obtenerPlatillo(id: Int): PlatilloConVariantes? = platilloDao.obtener(id)

    suspend fun guardarPlatillo(platillo: PlatilloEntity, variantes: List<VarianteEntity>): Int {
        val id = db.withTransaction {
            val idP = if (platillo.idPlatillo == 0) {
                platilloDao.insertar(platillo).toInt()
            } else {
                platilloDao.actualizar(platillo)
                platillo.idPlatillo
            }
            variantes.forEach { v ->
                val vp = v.copy(idPlatillo = idP)
                if (vp.idVariante == 0) varianteDao.insertar(vp) else varianteDao.actualizar(vp)
            }
            idP
        }
        remoto {
            val completo = platilloDao.obtener(id) ?: return@remoto
            val dto = completo.platillo.toDto(completo.variantes)
            if (platillo.idPlatillo == 0) api.createPlatillo(dto) else api.updatePlatillo(id, dto)
        }
        return id
    }

    suspend fun actualizarMenuDelDia(menu: MenuDelDiaEntity) = withContext(Dispatchers.IO) {
        menuDao.updateMenuDelDia(menu)
        remoto { api.updateMenuDelDia(menu.idMenuDia, menu.toDto()) }
    }

    suspend fun desactivarPlatillo(id: Int) {
        platilloDao.desactivar(id)
        remoto { api.desactivarPlatillo(id) }
    }
}