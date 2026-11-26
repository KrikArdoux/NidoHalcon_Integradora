package mx.edu.utez.cafeteria

import android.content.Context
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import mx.edu.utez.cafeteria.data.local.AppDatabase
import mx.edu.utez.cafeteria.data.local.DataSeeder
import mx.edu.utez.cafeteria.data.local.UserPreferencesRepository
import mx.edu.utez.cafeteria.data.remote.RetrofitClient
import mx.edu.utez.cafeteria.data.repository.AuthRepository
import mx.edu.utez.cafeteria.data.repository.MenuRepository
import mx.edu.utez.cafeteria.data.repository.PedidoRepository
import mx.edu.utez.cafeteria.navigation.AppNavGraph
import mx.edu.utez.cafeteria.ui.theme.CafeteriaTheme
import mx.edu.utez.cafeteria.ui.viewmodels.HistorialViewModel
import mx.edu.utez.cafeteria.ui.viewmodels.LoginViewModel
import mx.edu.utez.cafeteria.ui.viewmodels.MenuViewModel
import mx.edu.utez.cafeteria.ui.viewmodels.PedidoViewModel

class AppContainer private constructor(context: Context) {
    val database: AppDatabase = AppDatabase.getInstance(context)
    val userPreferences = UserPreferencesRepository(context.applicationContext)
    private val api = RetrofitClient.api

    val authRepository = AuthRepository(database, api, userPreferences)
    val menuRepository = MenuRepository(database, api)
    val pedidoRepository = PedidoRepository(database, api)

    init {
        CoroutineScope(Dispatchers.IO).launch { DataSeeder.sembrarSiVacio(database) }
    }

    companion object {
        @Volatile private var instancia: AppContainer? = null

        fun obtener(context: Context): AppContainer =
            instancia ?: synchronized(this) {
                instancia ?: AppContainer(context.applicationContext).also { instancia = it }
            }
    }
}

class AppViewModelFactory(private val c: AppContainer) : ViewModelProvider.Factory {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T = when {
        modelClass.isAssignableFrom(LoginViewModel::class.java) -> LoginViewModel(c.authRepository) as T
        modelClass.isAssignableFrom(MenuViewModel::class.java) -> MenuViewModel(c.menuRepository, c.userPreferences) as T
        modelClass.isAssignableFrom(PedidoViewModel::class.java) -> PedidoViewModel(c.pedidoRepository, c.userPreferences) as T
        modelClass.isAssignableFrom(HistorialViewModel::class.java) -> HistorialViewModel(c.pedidoRepository, c.userPreferences) as T
        else -> throw IllegalArgumentException("ViewModel desconocido: ${modelClass.name}")
    }
}

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val container = AppContainer.obtener(this)
        setContent {
            CafeteriaTheme {
                AppNavGraph(container)
            }
        }
    }
}
