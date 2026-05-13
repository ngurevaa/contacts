package ru.gureva.yadro.presentation.ui.permission

sealed interface PermissionStatus {
    data object Granted : PermissionStatus
    data object ShouldShowRationale : PermissionStatus
    data object DeniedPermanently : PermissionStatus
    data object RequestRequired : PermissionStatus
}
