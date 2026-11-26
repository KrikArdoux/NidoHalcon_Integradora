package mx.edu.utez.cafeteria.data.local

import android.content.Context
import androidx.room.Database
import androidx.room.Embedded
import androidx.room.Relation
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.withTransaction
import androidx.sqlite.db.SupportSQLiteDatabase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import mx.edu.utez.cafeteria.data.local.dao.CajeroDao
import mx.edu.utez.cafeteria.data.local.dao.MenuDelDiaDao
import mx.edu.utez.cafeteria.data.local.dao.PlatilloDao
import mx.edu.utez.cafeteria.data.local.dao.TicketDao
import mx.edu.utez.cafeteria.data.local.dao.VarianteDao
import mx.edu.utez.cafeteria.data.local.entity.CajeroEntity
import mx.edu.utez.cafeteria.data.local.entity.DetalleTicketEntity
import mx.edu.utez.cafeteria.data.local.entity.MenuDelDiaEntity
import mx.edu.utez.cafeteria.data.local.entity.PlatilloEntity
import mx.edu.utez.cafeteria.data.local.entity.TicketEntity
import mx.edu.utez.cafeteria.data.local.entity.VarianteEntity

data class PlatilloConVariantes(
    @Embedded val platillo: PlatilloEntity,
    @Relation(parentColumn = "idPlatillo", entityColumn = "idPlatillo")
    val variantes: List<VarianteEntity>
)

data class TicketConDetalles(
    @Embedded val ticket: TicketEntity,
    @Relation(parentColumn = "numeroPedido", entityColumn = "numeroPedido")
    val detalles: List<DetalleTicketEntity>
)

@Database(
    entities = [
        CajeroEntity::class, PlatilloEntity::class, VarianteEntity::class,
        MenuDelDiaEntity::class, TicketEntity::class, DetalleTicketEntity::class
    ],
    version = 1,
    exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun cajeroDao(): CajeroDao
    abstract fun platilloDao(): PlatilloDao
    abstract fun varianteDao(): VarianteDao
    abstract fun menuDelDiaDao(): MenuDelDiaDao
    abstract fun ticketDao(): TicketDao

    companion object {
        @Volatile private var INSTANCE: AppDatabase? = null

        fun getInstance(context: Context): AppDatabase =
            INSTANCE ?: synchronized(this) {
                INSTANCE ?: Room.databaseBuilder(
                    context.applicationContext, AppDatabase::class.java, "nido_halcon.db"
                ).addCallback(SeedCallback).build().also { INSTANCE = it }
            }

        private val SeedCallback = object : Callback() {
            override fun onCreate(db: SupportSQLiteDatabase) {
                super.onCreate(db)
                CoroutineScope(Dispatchers.IO).launch {
                    INSTANCE?.let { DataSeeder.sembrarSiVacio(it) }
                }
            }
        }
    }
}

object DataSeeder {
    private val mutex = Mutex()

    private class Cat(val nombre: String, val base: Double, val desc: String, val variantes: String)

