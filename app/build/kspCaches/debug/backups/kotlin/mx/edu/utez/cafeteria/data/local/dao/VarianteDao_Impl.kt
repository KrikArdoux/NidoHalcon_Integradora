package mx.edu.utez.cafeteria.`data`.local.dao

import androidx.room.EntityDeleteOrUpdateAdapter
import androidx.room.EntityInsertAdapter
import androidx.room.EntityUpsertAdapter
import androidx.room.RoomDatabase
import androidx.room.util.getColumnIndexOrThrow
import androidx.room.util.getTotalChangedRows
import androidx.room.util.performSuspending
import androidx.sqlite.SQLiteStatement
import javax.`annotation`.processing.Generated
import kotlin.Boolean
import kotlin.Double
import kotlin.Int
import kotlin.Long
import kotlin.String
import kotlin.Suppress
import kotlin.Unit
import kotlin.collections.List
import kotlin.reflect.KClass
import mx.edu.utez.cafeteria.`data`.local.entity.VarianteEntity

@Generated(value = ["androidx.room.RoomProcessor"])
@Suppress(names = ["UNCHECKED_CAST", "DEPRECATION", "REDUNDANT_PROJECTION", "REMOVAL"])
public class VarianteDao_Impl(
  __db: RoomDatabase,
) : VarianteDao {
  private val __db: RoomDatabase

  private val __insertAdapterOfVarianteEntity: EntityInsertAdapter<VarianteEntity>

  private val __updateAdapterOfVarianteEntity: EntityDeleteOrUpdateAdapter<VarianteEntity>

  private val __upsertAdapterOfVarianteEntity: EntityUpsertAdapter<VarianteEntity>
  init {
    this.__db = __db
    this.__insertAdapterOfVarianteEntity = object : EntityInsertAdapter<VarianteEntity>() {
      protected override fun createQuery(): String =
          "INSERT OR ABORT INTO `variantes` (`idVariante`,`idPlatillo`,`nombreVariante`,`detalle`,`precioExtra`,`stock`,`activo`) VALUES (nullif(?, 0),?,?,?,?,?,?)"

      protected override fun bind(statement: SQLiteStatement, entity: VarianteEntity) {
        statement.bindLong(1, entity.idVariante.toLong())
        statement.bindLong(2, entity.idPlatillo.toLong())
        statement.bindText(3, entity.nombreVariante)
        val _tmpDetalle: String? = entity.detalle
        if (_tmpDetalle == null) {
          statement.bindNull(4)
        } else {
          statement.bindText(4, _tmpDetalle)
        }
        statement.bindDouble(5, entity.precioExtra)
        statement.bindLong(6, entity.stock.toLong())
        val _tmp: Int = if (entity.activo) 1 else 0
        statement.bindLong(7, _tmp.toLong())
      }
    }
    this.__updateAdapterOfVarianteEntity = object : EntityDeleteOrUpdateAdapter<VarianteEntity>() {
      protected override fun createQuery(): String =
          "UPDATE OR ABORT `variantes` SET `idVariante` = ?,`idPlatillo` = ?,`nombreVariante` = ?,`detalle` = ?,`precioExtra` = ?,`stock` = ?,`activo` = ? WHERE `idVariante` = ?"

      protected override fun bind(statement: SQLiteStatement, entity: VarianteEntity) {
        statement.bindLong(1, entity.idVariante.toLong())
        statement.bindLong(2, entity.idPlatillo.toLong())
        statement.bindText(3, entity.nombreVariante)
        val _tmpDetalle: String? = entity.detalle
        if (_tmpDetalle == null) {
          statement.bindNull(4)
        } else {
          statement.bindText(4, _tmpDetalle)
        }
        statement.bindDouble(5, entity.precioExtra)
        statement.bindLong(6, entity.stock.toLong())
        val _tmp: Int = if (entity.activo) 1 else 0
        statement.bindLong(7, _tmp.toLong())
        statement.bindLong(8, entity.idVariante.toLong())
      }
    }
    this.__upsertAdapterOfVarianteEntity = EntityUpsertAdapter<VarianteEntity>(object :
        EntityInsertAdapter<VarianteEntity>() {
      protected override fun createQuery(): String =
          "INSERT INTO `variantes` (`idVariante`,`idPlatillo`,`nombreVariante`,`detalle`,`precioExtra`,`stock`,`activo`) VALUES (nullif(?, 0),?,?,?,?,?,?)"

      protected override fun bind(statement: SQLiteStatement, entity: VarianteEntity) {
        statement.bindLong(1, entity.idVariante.toLong())
        statement.bindLong(2, entity.idPlatillo.toLong())
        statement.bindText(3, entity.nombreVariante)
        val _tmpDetalle: String? = entity.detalle
        if (_tmpDetalle == null) {
          statement.bindNull(4)
        } else {
          statement.bindText(4, _tmpDetalle)
        }
        statement.bindDouble(5, entity.precioExtra)
        statement.bindLong(6, entity.stock.toLong())
        val _tmp: Int = if (entity.activo) 1 else 0
        statement.bindLong(7, _tmp.toLong())
      }
    }, object : EntityDeleteOrUpdateAdapter<VarianteEntity>() {
      protected override fun createQuery(): String =
          "UPDATE `variantes` SET `idVariante` = ?,`idPlatillo` = ?,`nombreVariante` = ?,`detalle` = ?,`precioExtra` = ?,`stock` = ?,`activo` = ? WHERE `idVariante` = ?"

      protected override fun bind(statement: SQLiteStatement, entity: VarianteEntity) {
        statement.bindLong(1, entity.idVariante.toLong())
        statement.bindLong(2, entity.idPlatillo.toLong())
        statement.bindText(3, entity.nombreVariante)
        val _tmpDetalle: String? = entity.detalle
        if (_tmpDetalle == null) {
          statement.bindNull(4)
        } else {
          statement.bindText(4, _tmpDetalle)
        }
        statement.bindDouble(5, entity.precioExtra)
        statement.bindLong(6, entity.stock.toLong())
        val _tmp: Int = if (entity.activo) 1 else 0
        statement.bindLong(7, _tmp.toLong())
        statement.bindLong(8, entity.idVariante.toLong())
      }
    })
  }

  public override suspend fun insertar(variante: VarianteEntity): Long = performSuspending(__db,
      false, true) { _connection ->
    val _result: Long = __insertAdapterOfVarianteEntity.insertAndReturnId(_connection, variante)
    _result
  }

  public override suspend fun insertarTodas(variantes: List<VarianteEntity>): Unit =
      performSuspending(__db, false, true) { _connection ->
    __insertAdapterOfVarianteEntity.insert(_connection, variantes)
  }

  public override suspend fun actualizar(variante: VarianteEntity): Unit = performSuspending(__db,
      false, true) { _connection ->
    __updateAdapterOfVarianteEntity.handle(_connection, variante)
  }

  public override suspend fun upsertTodas(variantes: List<VarianteEntity>): Unit =
      performSuspending(__db, false, true) { _connection ->
    __upsertAdapterOfVarianteEntity.upsert(_connection, variantes)
  }

  public override suspend fun obtener(id: Int): VarianteEntity? {
    val _sql: String = "SELECT * FROM variantes WHERE idVariante = ?"
    return performSuspending(__db, true, false) { _connection ->
      val _stmt: SQLiteStatement = _connection.prepare(_sql)
      try {
        var _argIndex: Int = 1
        _stmt.bindLong(_argIndex, id.toLong())
        val _columnIndexOfIdVariante: Int = getColumnIndexOrThrow(_stmt, "idVariante")
        val _columnIndexOfIdPlatillo: Int = getColumnIndexOrThrow(_stmt, "idPlatillo")
        val _columnIndexOfNombreVariante: Int = getColumnIndexOrThrow(_stmt, "nombreVariante")
        val _columnIndexOfDetalle: Int = getColumnIndexOrThrow(_stmt, "detalle")
        val _columnIndexOfPrecioExtra: Int = getColumnIndexOrThrow(_stmt, "precioExtra")
        val _columnIndexOfStock: Int = getColumnIndexOrThrow(_stmt, "stock")
        val _columnIndexOfActivo: Int = getColumnIndexOrThrow(_stmt, "activo")
        val _result: VarianteEntity?
        if (_stmt.step()) {
          val _tmpIdVariante: Int
          _tmpIdVariante = _stmt.getLong(_columnIndexOfIdVariante).toInt()
          val _tmpIdPlatillo: Int
          _tmpIdPlatillo = _stmt.getLong(_columnIndexOfIdPlatillo).toInt()
          val _tmpNombreVariante: String
          _tmpNombreVariante = _stmt.getText(_columnIndexOfNombreVariante)
          val _tmpDetalle: String?
          if (_stmt.isNull(_columnIndexOfDetalle)) {
            _tmpDetalle = null
          } else {
            _tmpDetalle = _stmt.getText(_columnIndexOfDetalle)
          }
          val _tmpPrecioExtra: Double
          _tmpPrecioExtra = _stmt.getDouble(_columnIndexOfPrecioExtra)
          val _tmpStock: Int
          _tmpStock = _stmt.getLong(_columnIndexOfStock).toInt()
          val _tmpActivo: Boolean
          val _tmp: Int
          _tmp = _stmt.getLong(_columnIndexOfActivo).toInt()
          _tmpActivo = _tmp != 0
          _result =
              VarianteEntity(_tmpIdVariante,_tmpIdPlatillo,_tmpNombreVariante,_tmpDetalle,_tmpPrecioExtra,_tmpStock,_tmpActivo)
        } else {
          _result = null
        }
        _result
      } finally {
        _stmt.close()
      }
    }
  }

  public override suspend fun desactivar(id: Int) {
    val _sql: String = "UPDATE variantes SET activo = 0 WHERE idVariante = ?"
    return performSuspending(__db, false, true) { _connection ->
      val _stmt: SQLiteStatement = _connection.prepare(_sql)
      try {
        var _argIndex: Int = 1
        _stmt.bindLong(_argIndex, id.toLong())
        _stmt.step()
      } finally {
        _stmt.close()
      }
    }
  }

  public override suspend fun descontarStock(id: Int, cantidad: Int): Int {
    val _sql: String = "UPDATE variantes SET stock = stock - ? WHERE idVariante = ? AND stock >= ?"
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
