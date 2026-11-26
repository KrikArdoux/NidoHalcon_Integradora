package mx.edu.utez.cafeteria.`data`.local.dao

import androidx.room.EntityDeleteOrUpdateAdapter
import androidx.room.EntityInsertAdapter
import androidx.room.EntityUpsertAdapter
import androidx.room.RoomDatabase
import androidx.room.util.getColumnIndexOrThrow
import androidx.room.util.performSuspending
import androidx.sqlite.SQLiteStatement
import javax.`annotation`.processing.Generated
import kotlin.Int
import kotlin.String
import kotlin.Suppress
import kotlin.Unit
import kotlin.collections.List
import kotlin.reflect.KClass
import mx.edu.utez.cafeteria.`data`.local.entity.CajeroEntity

@Generated(value = ["androidx.room.RoomProcessor"])
@Suppress(names = ["UNCHECKED_CAST", "DEPRECATION", "REDUNDANT_PROJECTION", "REMOVAL"])
public class CajeroDao_Impl(
  __db: RoomDatabase,
) : CajeroDao {
  private val __db: RoomDatabase

  private val __upsertAdapterOfCajeroEntity: EntityUpsertAdapter<CajeroEntity>
  init {
    this.__db = __db
    this.__upsertAdapterOfCajeroEntity = EntityUpsertAdapter<CajeroEntity>(object :
        EntityInsertAdapter<CajeroEntity>() {
      protected override fun createQuery(): String =
          "INSERT INTO `cajeros` (`matricula`,`nombreCompleto`,`passwordHash`) VALUES (?,?,?)"

      protected override fun bind(statement: SQLiteStatement, entity: CajeroEntity) {
        statement.bindText(1, entity.matricula)
        statement.bindText(2, entity.nombreCompleto)
        statement.bindText(3, entity.passwordHash)
      }
    }, object : EntityDeleteOrUpdateAdapter<CajeroEntity>() {
      protected override fun createQuery(): String =
          "UPDATE `cajeros` SET `matricula` = ?,`nombreCompleto` = ?,`passwordHash` = ? WHERE `matricula` = ?"

      protected override fun bind(statement: SQLiteStatement, entity: CajeroEntity) {
        statement.bindText(1, entity.matricula)
        statement.bindText(2, entity.nombreCompleto)
        statement.bindText(3, entity.passwordHash)
        statement.bindText(4, entity.matricula)
      }
    })
  }

  public override suspend fun upsert(cajero: CajeroEntity): Unit = performSuspending(__db, false,
      true) { _connection ->
    __upsertAdapterOfCajeroEntity.upsert(_connection, cajero)
  }

  public override suspend fun buscar(matricula: String): CajeroEntity? {
    val _sql: String = "SELECT * FROM cajeros WHERE matricula = ? LIMIT 1"
    return performSuspending(__db, true, false) { _connection ->
      val _stmt: SQLiteStatement = _connection.prepare(_sql)
      try {
        var _argIndex: Int = 1
        _stmt.bindText(_argIndex, matricula)
        val _columnIndexOfMatricula: Int = getColumnIndexOrThrow(_stmt, "matricula")
        val _columnIndexOfNombreCompleto: Int = getColumnIndexOrThrow(_stmt, "nombreCompleto")
        val _columnIndexOfPasswordHash: Int = getColumnIndexOrThrow(_stmt, "passwordHash")
        val _result: CajeroEntity?
        if (_stmt.step()) {
          val _tmpMatricula: String
          _tmpMatricula = _stmt.getText(_columnIndexOfMatricula)
          val _tmpNombreCompleto: String
          _tmpNombreCompleto = _stmt.getText(_columnIndexOfNombreCompleto)
          val _tmpPasswordHash: String
          _tmpPasswordHash = _stmt.getText(_columnIndexOfPasswordHash)
          _result = CajeroEntity(_tmpMatricula,_tmpNombreCompleto,_tmpPasswordHash)
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
    val _sql: String = "SELECT COUNT(*) FROM cajeros"
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

  public companion object {
    public fun getRequiredConverters(): List<KClass<*>> = emptyList()
  }
}
