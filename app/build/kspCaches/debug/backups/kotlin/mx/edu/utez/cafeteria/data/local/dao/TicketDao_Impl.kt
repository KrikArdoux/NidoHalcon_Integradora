package mx.edu.utez.cafeteria.`data`.local.dao

import androidx.collection.LongSparseArray
import androidx.room.EntityInsertAdapter
import androidx.room.RoomDatabase
import androidx.room.coroutines.createFlow
import androidx.room.util.appendPlaceholders
import androidx.room.util.getColumnIndex
import androidx.room.util.getColumnIndexOrThrow
import androidx.room.util.performInTransactionSuspending
import androidx.room.util.performSuspending
import androidx.room.util.recursiveFetchLongSparseArray
import androidx.sqlite.SQLiteConnection
import androidx.sqlite.SQLiteStatement
import javax.`annotation`.processing.Generated
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
import mx.edu.utez.cafeteria.`data`.local.TicketConDetalles
import mx.edu.utez.cafeteria.`data`.local.entity.DetalleTicketEntity
import mx.edu.utez.cafeteria.`data`.local.entity.TicketEntity

@Generated(value = ["androidx.room.RoomProcessor"])
@Suppress(names = ["UNCHECKED_CAST", "DEPRECATION", "REDUNDANT_PROJECTION", "REMOVAL"])
public class TicketDao_Impl(
  __db: RoomDatabase,
) : TicketDao() {
  private val __db: RoomDatabase

  private val __insertAdapterOfTicketEntity: EntityInsertAdapter<TicketEntity>

  private val __insertAdapterOfDetalleTicketEntity: EntityInsertAdapter<DetalleTicketEntity>
  init {
    this.__db = __db
    this.__insertAdapterOfTicketEntity = object : EntityInsertAdapter<TicketEntity>() {
      protected override fun createQuery(): String =
          "INSERT OR ABORT INTO `tickets` (`numeroPedido`,`matriculaCajero`,`nombreCajero`,`fechaHora`,`metodoPago`,`precioTotal`) VALUES (nullif(?, 0),?,?,?,?,?)"

      protected override fun bind(statement: SQLiteStatement, entity: TicketEntity) {
        statement.bindLong(1, entity.numeroPedido.toLong())
        statement.bindText(2, entity.matriculaCajero)
        statement.bindText(3, entity.nombreCajero)
        statement.bindText(4, entity.fechaHora)
        statement.bindText(5, entity.metodoPago)
        statement.bindDouble(6, entity.precioTotal)
      }
    }
    this.__insertAdapterOfDetalleTicketEntity = object : EntityInsertAdapter<DetalleTicketEntity>()
        {
      protected override fun createQuery(): String =
          "INSERT OR ABORT INTO `detalle_tickets` (`idDetalle`,`numeroPedido`,`nombrePlatillo`,`nombreVariante`,`cantidad`,`precioUnitario`,`importe`) VALUES (nullif(?, 0),?,?,?,?,?,?)"

      protected override fun bind(statement: SQLiteStatement, entity: DetalleTicketEntity) {
        statement.bindLong(1, entity.idDetalle.toLong())
        statement.bindLong(2, entity.numeroPedido.toLong())
        statement.bindText(3, entity.nombrePlatillo)
        statement.bindText(4, entity.nombreVariante)
        statement.bindLong(5, entity.cantidad.toLong())
        statement.bindDouble(6, entity.precioUnitario)
        statement.bindDouble(7, entity.importe)
      }
    }
  }

  public override suspend fun insertarTicket(ticket: TicketEntity): Long = performSuspending(__db,
      false, true) { _connection ->
    val _result: Long = __insertAdapterOfTicketEntity.insertAndReturnId(_connection, ticket)
    _result
  }

  public override suspend fun insertarDetalles(detalles: List<DetalleTicketEntity>): Unit =
      performSuspending(__db, false, true) { _connection ->
    __insertAdapterOfDetalleTicketEntity.insert(_connection, detalles)
  }

  public override suspend fun insertarTicketConDetalles(ticket: TicketEntity,
      detalles: List<DetalleTicketEntity>): Int = performInTransactionSuspending(__db) {
    super@TicketDao_Impl.insertarTicketConDetalles(ticket, detalles)
  }

  public override fun observarPorCajero(matricula: String): Flow<List<TicketEntity>> {
    val _sql: String = "SELECT * FROM tickets WHERE matriculaCajero = ? ORDER BY numeroPedido DESC"
    return createFlow(__db, false, arrayOf("tickets")) { _connection ->
      val _stmt: SQLiteStatement = _connection.prepare(_sql)
      try {
        var _argIndex: Int = 1
        _stmt.bindText(_argIndex, matricula)
        val _columnIndexOfNumeroPedido: Int = getColumnIndexOrThrow(_stmt, "numeroPedido")
        val _columnIndexOfMatriculaCajero: Int = getColumnIndexOrThrow(_stmt, "matriculaCajero")
        val _columnIndexOfNombreCajero: Int = getColumnIndexOrThrow(_stmt, "nombreCajero")
        val _columnIndexOfFechaHora: Int = getColumnIndexOrThrow(_stmt, "fechaHora")
        val _columnIndexOfMetodoPago: Int = getColumnIndexOrThrow(_stmt, "metodoPago")
        val _columnIndexOfPrecioTotal: Int = getColumnIndexOrThrow(_stmt, "precioTotal")
        val _result: MutableList<TicketEntity> = mutableListOf()
        while (_stmt.step()) {
          val _item: TicketEntity
          val _tmpNumeroPedido: Int
          _tmpNumeroPedido = _stmt.getLong(_columnIndexOfNumeroPedido).toInt()
          val _tmpMatriculaCajero: String
          _tmpMatriculaCajero = _stmt.getText(_columnIndexOfMatriculaCajero)
          val _tmpNombreCajero: String
          _tmpNombreCajero = _stmt.getText(_columnIndexOfNombreCajero)
          val _tmpFechaHora: String
          _tmpFechaHora = _stmt.getText(_columnIndexOfFechaHora)
          val _tmpMetodoPago: String
          _tmpMetodoPago = _stmt.getText(_columnIndexOfMetodoPago)
          val _tmpPrecioTotal: Double
          _tmpPrecioTotal = _stmt.getDouble(_columnIndexOfPrecioTotal)
          _item =
              TicketEntity(_tmpNumeroPedido,_tmpMatriculaCajero,_tmpNombreCajero,_tmpFechaHora,_tmpMetodoPago,_tmpPrecioTotal)
          _result.add(_item)
        }
        _result
      } finally {
        _stmt.close()
      }
    }
  }

  public override suspend fun obtenerConDetalles(numero: Int): TicketConDetalles? {
    val _sql: String = "SELECT * FROM tickets WHERE numeroPedido = ?"
    return performSuspending(__db, true, true) { _connection ->
      val _stmt: SQLiteStatement = _connection.prepare(_sql)
      try {
        var _argIndex: Int = 1
        _stmt.bindLong(_argIndex, numero.toLong())
        val _columnIndexOfNumeroPedido: Int = getColumnIndexOrThrow(_stmt, "numeroPedido")
        val _columnIndexOfMatriculaCajero: Int = getColumnIndexOrThrow(_stmt, "matriculaCajero")
        val _columnIndexOfNombreCajero: Int = getColumnIndexOrThrow(_stmt, "nombreCajero")
        val _columnIndexOfFechaHora: Int = getColumnIndexOrThrow(_stmt, "fechaHora")
        val _columnIndexOfMetodoPago: Int = getColumnIndexOrThrow(_stmt, "metodoPago")
        val _columnIndexOfPrecioTotal: Int = getColumnIndexOrThrow(_stmt, "precioTotal")
        val _collectionDetalles: LongSparseArray<MutableList<DetalleTicketEntity>> =
            LongSparseArray<MutableList<DetalleTicketEntity>>()
        while (_stmt.step()) {
          val _tmpKey: Long
          _tmpKey = _stmt.getLong(_columnIndexOfNumeroPedido)
          if (!_collectionDetalles.containsKey(_tmpKey)) {
            _collectionDetalles.put(_tmpKey, mutableListOf())
          }
        }
        _stmt.reset()
        __fetchRelationshipdetalleTicketsAsmxEduUtezCafeteriaDataLocalEntityDetalleTicketEntity(_connection,
            _collectionDetalles)
        val _result: TicketConDetalles?
        if (_stmt.step()) {
          val _tmpTicket: TicketEntity
          val _tmpNumeroPedido: Int
          _tmpNumeroPedido = _stmt.getLong(_columnIndexOfNumeroPedido).toInt()
          val _tmpMatriculaCajero: String
          _tmpMatriculaCajero = _stmt.getText(_columnIndexOfMatriculaCajero)
          val _tmpNombreCajero: String
          _tmpNombreCajero = _stmt.getText(_columnIndexOfNombreCajero)
          val _tmpFechaHora: String
          _tmpFechaHora = _stmt.getText(_columnIndexOfFechaHora)
          val _tmpMetodoPago: String
          _tmpMetodoPago = _stmt.getText(_columnIndexOfMetodoPago)
          val _tmpPrecioTotal: Double
          _tmpPrecioTotal = _stmt.getDouble(_columnIndexOfPrecioTotal)
          _tmpTicket =
              TicketEntity(_tmpNumeroPedido,_tmpMatriculaCajero,_tmpNombreCajero,_tmpFechaHora,_tmpMetodoPago,_tmpPrecioTotal)
          val _tmpDetallesCollection: MutableList<DetalleTicketEntity>
          val _tmpKey_1: Long
          _tmpKey_1 = _stmt.getLong(_columnIndexOfNumeroPedido)
          _tmpDetallesCollection = checkNotNull(_collectionDetalles.get(_tmpKey_1))
          _result = TicketConDetalles(_tmpTicket,_tmpDetallesCollection)
        } else {
          _result = null
        }
        _result
      } finally {
        _stmt.close()
      }
    }
  }

  public override suspend fun existe(numero: Int): Int {
    val _sql: String = "SELECT COUNT(*) FROM tickets WHERE numeroPedido = ?"
    return performSuspending(__db, true, false) { _connection ->
      val _stmt: SQLiteStatement = _connection.prepare(_sql)
      try {
        var _argIndex: Int = 1
        _stmt.bindLong(_argIndex, numero.toLong())
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

  public override suspend fun existeSimilar(
    matricula: String,
    fecha: String,
    total: Double,
  ): Int {
    val _sql: String =
        "SELECT COUNT(*) FROM tickets WHERE matriculaCajero = ? AND fechaHora = ? AND precioTotal = ?"
    return performSuspending(__db, true, false) { _connection ->
      val _stmt: SQLiteStatement = _connection.prepare(_sql)
      try {
        var _argIndex: Int = 1
        _stmt.bindText(_argIndex, matricula)
        _argIndex = 2
        _stmt.bindText(_argIndex, fecha)
        _argIndex = 3
        _stmt.bindDouble(_argIndex, total)
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

  private
      fun __fetchRelationshipdetalleTicketsAsmxEduUtezCafeteriaDataLocalEntityDetalleTicketEntity(_connection: SQLiteConnection,
      _map: LongSparseArray<MutableList<DetalleTicketEntity>>) {
    if (_map.isEmpty()) {
      return
    }
    if (_map.size() > 999) {
      recursiveFetchLongSparseArray(_map, true) { _tmpMap ->
        __fetchRelationshipdetalleTicketsAsmxEduUtezCafeteriaDataLocalEntityDetalleTicketEntity(_connection,
            _tmpMap)
      }
      return
    }
    val _stringBuilder: StringBuilder = StringBuilder()
    _stringBuilder.append("SELECT `idDetalle`,`numeroPedido`,`nombrePlatillo`,`nombreVariante`,`cantidad`,`precioUnitario`,`importe` FROM `detalle_tickets` WHERE `numeroPedido` IN (")
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
      val _itemKeyIndex: Int = getColumnIndex(_stmt, "numeroPedido")
      if (_itemKeyIndex == -1) {
        return
      }
      val _columnIndexOfIdDetalle: Int = 0
      val _columnIndexOfNumeroPedido: Int = 1
      val _columnIndexOfNombrePlatillo: Int = 2
      val _columnIndexOfNombreVariante: Int = 3
      val _columnIndexOfCantidad: Int = 4
      val _columnIndexOfPrecioUnitario: Int = 5
      val _columnIndexOfImporte: Int = 6
      while (_stmt.step()) {
        val _tmpKey: Long
        _tmpKey = _stmt.getLong(_itemKeyIndex)
        val _tmpRelation: MutableList<DetalleTicketEntity>? = _map.get(_tmpKey)
        if (_tmpRelation != null) {
          val _item_1: DetalleTicketEntity
          val _tmpIdDetalle: Int
          _tmpIdDetalle = _stmt.getLong(_columnIndexOfIdDetalle).toInt()
          val _tmpNumeroPedido: Int
          _tmpNumeroPedido = _stmt.getLong(_columnIndexOfNumeroPedido).toInt()
          val _tmpNombrePlatillo: String
          _tmpNombrePlatillo = _stmt.getText(_columnIndexOfNombrePlatillo)
          val _tmpNombreVariante: String
          _tmpNombreVariante = _stmt.getText(_columnIndexOfNombreVariante)
          val _tmpCantidad: Int
          _tmpCantidad = _stmt.getLong(_columnIndexOfCantidad).toInt()
          val _tmpPrecioUnitario: Double
          _tmpPrecioUnitario = _stmt.getDouble(_columnIndexOfPrecioUnitario)
          val _tmpImporte: Double
          _tmpImporte = _stmt.getDouble(_columnIndexOfImporte)
          _item_1 =
              DetalleTicketEntity(_tmpIdDetalle,_tmpNumeroPedido,_tmpNombrePlatillo,_tmpNombreVariante,_tmpCantidad,_tmpPrecioUnitario,_tmpImporte)
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
