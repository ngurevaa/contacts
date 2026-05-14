package ru.gureva.yadro.utils

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.provider.Settings
import androidx.core.net.toUri

fun Context.openAppSettings() {
    val intent = Intent(
        Settings.ACTION_APPLICATION_DETAILS_SETTINGS,
        Uri.fromParts("package", packageName, null)
    )
    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
    startActivity(intent)
}

fun Context.callContact(phoneNumber: String) {
    val intent = Intent(
        Intent.ACTION_CALL,
        "tel:$phoneNumber".toUri()
    )
    startActivity(intent)
}
