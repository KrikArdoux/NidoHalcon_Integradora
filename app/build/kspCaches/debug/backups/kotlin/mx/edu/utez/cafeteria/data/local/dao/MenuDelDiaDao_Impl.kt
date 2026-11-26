package mx.edu.utez.cafeteria.`data`.local.dao

import androidx.room.EntityDeleteOrUpdateAdapter
import androidx.room.EntityInsertAdapter
import androidx.room.EntityUpsertAdapter
import androidx.room.RoomDatabase
import androidx.room.coroutines.createFlow
import androidx.room.util.getColumnIndexOrThrow
import androidx.room.util.getTotalChangedRows
import androidx.room.util.performSuspending
import androidx.sqlite.SQLiteStatement
import javax.`annotation`.processing.Generated
import kotlin.Boolean
import kotlin.Double
import kotlin.Int
import kotlin.String
import kotlin.Suppress
import kotlin.Unit
import kotlin.collections.List
import kotlin.collections.MutableList
import kotlin.collections.mutableListOf
import kotlin.reflect.KClass
import kotlinx.coroutines.flow.Flow
import mx.edu.utez.cafeteria.`data`.local.entity.MenuDelDiaEntity

@Generated(value = ["androidx.room.RoomProcessor"])
@Suppress(names = ["UNCHECKED_CAST", "DEPRECATION", "REDUNDANT_PROJECTION", "REMOVAL"])
public class MenuDelDiaDao_Impl(
  __db: RoomDatabase,
) : MenuDelDiaDao {
  private val __db: RoomDatabase

  private val __insertAdapterOfMenuDelDiaEntity: EntityInsertAdapter<MenuDelDiaEntity>

  private val __updateAdapterOfMenuDelDiaEntity: EntityDeleteOrUpdateAdapter<MenuDelDiaEntity>

  private val __upsertAdapterOfMenuDelDiaEntity: EntityUpsertAdapter<MenuDelDiaEntity>
  init {
    this.__db = __db
    this.__insertAdapterOfMenuDelDiaEntity = object : EntityInsertAdapter<MenuDelDiaEntity>() {
      protected override fun createQuery(): String =
          "INSERT OR ABORT INTO `menu_del_dia` (`idMenuDia`,`diaSemana`,`numeroDia`,`sopa`,`platoFuerte`,`guarnicion`,`frijolesPostre`,`precio`,`stock`,`activo`) VALUES (nullif(?, 0),?,?,?,?,?,?,?,?,?)"

      protected override fun bind(statement: SQLiteStatement, entity: MenuDelDiaEntity) {
        statement.bindLong(1, entity.idMenuDia.toLong())
        statement.bindText(2, entity.diaSemana)
        statement.bindLong(3, entity.numeroDia.toLong())
        statement.bindText(4, entity.sopa)
        statement.bindText(5, entity.platoFuerte)
        statement.bindText(6, entity.guarnicion)
        statement.bindText(7, entity.frijolesPostre)
        statement.bindDouble(8, entity.precio)
        statement.bindLong(9, entity.stock.toLong())
        val _tmp: Int = if (entity.activo) 1 else 0
        statement.bindLong(10, _tmp.toLong())
      }
    }
    this.__updateAdapterOfMenuDelDiaEntity = object :
        EntityDeleteOrUpdateAdapter<MenuDelDiaEntity>() {
      protected override fun createQuery(): String =
          "UPDATE OR ABORT `menu_del_dia` SET `idMenuDia` = ?,`diaSemana` = ?,`numeroDia` = ?,`sopa` = ?,`platoFuerte` = ?,`guarnicion` = ?,`frijolesPostre` = ?,`precio` = ?,`stock` = ?,`activo` = ? WHERE `idMenuDia` = ?"

      protected override fun bind(statement: SQLiteStatement, entity: MenuDelDiaEntity) {
        statement.bindLong(1, entity.idMenuDia.toLong())
        statement.bindText(2, entity.diaSemana)
        statement.bindLong(3, entity.numeroDia.toLong())
        statement.bindText(4, entity.sopa)
        statement.bindText(5, entity.platoFuerte)
        statement.bindText(6, entity.guarnicion)
        statement.bindText(7, entity.frijolesPostre)
        statement.bindDouble(8, entity.precio)
        statement.bindLong(9, entity.stock.toLong())
        val _tmp: Int = if (entity.activo) 1 else 0
        statement.bindLong(10, _tmp.toLong())
        statement.bindLong(11, entity.idMenuDia.toLong())
      }
    }
    this.__upsertAdapterOfMenuDelDiaEntity = EntityUpsertAdapter<MenuDelDiaEntity>(object :
        EntityInsertAdapter<MenuDelDiaEntity>() {
      protected override fun createQuery(): String =
          "INSERT INTO `menu_del_dia` (`idMenuDia`,`diaSemana`,`numeroDia`,`sopa`,`platoFuerte`,`guarnicion`,`frijolesPostre`,`precio`,`stock`,`activo`) VALUES (nullif(?, 0),?,?,?,?,?,?,?,?,?)"

      protected override fun bind(statement: SQLiteStatement, entity: MenuDelDiaEntity) {
        statement.bindLong(1, entity.idMenuDia.toLong())
        statement.bindText(2, entity.diaSemana)
        statement.bindLong(3, entity.numeroDia.toLong())
        statement.bindText(4, entity.sopa)
        statement.bindText(5, entity.platoFuerte)
        statement.bindText(6, entity.guarnicion)
        statement.bindText(7, entity.frijolesPostre)
        statement.bindDouble(8, entity.precio)
        statement.bindLong(9, entity.stock.toLong())
        val _tmp: Int = if (entity.activo) 1 else 0
        statement.bindLong(10, _tmp.toLong())
      }
    }, object : EntityDeleteOrUpdateAdapter<MenuDelDiaEntity>() {
      protected override fun createQuery(): String =
          "UPDATE `menu_del_dia` SET `idMenuDia` = ?,`diaSemana` = ?,`numeroDia` = ?,`sopa` = ?,`platoFuerte` = ?,`guarnicion` = ?,`frijolesPostre` = ?,`precio` = ?,`stock` = ?,`activo` = ? WHERE `idMenuDia` = ?"

      protected override fun bind(statement: SQLiteStatement, entity: MenuDelDiaEntity) {
        statement.bindLong(1, entity.idMenuDia.toLong())
        statement.bindText(2, entity.diaSemana)
        statement.bindLong(3, entity.numeroDia.toLong())
        statement.bindText(4, entity.sopa)
        statement.bindText(5, entity.platoFuerte)
        statement.bindText(6, entity.guarnicion)
        statement.bindText(7, entity.frijolesPostre)
        statement.bindDouble(8, entity.precio)
        statement.bindLong(9, entity.stock.toLong())
        val _tmp: Int = if (entity.activo) 1 else 0
        statement.bindLong(10, _tmp.toLong())
        statement.bindLong(11, entity.idMenuDia.toLong())
      }
    })
  }

  public override suspend fun insertarTodos(menus: List<MenuDelDiaEntity>): Unit =
      performSuspending(__db, false, true) { _connection ->
    __insertAdapterOfMenuDelDiaEntity.insert(_connection, menus)
  }

  public override suspend fun updateMenuDelDia(menu: MenuDelDiaEntity): Unit =
      performSuspending(__db, false, true) { _connection ->
    __updateAdapterOfMenuDelDiaEntity.handle(_connection, menu)
  }

  public override suspend fun upsertTodos(menus: List<MenuDelDiaEntity>): Unit =
      performSuspending(__db, false, true) { _connection ->
    __upsertAdapterOfMenuDelDiaEntity.upsert(_connection, menus)
  }

  public override fun observarActivos(): Flow<List<MenuDelDiaEntity>> {
    val _sql: String = "SELECT * FROM menu_del_dia WHERE activo = 1 ORDER BY numeroDia"
    return createFlow(__db, false, arrayOf("menu_del_dia")) { _connection ->
      val _stmt: SQLiteStatement = _connection.prepare(_sql)
      try {
        val _columnIndexOfIdMenuDia: Int = getColumnIndexOrThrow(_stmt, "idMenuDia")
        val _columnIndexOfDiaSemana: Int = getColumnIndexOrThrow(_stmt, "diaSemana")
        val _columnIndexOfNumeroDia: Int = getColumnIndexOrThrow(_stmt, "numeroDia")
        val _columnIndexOfSopa: Int = getColumnIndexOrThrow(_stmt, "sopa")
        val _columnIndexOfPlatoFuerte: Int = getColumnIndexOrThrow(_stmt, "platoFuerte")
        val _columnIndexOfGuarnicion: Int = getColumnIndexOrThrow(_stmt, "guarnicion")
        val _columnIndexOfFrijolesPostre: Int = getColumnIndexOrThrow(_stmt, "frijolesPostre")
        val _columnIndexOfPrecio: Int = getColumnIndexOrThrow(_stmt, "precio")
        val _columnIndexOfStock: Int = getColumnIndexOrThrow(_stmt, "stock")
        val _columnIndexOfActivo: Int = getColumnIndexOrThrow(_stmt, "activo")
        val _result: MutableList<MenuDelDiaEntity> = mutableListOf()
        while (_stmt.step()) {
          val _item: MenuDelDiaEntity
          val _tmpIdMenuDia: Int
          _tmpIdMenuDia = _stmt.getLong(_columnIndexOfIdMenuDia).toInt()
          val _tmpDiaSemana: String
          _tmpDiaSemana = _stmt.getText(_columnIndexOfDiaSemana)
          val _tmpNumeroDia: Int
          _tmpNumeroDia = _stmt.getLong(_columnIndexOfNumeroDia).toInt()
          val _tmpSopa: String
          _tmpSopa = _stmt.getText(_columnIndexOfSopa)
          val _tmpPlatoFuerte: String
          _tmpPlatoFuerte = _stmt.getText(_columnIndexOfPlatoFuerte)
          val _tmpGuarnicion: String
          _tmpGuarnicion = _stmt.getText(_columnIndexOfGuarnicion)
          val _tmpFrijolesPostre: String
          _tmpFrijolesPostre = _stmt.getText(_columnIndexOfFrijolesPostre)
          val _tmpPrecio: Double
          _tmpPrecio = _stmt.getDouble(_columnIndexOfPrecio)
          val _tmpStock: Int
          _tmpStock = _stmt.getLong(_columnIndexOfStock).toInt()
          val _tmpActivo: Boolean
          val _tmp: Int
          _tmp = _stmt.getLong(_columnIndexOfActivo).toInt()
          _tmpActivo = _tmp != 0
          _item =
              MenuDelDiaEntity(_tmpIdMenuDia,_tmpDiaSemana,_tmpNumeroDia,_tmpSopa,_tmpPlatoFuerte,_tmpGuarnicion,_tmpFrijolesPostre,_tmpPrecio,_tmpStock,_tmpActivo)
          _result.add(_item)
        }
        _result
      } finally {
        _stmt.close()
      }
    }
  }

  public override suspend fun contar(): Int {
    val _sql: String = "SELECT COUNT(*) FROM menu_del_dia"
    return performSuspending(__db, true, false) { _connection ->
      val _stmt: SQLiteStatement = _connection.prepare(_sql)
      try {
        val _result: Int
        if (_stmt.step()) {
          val _tmp: Int
          _tmp = _stmt.getLong(0).toInt()
          _result = _tmp
        } else {
          _result = 0
        }
        _result
      } finally {
        _stmt.close()
      }
    }
  }

  public override suspend fun descontarStock(id: Int, cantidad: Int): Int {
    val _sql: String =
        "UPDATE menu_del_dia SET stock = stock - ? WHERE idMenuDia = ? AND stock >= ?"
    return performSuspending(__db, false, true) { _connection ->
      val _stmt: SQLiteStatement = _connection.prepare(_sql)
      try {
        var _argIndex: Int = 1
        _stmt.bindLong(_argIndex, cantidad.toLong())
        _argIndex = 2
        _stmt.bindLong(_argIndex, id.toLong())
        _argIndex = 3
        _stmt.bindLong(_argIndex, cantidad.toLong())
        _stmt.step()
        getTotalChangedRows(_connection)
      } finally {
        _stmt.close()
      }
    }
  }

  public companion object {
    public fun getRequiredConverters(): List<KClass<*>> = emptyList()
  }
}
