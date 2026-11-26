package mx.edu.utez.cafeteria.ui.theme

import android.app.Activity
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalView

private val EsquemaClaro = lightColorScheme(
    primary = NavyPrimary,
    onPrimary = Color.White,
    secondary = EmeraldAccent,
    onSecondary = Color.White,
    tertiary = NavySurfaceVariant,
    background = BackgroundLight,
    onBackground = NavyPrimary,
    surface = CardSurface,
    onSurface = NavyPrimary,
    surfaceVariant = BackgroundLight,
    onSurfaceVariant = TextMuted,
    outline = BorderLight,
    error = AgotadoText,
    errorContainer = AgotadoBg,
    onErrorContainer = AgotadoText
)

@Composable
fun CafeteriaTheme(content: @Composable () -> Unit) {
    val view = LocalView.current
    SideEffect {
        (view.context as? Activity)?.window?.statusBarColor = NavyPrimary.toArgb()
    }
    MaterialTheme(colorScheme = EsquemaClaro, typography = AppTypography, content = content)
}
