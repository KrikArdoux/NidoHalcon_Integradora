package mx.edu.utez.cafeteria.`data`.local

import androidx.room.InvalidationTracker
import androidx.room.RoomOpenDelegate
import androidx.room.migration.AutoMigrationSpec
import androidx.room.migration.Migration
import androidx.room.util.TableInfo
import androidx.room.util.TableInfo.Companion.read
import androidx.room.util.dropFtsSyncTriggers
import androidx.sqlite.SQLiteConnection
import androidx.sqlite.execSQL
import javax.`annotation`.processing.Generated
import kotlin.Lazy
import kotlin.String
import kotlin.Suppress
import kotlin.collections.List
import kotlin.collections.Map
import kotlin.collections.MutableList
import kotlin.collections.MutableMap
import kotlin.collections.MutableSet
import kotlin.collections.Set
import kotlin.collections.mutableListOf
import kotlin.collections.mutableMapOf
import kotlin.collections.mutableSetOf
import kotlin.reflect.KClass
import mx.edu.utez.cafeteria.`data`.local.dao.CajeroDao
import mx.edu.utez.cafeteria.`data`.local.dao.CajeroDao_Impl
import mx.edu.utez.cafeteria.`data`.local.dao.MenuDelDiaDao
import mx.edu.utez.cafeteria.`data`.local.dao.MenuDelDiaDao_Impl
import mx.edu.utez.cafeteria.`data`.local.dao.PlatilloDao
import mx.edu.utez.cafeteria.`data`.local.dao.PlatilloDao_Impl
import mx.edu.utez.cafeteria.`data`.local.dao.TicketDao
import mx.edu.utez.cafeteria.`data`.local.dao.TicketDao_Impl
import mx.edu.utez.cafeteria.`data`.local.dao.VarianteDao
import mx.edu.utez.cafeteria.`data`.local.dao.VarianteDao_Impl

@Generated(value = ["androidx.room.RoomProcessor"])
@Suppress(names = ["UNCHECKED_CAST", "DEPRECATION", "REDUNDANT_PROJECTION", "REMOVAL"])
public class AppDatabase_Impl : AppDatabase() {
  private val _cajeroDao: Lazy<CajeroDao> = lazy {
    CajeroDao_Impl(this)
  }

  private val _platilloDao: Lazy<PlatilloDao> = lazy {
    PlatilloDao_Impl(this)
  }

  private val _varianteDao: Lazy<VarianteDao> = lazy {
    VarianteDao_Impl(this)
  }

  private val _menuDelDiaDao: Lazy<MenuDelDiaDao> = lazy {
    MenuDelDiaDao_Impl(this)
  }

  private val _ticketDao: Lazy<TicketDao> = lazy {
    TicketDao_Impl(this)
  }

