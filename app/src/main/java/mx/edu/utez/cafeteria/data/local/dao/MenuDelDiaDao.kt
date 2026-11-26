package mx.edu.utez.cafeteria.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import androidx.room.Upsert
import kotlinx.coroutines.flow.Flow
import mx.edu.utez.cafeteria.data.local.entity.MenuDelDiaEntity

@Dao
interface MenuDelDiaDao {
    @Query("SELECT * FROM menu_del_dia WHERE activo = 1 ORDER BY numeroDia")
    fun observarActivos(): Flow<List<MenuDelDiaEntity>>

    @Insert
    suspend fun insertarTodos(menus: List<MenuDelDiaEntity>)

    @Update
    suspend fun updateMenuDelDia(menu: MenuDelDiaEntity)

    @Upsert
    suspend fun upsertTodos(menus: List<MenuDelDiaEntity>)

    @Query("UPDATE menu_del_dia SET stock = stock - :cantidad WHERE idMenuDia = :id AND stock >= :cantidad")
    suspend fun descontarStock(id: Int, cantidad: Int): Int

    @Query("SELECT COUNT(*) FROM menu_del_dia")
    suspend fun contar(): Int
}