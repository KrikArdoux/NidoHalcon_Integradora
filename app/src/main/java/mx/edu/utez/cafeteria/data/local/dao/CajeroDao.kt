package mx.edu.utez.cafeteria.data.local.dao

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Upsert
import mx.edu.utez.cafeteria.data.local.entity.CajeroEntity

@Dao
interface CajeroDao {
    @Query("SELECT * FROM cajeros WHERE matricula = :matricula LIMIT 1")
    suspend fun buscar(matricula: String): CajeroEntity?

    @Upsert
    suspend fun upsert(cajero: CajeroEntity)

    @Query("SELECT COUNT(*) FROM cajeros")
    suspend fun contar(): Int
}