  protected override fun createOpenDelegate(): RoomOpenDelegate {
    val _openDelegate: RoomOpenDelegate = object : RoomOpenDelegate(1,
        "8df925e4ce16b23e95dd102e9c14a806", "54e6526280efb8bf78058b9aa18487b9") {
      public override fun createAllTables(connection: SQLiteConnection) {
        connection.execSQL("CREATE TABLE IF NOT EXISTS `cajeros` (`matricula` TEXT NOT NULL, `nombreCompleto` TEXT NOT NULL, `passwordHash` TEXT NOT NULL, PRIMARY KEY(`matricula`))")
        connection.execSQL("CREATE TABLE IF NOT EXISTS `platillos` (`idPlatillo` INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, `nombre` TEXT NOT NULL, `descripcion` TEXT NOT NULL, `categoria` TEXT NOT NULL, `precioBase` REAL NOT NULL, `imagenUri` TEXT, `activo` INTEGER NOT NULL)")
        connection.execSQL("CREATE TABLE IF NOT EXISTS `variantes` (`idVariante` INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, `idPlatillo` INTEGER NOT NULL, `nombreVariante` TEXT NOT NULL, `detalle` TEXT, `precioExtra` REAL NOT NULL, `stock` INTEGER NOT NULL, `activo` INTEGER NOT NULL, FOREIGN KEY(`idPlatillo`) REFERENCES `platillos`(`idPlatillo`) ON UPDATE NO ACTION ON DELETE CASCADE )")
        connection.execSQL("CREATE INDEX IF NOT EXISTS `index_variantes_idPlatillo` ON `variantes` (`idPlatillo`)")
        connection.execSQL("CREATE TABLE IF NOT EXISTS `menu_del_dia` (`idMenuDia` INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, `diaSemana` TEXT NOT NULL, `numeroDia` INTEGER NOT NULL, `sopa` TEXT NOT NULL, `platoFuerte` TEXT NOT NULL, `guarnicion` TEXT NOT NULL, `frijolesPostre` TEXT NOT NULL, `precio` REAL NOT NULL, `stock` INTEGER NOT NULL, `activo` INTEGER NOT NULL)")
        connection.execSQL("CREATE TABLE IF NOT EXISTS `tickets` (`numeroPedido` INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, `matriculaCajero` TEXT NOT NULL, `nombreCajero` TEXT NOT NULL, `fechaHora` TEXT NOT NULL, `metodoPago` TEXT NOT NULL, `precioTotal` REAL NOT NULL)")
        connection.execSQL("CREATE TABLE IF NOT EXISTS `detalle_tickets` (`idDetalle` INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, `numeroPedido` INTEGER NOT NULL, `nombrePlatillo` TEXT NOT NULL, `nombreVariante` TEXT NOT NULL, `cantidad` INTEGER NOT NULL, `precioUnitario` REAL NOT NULL, `importe` REAL NOT NULL, FOREIGN KEY(`numeroPedido`) REFERENCES `tickets`(`numeroPedido`) ON UPDATE NO ACTION ON DELETE CASCADE )")
        connection.execSQL("CREATE INDEX IF NOT EXISTS `index_detalle_tickets_numeroPedido` ON `detalle_tickets` (`numeroPedido`)")
        connection.execSQL("CREATE TABLE IF NOT EXISTS room_master_table (id INTEGER PRIMARY KEY,identity_hash TEXT)")
        connection.execSQL("INSERT OR REPLACE INTO room_master_table (id,identity_hash) VALUES(42, '8df925e4ce16b23e95dd102e9c14a806')")
      }

      public override fun dropAllTables(connection: SQLiteConnection) {
        connection.execSQL("DROP TABLE IF EXISTS `cajeros`")
        connection.execSQL("DROP TABLE IF EXISTS `platillos`")
        connection.execSQL("DROP TABLE IF EXISTS `variantes`")
        connection.execSQL("DROP TABLE IF EXISTS `menu_del_dia`")
        connection.execSQL("DROP TABLE IF EXISTS `tickets`")
        connection.execSQL("DROP TABLE IF EXISTS `detalle_tickets`")
      }

      public override fun onCreate(connection: SQLiteConnection) {
      }

      public override fun onOpen(connection: SQLiteConnection) {
        connection.execSQL("PRAGMA foreign_keys = ON")
        internalInitInvalidationTracker(connection)
      }

      public override fun onPreMigrate(connection: SQLiteConnection) {
        dropFtsSyncTriggers(connection)
      }

      public override fun onPostMigrate(connection: SQLiteConnection) {
      }

      public override fun onValidateSchema(connection: SQLiteConnection):
          RoomOpenDelegate.ValidationResult {
        val _columnsCajeros: MutableMap<String, TableInfo.Column> = mutableMapOf()
        _columnsCajeros.put("matricula", TableInfo.Column("matricula", "TEXT", true, 1, null,
            TableInfo.CREATED_FROM_ENTITY))
        _columnsCajeros.put("nombreCompleto", TableInfo.Column("nombreCompleto", "TEXT", true, 0,
            null, TableInfo.CREATED_FROM_ENTITY))
        _columnsCajeros.put("passwordHash", TableInfo.Column("passwordHash", "TEXT", true, 0, null,
            TableInfo.CREATED_FROM_ENTITY))
        val _foreignKeysCajeros: MutableSet<TableInfo.ForeignKey> = mutableSetOf()
        val _indicesCajeros: MutableSet<TableInfo.Index> = mutableSetOf()
        val _infoCajeros: TableInfo = TableInfo("cajeros", _columnsCajeros, _foreignKeysCajeros,
            _indicesCajeros)
        val _existingCajeros: TableInfo = read(connection, "cajeros")
        if (!_infoCajeros.equals(_existingCajeros)) {
          return RoomOpenDelegate.ValidationResult(false, """
              |cajeros(mx.edu.utez.cafeteria.data.local.entity.CajeroEntity).
              | Expected:
              |""".trimMargin() + _infoCajeros + """
              |
              | Found:
              |""".trimMargin() + _existingCajeros)
        }
        val _columnsPlatillos: MutableMap<String, TableInfo.Column> = mutableMapOf()
        _columnsPlatillos.put("idPlatillo", TableInfo.Column("idPlatillo", "INTEGER", true, 1, null,
            TableInfo.CREATED_FROM_ENTITY))
        _columnsPlatillos.put("nombre", TableInfo.Column("nombre", "TEXT", true, 0, null,
            TableInfo.CREATED_FROM_ENTITY))
        _columnsPlatillos.put("descripcion", TableInfo.Column("descripcion", "TEXT", true, 0, null,
            TableInfo.CREATED_FROM_ENTITY))
        _columnsPlatillos.put("categoria", TableInfo.Column("categoria", "TEXT", true, 0, null,
            TableInfo.CREATED_FROM_ENTITY))
        _columnsPlatillos.put("precioBase", TableInfo.Column("precioBase", "REAL", true, 0, null,
            TableInfo.CREATED_FROM_ENTITY))
        _columnsPlatillos.put("imagenUri", TableInfo.Column("imagenUri", "TEXT", false, 0, null,
            TableInfo.CREATED_FROM_ENTITY))
        _columnsPlatillos.put("activo", TableInfo.Column("activo", "INTEGER", true, 0, null,
            TableInfo.CREATED_FROM_ENTITY))
        val _foreignKeysPlatillos: MutableSet<TableInfo.ForeignKey> = mutableSetOf()
        val _indicesPlatillos: MutableSet<TableInfo.Index> = mutableSetOf()
        val _infoPlatillos: TableInfo = TableInfo("platillos", _columnsPlatillos,
            _foreignKeysPlatillos, _indicesPlatillos)
        val _existingPlatillos: TableInfo = read(connection, "platillos")
        if (!_infoPlatillos.equals(_existingPlatillos)) {
          return RoomOpenDelegate.ValidationResult(false, """
              |platillos(mx.edu.utez.cafeteria.data.local.entity.PlatilloEntity).
              | Expected:
              |""".trimMargin() + _infoPlatillos + """
              |
              | Found:
              |""".trimMargin() + _existingPlatillos)
        }
        val _columnsVariantes: MutableMap<String, TableInfo.Column> = mutableMapOf()
        _columnsVariantes.put("idVariante", TableInfo.Column("idVariante", "INTEGER", true, 1, null,
            TableInfo.CREATED_FROM_ENTITY))
        _columnsVariantes.put("idPlatillo", TableInfo.Column("idPlatillo", "INTEGER", true, 0, null,
            TableInfo.CREATED_FROM_ENTITY))
        _columnsVariantes.put("nombreVariante", TableInfo.Column("nombreVariante", "TEXT", true, 0,
            null, TableInfo.CREATED_FROM_ENTITY))
        _columnsVariantes.put("detalle", TableInfo.Column("detalle", "TEXT", false, 0, null,
            TableInfo.CREATED_FROM_ENTITY))
        _columnsVariantes.put("precioExtra", TableInfo.Column("precioExtra", "REAL", true, 0, null,
            TableInfo.CREATED_FROM_ENTITY))
        _columnsVariantes.put("stock", TableInfo.Column("stock", "INTEGER", true, 0, null,
            TableInfo.CREATED_FROM_ENTITY))
        _columnsVariantes.put("activo", TableInfo.Column("activo", "INTEGER", true, 0, null,
            TableInfo.CREATED_FROM_ENTITY))
        val _foreignKeysVariantes: MutableSet<TableInfo.ForeignKey> = mutableSetOf()
        _foreignKeysVariantes.add(TableInfo.ForeignKey("platillos", "CASCADE", "NO ACTION",
            listOf("idPlatillo"), listOf("idPlatillo")))
        val _indicesVariantes: MutableSet<TableInfo.Index> = mutableSetOf()
        _indicesVariantes.add(TableInfo.Index("index_variantes_idPlatillo", false,
            listOf("idPlatillo"), listOf("ASC")))
        val _infoVariantes: TableInfo = TableInfo("variantes", _columnsVariantes,
            _foreignKeysVariantes, _indicesVariantes)
        val _existingVariantes: TableInfo = read(connection, "variantes")
        if (!_infoVariantes.equals(_existingVariantes)) {
          return RoomOpenDelegate.ValidationResult(false, """
              |variantes(mx.edu.utez.cafeteria.data.local.entity.VarianteEntity).
              | Expected:
              |""".trimMargin() + _infoVariantes + """
              |
              | Found:
              |""".trimMargin() + _existingVariantes)
        }
        val _columnsMenuDelDia: MutableMap<String, TableInfo.Column> = mutableMapOf()
        _columnsMenuDelDia.put("idMenuDia", TableInfo.Column("idMenuDia", "INTEGER", true, 1, null,
            TableInfo.CREATED_FROM_ENTITY))
        _columnsMenuDelDia.put("diaSemana", TableInfo.Column("diaSemana", "TEXT", true, 0, null,
            TableInfo.CREATED_FROM_ENTITY))
        _columnsMenuDelDia.put("numeroDia", TableInfo.Column("numeroDia", "INTEGER", true, 0, null,
            TableInfo.CREATED_FROM_ENTITY))
        _columnsMenuDelDia.put("sopa", TableInfo.Column("sopa", "TEXT", true, 0, null,
            TableInfo.CREATED_FROM_ENTITY))
        _columnsMenuDelDia.put("platoFuerte", TableInfo.Column("platoFuerte", "TEXT", true, 0, null,
            TableInfo.CREATED_FROM_ENTITY))
        _columnsMenuDelDia.put("guarnicion", TableInfo.Column("guarnicion", "TEXT", true, 0, null,
            TableInfo.CREATED_FROM_ENTITY))
        _columnsMenuDelDia.put("frijolesPostre", TableInfo.Column("frijolesPostre", "TEXT", true, 0,
            null, TableInfo.CREATED_FROM_ENTITY))
        _columnsMenuDelDia.put("precio", TableInfo.Column("precio", "REAL", true, 0, null,
            TableInfo.CREATED_FROM_ENTITY))
        _columnsMenuDelDia.put("stock", TableInfo.Column("stock", "INTEGER", true, 0, null,
            TableInfo.CREATED_FROM_ENTITY))
        _columnsMenuDelDia.put("activo", TableInfo.Column("activo", "INTEGER", true, 0, null,
            TableInfo.CREATED_FROM_ENTITY))
        val _foreignKeysMenuDelDia: MutableSet<TableInfo.ForeignKey> = mutableSetOf()
        val _indicesMenuDelDia: MutableSet<TableInfo.Index> = mutableSetOf()
        val _infoMenuDelDia: TableInfo = TableInfo("menu_del_dia", _columnsMenuDelDia,
            _foreignKeysMenuDelDia, _indicesMenuDelDia)
        val _existingMenuDelDia: TableInfo = read(connection, "menu_del_dia")
        if (!_infoMenuDelDia.equals(_existingMenuDelDia)) {
          return RoomOpenDelegate.ValidationResult(false, """
              |menu_del_dia(mx.edu.utez.cafeteria.data.local.entity.MenuDelDiaEntity).
              | Expected:
              |""".trimMargin() + _infoMenuDelDia + """
              |
              | Found:
              |""".trimMargin() + _existingMenuDelDia)
        }
        val _columnsTickets: MutableMap<String, TableInfo.Column> = mutableMapOf()
        _columnsTickets.put("numeroPedido", TableInfo.Column("numeroPedido", "INTEGER", true, 1,
            null, TableInfo.CREATED_FROM_ENTITY))
        _columnsTickets.put("matriculaCajero", TableInfo.Column("matriculaCajero", "TEXT", true, 0,
            null, TableInfo.CREATED_FROM_ENTITY))
        _columnsTickets.put("nombreCajero", TableInfo.Column("nombreCajero", "TEXT", true, 0, null,
            TableInfo.CREATED_FROM_ENTITY))
        _columnsTickets.put("fechaHora", TableInfo.Column("fechaHora", "TEXT", true, 0, null,
            TableInfo.CREATED_FROM_ENTITY))
        _columnsTickets.put("metodoPago", TableInfo.Column("metodoPago", "TEXT", true, 0, null,
            TableInfo.CREATED_FROM_ENTITY))
        _columnsTickets.put("precioTotal", TableInfo.Column("precioTotal", "REAL", true, 0, null,
            TableInfo.CREATED_FROM_ENTITY))
        val _foreignKeysTickets: MutableSet<TableInfo.ForeignKey> = mutableSetOf()
        val _indicesTickets: MutableSet<TableInfo.Index> = mutableSetOf()
        val _infoTickets: TableInfo = TableInfo("tickets", _columnsTickets, _foreignKeysTickets,
            _indicesTickets)
        val _existingTickets: TableInfo = read(connection, "tickets")
        if (!_infoTickets.equals(_existingTickets)) {
          return RoomOpenDelegate.ValidationResult(false, """
              |tickets(mx.edu.utez.cafeteria.data.local.entity.TicketEntity).
              | Expected:
              |""".trimMargin() + _infoTickets + """
              |
              | Found:
              |""".trimMargin() + _existingTickets)
        }
        val _columnsDetalleTickets: MutableMap<String, TableInfo.Column> = mutableMapOf()
        _columnsDetalleTickets.put("idDetalle", TableInfo.Column("idDetalle", "INTEGER", true, 1,
            null, TableInfo.CREATED_FROM_ENTITY))
        _columnsDetalleTickets.put("numeroPedido", TableInfo.Column("numeroPedido", "INTEGER", true,
            0, null, TableInfo.CREATED_FROM_ENTITY))
        _columnsDetalleTickets.put("nombrePlatillo", TableInfo.Column("nombrePlatillo", "TEXT",
            true, 0, null, TableInfo.CREATED_FROM_ENTITY))
        _columnsDetalleTickets.put("nombreVariante", TableInfo.Column("nombreVariante", "TEXT",
            true, 0, null, TableInfo.CREATED_FROM_ENTITY))
        _columnsDetalleTickets.put("cantidad", TableInfo.Column("cantidad", "INTEGER", true, 0,
            null, TableInfo.CREATED_FROM_ENTITY))
        _columnsDetalleTickets.put("precioUnitario", TableInfo.Column("precioUnitario", "REAL",
            true, 0, null, TableInfo.CREATED_FROM_ENTITY))
        _columnsDetalleTickets.put("importe", TableInfo.Column("importe", "REAL", true, 0, null,
            TableInfo.CREATED_FROM_ENTITY))
        val _foreignKeysDetalleTickets: MutableSet<TableInfo.ForeignKey> = mutableSetOf()
        _foreignKeysDetalleTickets.add(TableInfo.ForeignKey("tickets", "CASCADE", "NO ACTION",
            listOf("numeroPedido"), listOf("numeroPedido")))
        val _indicesDetalleTickets: MutableSet<TableInfo.Index> = mutableSetOf()
        _indicesDetalleTickets.add(TableInfo.Index("index_detalle_tickets_numeroPedido", false,
            listOf("numeroPedido"), listOf("ASC")))
        val _infoDetalleTickets: TableInfo = TableInfo("detalle_tickets", _columnsDetalleTickets,
            _foreignKeysDetalleTickets, _indicesDetalleTickets)
        val _existingDetalleTickets: TableInfo = read(connection, "detalle_tickets")
        if (!_infoDetalleTickets.equals(_existingDetalleTickets)) {
          return RoomOpenDelegate.ValidationResult(false, """
              |detalle_tickets(mx.edu.utez.cafeteria.data.local.entity.DetalleTicketEntity).
              | Expected:
              |""".trimMargin() + _infoDetalleTickets + """
              |
              | Found:
              |""".trimMargin() + _existingDetalleTickets)
        }
        return RoomOpenDelegate.ValidationResult(true, null)
      }
    }
    return _openDelegate
  }

