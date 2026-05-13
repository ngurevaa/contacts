package ru.gureva.yadro.presentation.ui.permission

import android.app.Activity
import android.content.pm.PackageManager
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.platform.LocalContext
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat

@Composable
fun rememberPermissionHandler(
    permission: String
): Pair<PermissionStatus?, () -> Unit> {
    val context = LocalContext.current
    var status by remember { mutableStateOf<PermissionStatus?>(null) }

    val launcher = rememberLauncherForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { isGranted ->
        status = when {
            isGranted -> PermissionStatus.Granted
            (context as? Activity)?.let { activity ->
                ActivityCompat.shouldShowRequestPermissionRationale(activity, permission)
            } == true -> PermissionStatus.ShouldShowRationale
            else -> PermissionStatus.DeniedPermanently
        }
    }

    LaunchedEffect(Unit) {
        status = when {
            ContextCompat.checkSelfPermission(context, permission) == PackageManager.PERMISSION_GRANTED ->
                PermissionStatus.Granted
            (context as? Activity)?.shouldShowRequestPermissionRationale(permission) == true ->
                PermissionStatus.ShouldShowRationale
            else -> PermissionStatus.RequestRequired
        }
    }

    return status to { launcher.launch(permission) }
}
