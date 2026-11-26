package mx.edu.utez.cafeteria.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Transaction
import androidx.room.Update
import androidx.room.Upsert
import kotlinx.coroutines.flow.Flow
import mx.edu.utez.cafeteria.data.local.PlatilloConVariantes
import mx.edu.utez.cafeteria.data.local.entity.PlatilloEntity

@Dao
interface PlatilloDao {
    @Transaction
    @Query("SELECT * FROM platillos WHERE activo = 1 ORDER BY idPlatillo")
    fun observarActivos(): Flow<List<PlatilloConVariantes>>

    @Transaction
    @Query("SELECT * FROM platillos WHERE idPlatillo = :id")
    suspend fun obtener(id: Int): PlatilloConVariantes?

    @Insert
    suspend fun insertar(platillo: PlatilloEntity): Long

    @Update
    suspend fun actualizar(platillo: PlatilloEntity)

    @Upsert
    suspend fun upsert(platillo: PlatilloEntity)

    @Query("UPDATE platillos SET activo = 0 WHERE idPlatillo = :id")
    suspend fun desactivar(id: Int)

    @Query("SELECT COUNT(*) FROM platillos")
    suspend fun contar(): Int
}