    private val catalogo = listOf(
        Cat("Molletes", 35.0, "Sencillos", "Sencillos;0|Huevo revuelto;20|Cochinita;20|Longaniza de pollo;20|!Pastor;20"),
        Cat("Chilaquiles", 35.0, "Salsa verde o roja", "Sencillos;0|Pechuga asada;20|Huevo revuelto;20|Longaniza;20|!Pastor;20"),
        Cat("Huevos", 50.0, "", "A la mexicana;0|Tirados;0|Mix de pimientos;0|Longaniza de pollo;0"),
        Cat("Tortas", 55.0, "", "Alambre;0|Mole con pollo;0|Pechuga asada;0|Cochinita;0|Tinga de pollo;0|Bistec;0|!Pastor;0|Chilatorta;0"),
        Cat("Chapatas", 70.0, "", "Pollo a la plancha;0|Vegetariana;0|Bistec a la plancha;0"),
        Cat("Sandwich", 65.0, "Pan integral, 3 rebanadas", "Pollo asado;0|Huevo con frijoles;0|Huevo con longaniza;0"),
        Cat("Algo mas", 0.0, "", "Orden de tortillas;5|Orden de arroz;20|Dumplings pollo o verduras;60|Sushi surimi con ensalada;75|Nachos con carne y queso;75|Banderilla;18|Vaso con hielo;10"),
        Cat("Acorazados", 45.0, "", "Pollo a la plancha;0|Alambre;0|Mole con pollo;0|Tinga de pollo;0|Huevo en chile;0"),
        Cat("Enchiladas", 60.0, "Orden de 4 piezas", "Queso;0|Longaniza;0|Pollo;0"),
        Cat("Doraditas", 60.0, "Orden de 4 piezas", "Papa con queso;0|Longaniza;0|Pollo;0"),
        Cat(
            "Ensaladas", 55.0, "",
            "De la casa;0;Lechugas con panela, jitomate, pepino, jícama y betabel|" +
                "César;0;Lechugas con crutones de ajo y queso parmesano|" +
                "Mixta;0;Lechuga, germinados, panela, jitomate, pepino, jícama, zanahoria, betabel, manzana y pasta|" +
                "De la casa + Pollo asado;20|César + Pollo asado;20|Mixta + Pollo asado;20"
        ),
        Cat("Verdura", 0.0, "", "Bastones de verdura;25|Coctel de verduras;40"),
        Cat("Cafe y mas", 0.0, "", "Americano;30|Capuchino;40|Café o chocolate con leche;40|Capuchino con hielo;50|Café frapé;55|Té de sabores;30"),
        Cat(
            "Bebidas naturales", 0.0, "",
            "Batido mixto;55;Nuevo - Elige 4: fruta, verduras, avena, chía, granola, miel o yogurt|" +
                "Licuado de fresa;55|Licuado mixto 2 frutas;55|Licuado de manzana;55|Licuado de plátano;50|" +
                "Esquimo de fresa;50|Esquimo de vainilla;50|Agua de fruta 1 refil;20"
        ),
        Cat(
            "Bebidas embotelladas", 0.0, "",
            "Agua Skarch 1.5 L;20|Agua Ciel 1.2 L;20|Agua Skarch 1 L;15|Skarch Frutale 1 L;18|Levite 1 L;25|" +
                "Agua Ciel 600 ml;14|Agua Skarch 600 ml;12|Agua mineral 600 ml;25|Topo Chico 600 ml;25|" +
                "Vida Mineral 575 ml;18|Electrolit 500 ml;30|Jumex Único Fresco;32|Jumex Cero;30|" +
                "Jumexito 200 ml;12|Gin-Lu 1 L;32|Gin-Lu 500 ml;19"
        ),
        Cat(
            "Fruta +", 0.0, "",
            "Coctel de frutas;45|Snack de fruta;35|Snack de fruta con yogurt;40|Yogurt artesanal;20|Manzana;22|" +
                "Gelatina mosaico;20|Arroz con leche o tapioca;20|Paleta de fruta natural;20|" +
                "Paleta de fruta con yogurt;25|Troles de fruta natural;20|Danonino Bluey;15|Danonino Maxi;20"
        )
    )

    private val dias = listOf("Lun", "Mar", "Mie", "Jue", "Vie")

    suspend fun sembrarSiVacio(db: AppDatabase) = mutex.withLock {
        db.withTransaction {
            if (db.cajeroDao().contar() == 0) {
                db.cajeroDao().upsert(CajeroEntity("2023001", "Cajero Halcón", "1234"))
            }
            if (db.menuDelDiaDao().contar() == 0) {
                db.menuDelDiaDao().insertarTodos(
                    dias.mapIndexed { i, d ->
                        MenuDelDiaEntity(
                            diaSemana = d, numeroDia = i + 1,
                            sopa = "Consomé de res",
                            platoFuerte = "Tostadas de tinga de pollo (3) con crema y queso",
                            guarnicion = "Ensalada de jícama, zanahoria y betabel",
                            frijolesPostre = "Frijoles negros",
                            precio = 70.0, stock = 15, activo = true
                        )
                    }
                )
            }
            if (db.platilloDao().contar() == 0) {
                catalogo.forEach { cat ->
                    val id = db.platilloDao().insertar(
                        PlatilloEntity(nombre = cat.nombre, descripcion = cat.desc, categoria = cat.nombre, precioBase = cat.base)
                    ).toInt()
                    db.varianteDao().insertarTodas(
                        cat.variantes.split("|").map { token ->
                            val p = token.split(";")
                            VarianteEntity(
                                idPlatillo = id,
                                nombreVariante = p[0].removePrefix("!"),
                                detalle = p.getOrNull(2),
                                precioExtra = p[1].toDouble(),
                                stock = if (p[0].startsWith("!")) 0 else 10,
                                activo = true
                            )
                        }
                    )
                }
            }
        }
    }
}
