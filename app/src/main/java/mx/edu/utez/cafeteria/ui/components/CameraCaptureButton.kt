package mx.edu.utez.cafeteria.ui.components

import android.Manifest
import android.content.pm.PackageManager
import android.net.Uri
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.PhotoCamera
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import java.io.File
import mx.edu.utez.cafeteria.ui.theme.NavyPrimary

@Composable
fun CameraCaptureButton(
    onFotoCapturada: (String) -> Unit,
    modifier: Modifier = Modifier,
    etiqueta: String = "Tomar foto"
) {
    val context = LocalContext.current
    var uriPendiente by rememberSaveable { mutableStateOf<Uri?>(null) }

    val tomarFoto = rememberLauncherForActivityResult(ActivityResultContracts.TakePicture()) { ok ->
        val uri = uriPendiente
        if (ok && uri != null) onFotoCapturada(uri.toString())
    }

    fun lanzarCamara() {
        val dir = File(context.filesDir, "fotos").apply { mkdirs() }
        val archivo = File(dir, "platillo_${System.currentTimeMillis()}.jpg")
        val uri = FileProvider.getUriForFile(context, "${context.packageName}.fileprovider", archivo)
        uriPendiente = uri
        tomarFoto.launch(uri)
    }

    val permiso = rememberLauncherForActivityResult(ActivityResultContracts.RequestPermission()) { concedido ->
        if (concedido) lanzarCamara()
        else Toast.makeText(context, "Permiso de cámara denegado", Toast.LENGTH_SHORT).show()
    }

    OutlinedButton(
        modifier = modifier,
        onClick = {
            if (ContextCompat.checkSelfPermission(context, Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED) {
                lanzarCamara()
            } else {
                permiso.launch(Manifest.permission.CAMERA)
            }
        }
    ) {
        Icon(Icons.Default.PhotoCamera, contentDescription = null, tint = NavyPrimary)
        Text("  $etiqueta", color = NavyPrimary)
    }
}
