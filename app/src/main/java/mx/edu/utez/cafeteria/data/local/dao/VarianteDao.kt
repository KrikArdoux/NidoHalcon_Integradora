package mx.edu.utez.cafeteria.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import androidx.room.Upsert
import mx.edu.utez.cafeteria.data.local.entity.VarianteEntity

@Dao
interface VarianteDao {
    @Insert
    suspend fun insertar(variante: VarianteEntity): Long

    @Insert
    suspend fun insertarTodas(variantes: List<VarianteEntity>)

    @Update
    suspend fun actualizar(variante: VarianteEntity)

    @Upsert
    suspend fun upsertTodas(variantes: List<VarianteEntity>)

    @Query("UPDATE variantes SET activo = 0 WHERE idVariante = :id")
    suspend fun desactivar(id: Int)

    @Query("SELECT * FROM variantes WHERE idVariante = :id")
    suspend fun obtener(id: Int): VarianteEntity?

    @Query("UPDATE variantes SET stock = stock - :cantidad WHERE idVariante = :id AND stock >= :cantidad")
    suspend fun descontarStock(id: Int, cantidad: Int): Int
}