  protected override fun createInvalidationTracker(): InvalidationTracker {
    val _shadowTablesMap: MutableMap<String, String> = mutableMapOf()
    val _viewTables: MutableMap<String, Set<String>> = mutableMapOf()
    return InvalidationTracker(this, _shadowTablesMap, _viewTables, "cajeros", "platillos",
        "variantes", "menu_del_dia", "tickets", "detalle_tickets")
  }

  public override fun clearAllTables() {
    super.performClear(true, "cajeros", "platillos", "variantes", "menu_del_dia", "tickets",
        "detalle_tickets")
  }

  protected override fun getRequiredTypeConverterClasses(): Map<KClass<*>, List<KClass<*>>> {
    val _typeConvertersMap: MutableMap<KClass<*>, List<KClass<*>>> = mutableMapOf()
    _typeConvertersMap.put(CajeroDao::class, CajeroDao_Impl.getRequiredConverters())
    _typeConvertersMap.put(PlatilloDao::class, PlatilloDao_Impl.getRequiredConverters())
    _typeConvertersMap.put(VarianteDao::class, VarianteDao_Impl.getRequiredConverters())
    _typeConvertersMap.put(MenuDelDiaDao::class, MenuDelDiaDao_Impl.getRequiredConverters())
    _typeConvertersMap.put(TicketDao::class, TicketDao_Impl.getRequiredConverters())
    return _typeConvertersMap
  }

  public override fun getRequiredAutoMigrationSpecClasses(): Set<KClass<out AutoMigrationSpec>> {
    val _autoMigrationSpecsSet: MutableSet<KClass<out AutoMigrationSpec>> = mutableSetOf()
    return _autoMigrationSpecsSet
  }

  public override
      fun createAutoMigrations(autoMigrationSpecs: Map<KClass<out AutoMigrationSpec>, AutoMigrationSpec>):
      List<Migration> {
    val _autoMigrations: MutableList<Migration> = mutableListOf()
    return _autoMigrations
  }

  public override fun cajeroDao(): CajeroDao = _cajeroDao.value

  public override fun platilloDao(): PlatilloDao = _platilloDao.value

  public override fun varianteDao(): VarianteDao = _varianteDao.value

  public override fun menuDelDiaDao(): MenuDelDiaDao = _menuDelDiaDao.value

  public override fun ticketDao(): TicketDao = _ticketDao.value
}
