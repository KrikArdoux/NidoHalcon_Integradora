package mx.edu.utez.cafeteria.`data`.local.dao

import androidx.collection.LongSparseArray
import androidx.room.EntityDeleteOrUpdateAdapter
import androidx.room.EntityInsertAdapter
import androidx.room.EntityUpsertAdapter
import androidx.room.RoomDatabase
import androidx.room.coroutines.createFlow
import androidx.room.util.appendPlaceholders
import androidx.room.util.getColumnIndex
import androidx.room.util.getColumnIndexOrThrow
import androidx.room.util.performSuspending
import androidx.room.util.recursiveFetchLongSparseArray
import androidx.sqlite.SQLiteConnection
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
import kotlin.collections.MutableList
import kotlin.collections.mutableListOf
import kotlin.reflect.KClass
import kotlin.text.StringBuilder
import kotlinx.coroutines.flow.Flow
import mx.edu.utez.cafeteria.`data`.local.PlatilloConVariantes
import mx.edu.utez.cafeteria.`data`.local.entity.PlatilloEntity
import mx.edu.utez.cafeteria.`data`.local.entity.VarianteEntity

@Generated(value = ["androidx.room.RoomProcessor"])
@Suppress(names = ["UNCHECKED_CAST", "DEPRECATION", "REDUNDANT_PROJECTION", "REMOVAL"])
public class PlatilloDao_Impl(
  __db: RoomDatabase,
) : PlatilloDao {
  private val __db: RoomDatabase

  private val __insertAdapterOfPlatilloEntity: EntityInsertAdapter<PlatilloEntity>

  private val __updateAdapterOfPlatilloEntity: EntityDeleteOrUpdateAdapter<PlatilloEntity>

  private val __upsertAdapterOfPlatilloEntity: EntityUpsertAdapter<PlatilloEntity>
  init {
    this.__db = __db
    this.__insertAdapterOfPlatilloEntity = object : EntityInsertAdapter<PlatilloEntity>() {
      protected override fun createQuery(): String =
          "INSERT OR ABORT INTO `platillos` (`idPlatillo`,`nombre`,`descripcion`,`categoria`,`precioBase`,`imagenUri`,`activo`) VALUES (nullif(?, 0),?,?,?,?,?,?)"

      protected override fun bind(statement: SQLiteStatement, entity: PlatilloEntity) {
        statement.bindLong(1, entity.idPlatillo.toLong())
        statement.bindText(2, entity.nombre)
        statement.bindText(3, entity.descripcion)
        statement.bindText(4, entity.categoria)
        statement.bindDouble(5, entity.precioBase)
        val _tmpImagenUri: String? = entity.imagenUri
        if (_tmpImagenUri == null) {
          statement.bindNull(6)
        } else {
          statement.bindText(6, _tmpImagenUri)
        }
        val _tmp: Int = if (entity.activo) 1 else 0
        statement.bindLong(7, _tmp.toLong())
      }
    }
    this.__updateAdapterOfPlatilloEntity = object : EntityDeleteOrUpdateAdapter<PlatilloEntity>() {
      protected override fun createQuery(): String =
          "UPDATE OR ABORT `platillos` SET `idPlatillo` = ?,`nombre` = ?,`descripcion` = ?,`categoria` = ?,`precioBase` = ?,`imagenUri` = ?,`activo` = ? WHERE `idPlatillo` = ?"

      protected override fun bind(statement: SQLiteStatement, entity: PlatilloEntity) {
        statement.bindLong(1, entity.idPlatillo.toLong())
        statement.bindText(2, entity.nombre)
        statement.bindText(3, entity.descripcion)
        statement.bindText(4, entity.categoria)
        statement.bindDouble(5, entity.precioBase)
        val _tmpImagenUri: String? = entity.imagenUri
        if (_tmpImagenUri == null) {
          statement.bindNull(6)
        } else {
          statement.bindText(6, _tmpImagenUri)
        }
        val _tmp: Int = if (entity.activo) 1 else 0
        statement.bindLong(7, _tmp.toLong())
        statement.bindLong(8, entity.idPlatillo.toLong())
      }
    }
    this.__upsertAdapterOfPlatilloEntity = EntityUpsertAdapter<PlatilloEntity>(object :
        EntityInsertAdapter<PlatilloEntity>() {
      protected override fun createQuery(): String =
          "INSERT INTO `platillos` (`idPlatillo`,`nombre`,`descripcion`,`categoria`,`precioBase`,`imagenUri`,`activo`) VALUES (nullif(?, 0),?,?,?,?,?,?)"

      protected override fun bind(statement: SQLiteStatement, entity: PlatilloEntity) {
        statement.bindLong(1, entity.idPlatillo.toLong())
        statement.bindText(2, entity.nombre)
        statement.bindText(3, entity.descripcion)
        statement.bindText(4, entity.categoria)
        statement.bindDouble(5, entity.precioBase)
        val _tmpImagenUri: String? = entity.imagenUri
        if (_tmpImagenUri == null) {
          statement.bindNull(6)
        } else {
          statement.bindText(6, _tmpImagenUri)
        }
        val _tmp: Int = if (entity.activo) 1 else 0
        statement.bindLong(7, _tmp.toLong())
      }
    }, object : EntityDeleteOrUpdateAdapter<PlatilloEntity>() {
      protected override fun createQuery(): String =
          "UPDATE `platillos` SET `idPlatillo` = ?,`nombre` = ?,`descripcion` = ?,`categoria` = ?,`precioBase` = ?,`imagenUri` = ?,`activo` = ? WHERE `idPlatillo` = ?"

      protected override fun bind(statement: SQLiteStatement, entity: PlatilloEntity) {
        statement.bindLong(1, entity.idPlatillo.toLong())
        statement.bindText(2, entity.nombre)
        statement.bindText(3, entity.descripcion)
        statement.bindText(4, entity.categoria)
        statement.bindDouble(5, entity.precioBase)
        val _tmpImagenUri: String? = entity.imagenUri
        if (_tmpImagenUri == null) {
          statement.bindNull(6)
        } else {
          statement.bindText(6, _tmpImagenUri)
        }
        val _tmp: Int = if (entity.activo) 1 else 0
        statement.bindLong(7, _tmp.toLong())
        statement.bindLong(8, entity.idPlatillo.toLong())
      }
    })
  }

  public override suspend fun insertar(platillo: PlatilloEntity): Long = performSuspending(__db,
      false, true) { _connection ->
    val _result: Long = __insertAdapterOfPlatilloEntity.insertAndReturnId(_connection, platillo)
    _result
  }

  public override suspend fun actualizar(platillo: PlatilloEntity): Unit = performSuspending(__db,
      false, true) { _connection ->
    __updateAdapterOfPlatilloEntity.handle(_connection, platillo)
  }

  public override suspend fun upsert(platillo: PlatilloEntity): Unit = performSuspending(__db,
      false, true) { _connection ->
    __upsertAdapterOfPlatilloEntity.upsert(_connection, platillo)
  }

  public override fun observarActivos(): Flow<List<PlatilloConVariantes>> {
    val _sql: String = "SELECT * FROM platillos WHERE activo = 1 ORDER BY idPlatillo"
    return createFlow(__db, true, arrayOf("variantes", "platillos")) { _connection ->
      val _stmt: SQLiteStatement = _connection.prepare(_sql)
      try {
        val _columnIndexOfIdPlatillo: Int = getColumnIndexOrThrow(_stmt, "idPlatillo")
        val _columnIndexOfNombre: Int = getColumnIndexOrThrow(_stmt, "nombre")
        val _columnIndexOfDescripcion: Int = getColumnIndexOrThrow(_stmt, "descripcion")
        val _columnIndexOfCategoria: Int = getColumnIndexOrThrow(_stmt, "categoria")
        val _columnIndexOfPrecioBase: Int = getColumnIndexOrThrow(_stmt, "precioBase")
        val _columnIndexOfImagenUri: Int = getColumnIndexOrThrow(_stmt, "imagenUri")
        val _columnIndexOfActivo: Int = getColumnIndexOrThrow(_stmt, "activo")
        val _collectionVariantes: LongSparseArray<MutableList<VarianteEntity>> =
            LongSparseArray<MutableList<VarianteEntity>>()
        while (_stmt.step()) {
          val _tmpKey: Long
          _tmpKey = _stmt.getLong(_columnIndexOfIdPlatillo)
          if (!_collectionVariantes.containsKey(_tmpKey)) {
            _collectionVariantes.put(_tmpKey, mutableListOf())
          }
        }
        _stmt.reset()
        __fetchRelationshipvariantesAsmxEduUtezCafeteriaDataLocalEntityVarianteEntity(_connection,
            _collectionVariantes)
        val _result: MutableList<PlatilloConVariantes> = mutableListOf()
        while (_stmt.step()) {
          val _item: PlatilloConVariantes
          val _tmpPlatillo: PlatilloEntity
          val _tmpIdPlatillo: Int
          _tmpIdPlatillo = _stmt.getLong(_columnIndexOfIdPlatillo).toInt()
          val _tmpNombre: String
          _tmpNombre = _stmt.getText(_columnIndexOfNombre)
          val _tmpDescripcion: String
          _tmpDescripcion = _stmt.getText(_columnIndexOfDescripcion)
          val _tmpCategoria: String
          _tmpCategoria = _stmt.getText(_columnIndexOfCategoria)
          val _tmpPrecioBase: Double
          _tmpPrecioBase = _stmt.getDouble(_columnIndexOfPrecioBase)
          val _tmpImagenUri: String?
          if (_stmt.isNull(_columnIndexOfImagenUri)) {
            _tmpImagenUri = null
          } else {
            _tmpImagenUri = _stmt.getText(_columnIndexOfImagenUri)
          }
          val _tmpActivo: Boolean
          val _tmp: Int
          _tmp = _stmt.getLong(_columnIndexOfActivo).toInt()
          _tmpActivo = _tmp != 0
          _tmpPlatillo =
              PlatilloEntity(_tmpIdPlatillo,_tmpNombre,_tmpDescripcion,_tmpCategoria,_tmpPrecioBase,_tmpImagenUri,_tmpActivo)
          val _tmpVariantesCollection: MutableList<VarianteEntity>
          val _tmpKey_1: Long
          _tmpKey_1 = _stmt.getLong(_columnIndexOfIdPlatillo)
          _tmpVariantesCollection = checkNotNull(_collectionVariantes.get(_tmpKey_1))
          _item = PlatilloConVariantes(_tmpPlatillo,_tmpVariantesCollection)
          _result.add(_item)
        }
        _result
      } finally {
        _stmt.close()
      }
    }
  }

  public override suspend fun obtener(id: Int): PlatilloConVariantes? {
    val _sql: String = "SELECT * FROM platillos WHERE idPlatillo = ?"
    return performSuspending(__db, true, true) { _connection ->
      val _stmt: SQLiteStatement = _connection.prepare(_sql)
      try {
        var _argIndex: Int = 1
        _stmt.bindLong(_argIndex, id.toLong())
        val _columnIndexOfIdPlatillo: Int = getColumnIndexOrThrow(_stmt, "idPlatillo")
        val _columnIndexOfNombre: Int = getColumnIndexOrThrow(_stmt, "nombre")
        val _columnIndexOfDescripcion: Int = getColumnIndexOrThrow(_stmt, "descripcion")
        val _columnIndexOfCategoria: Int = getColumnIndexOrThrow(_stmt, "categoria")
        val _columnIndexOfPrecioBase: Int = getColumnIndexOrThrow(_stmt, "precioBase")
        val _columnIndexOfImagenUri: Int = getColumnIndexOrThrow(_stmt, "imagenUri")
        val _columnIndexOfActivo: Int = getColumnIndexOrThrow(_stmt, "activo")
        val _collectionVariantes: LongSparseArray<MutableList<VarianteEntity>> =
            LongSparseArray<MutableList<VarianteEntity>>()
        while (_stmt.step()) {
          val _tmpKey: Long
          _tmpKey = _stmt.getLong(_columnIndexOfIdPlatillo)
          if (!_collectionVariantes.containsKey(_tmpKey)) {
            _collectionVariantes.put(_tmpKey, mutableListOf())
          }
        }
        _stmt.reset()
        __fetchRelationshipvariantesAsmxEduUtezCafeteriaDataLocalEntityVarianteEntity(_connection,
            _collectionVariantes)
        val _result: PlatilloConVariantes?
        if (_stmt.step()) {
          val _tmpPlatillo: PlatilloEntity
          val _tmpIdPlatillo: Int
          _tmpIdPlatillo = _stmt.getLong(_columnIndexOfIdPlatillo).toInt()
          val _tmpNombre: String
          _tmpNombre = _stmt.getText(_columnIndexOfNombre)
          val _tmpDescripcion: String
          _tmpDescripcion = _stmt.getText(_columnIndexOfDescripcion)
          val _tmpCategoria: String
          _tmpCategoria = _stmt.getText(_columnIndexOfCategoria)
          val _tmpPrecioBase: Double
          _tmpPrecioBase = _stmt.getDouble(_columnIndexOfPrecioBase)
          val _tmpImagenUri: String?
          if (_stmt.isNull(_columnIndexOfImagenUri)) {
            _tmpImagenUri = null
          } else {
            _tmpImagenUri = _stmt.getText(_columnIndexOfImagenUri)
          }
          val _tmpActivo: Boolean
          val _tmp: Int
          _tmp = _stmt.getLong(_columnIndexOfActivo).toInt()
          _tmpActivo = _tmp != 0
          _tmpPlatillo =
              PlatilloEntity(_tmpIdPlatillo,_tmpNombre,_tmpDescripcion,_tmpCategoria,_tmpPrecioBase,_tmpImagenUri,_tmpActivo)
          val _tmpVariantesCollection: MutableList<VarianteEntity>
          val _tmpKey_1: Long
          _tmpKey_1 = _stmt.getLong(_columnIndexOfIdPlatillo)
          _tmpVariantesCollection = checkNotNull(_collectionVariantes.get(_tmpKey_1))
          _result = PlatilloConVariantes(_tmpPlatillo,_tmpVariantesCollection)
        } else {
          _result = null
        }
        _result
      } finally {
        _stmt.close()
      }
    }
  }

  public override suspend fun contar(): Int {
    val _sql: String = "SELECT COUNT(*) FROM platillos"
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

  public override suspend fun desactivar(id: Int) {
    val _sql: String = "UPDATE platillos SET activo = 0 WHERE idPlatillo = ?"
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

  private
      fun __fetchRelationshipvariantesAsmxEduUtezCafeteriaDataLocalEntityVarianteEntity(_connection: SQLiteConnection,
      _map: LongSparseArray<MutableList<VarianteEntity>>) {
    if (_map.isEmpty()) {
      return
    }
    if (_map.size() > 999) {
      recursiveFetchLongSparseArray(_map, true) { _tmpMap ->
        __fetchRelationshipvariantesAsmxEduUtezCafeteriaDataLocalEntityVarianteEntity(_connection,
            _tmpMap)
      }
      return
    }
    val _stringBuilder: StringBuilder = StringBuilder()
    _stringBuilder.append("SELECT `idVariante`,`idPlatillo`,`nombreVariante`,`detalle`,`precioExtra`,`stock`,`activo` FROM `variantes` WHERE `idPlatillo` IN (")
    val _inputSize: Int = _map.size()
    appendPlaceholders(_stringBuilder, _inputSize)
    _stringBuilder.append(")")
    val _sql: String = _stringBuilder.toString()
    val _stmt: SQLiteStatement = _connection.prepare(_sql)
    var _argIndex: Int = 1
    for (i in 0 until _map.size()) {
      val _item: Long = _map.keyAt(i)
      _stmt.bindLong(_argIndex, _item)
      _argIndex++
    }
    try {
      val _itemKeyIndex: Int = getColumnIndex(_stmt, "idPlatillo")
      if (_itemKeyIndex == -1) {
        return
      }
      val _columnIndexOfIdVariante: Int = 0
      val _columnIndexOfIdPlatillo: Int = 1
      val _columnIndexOfNombreVariante: Int = 2
      val _columnIndexOfDetalle: Int = 3
      val _columnIndexOfPrecioExtra: Int = 4
      val _columnIndexOfStock: Int = 5
      val _columnIndexOfActivo: Int = 6
      while (_stmt.step()) {
        val _tmpKey: Long
        _tmpKey = _stmt.getLong(_itemKeyIndex)
        val _tmpRelation: MutableList<VarianteEntity>? = _map.get(_tmpKey)
        if (_tmpRelation != null) {
          val _item_1: VarianteEntity
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
          _item_1 =
              VarianteEntity(_tmpIdVariante,_tmpIdPlatillo,_tmpNombreVariante,_tmpDetalle,_tmpPrecioExtra,_tmpStock,_tmpActivo)
          _tmpRelation.add(_item_1)
        }
      }
    } finally {
      _stmt.close()
    }
  }

  public companion object {
    public fun getRequiredConverters(): List<KClass<*>> = emptyList()
  }
}
