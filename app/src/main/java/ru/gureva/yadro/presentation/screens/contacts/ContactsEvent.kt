package ru.gureva.yadro.presentation.screens.contacts

sealed interface ContactsEvent {
    data object LoadContacts : ContactsEvent
    data object ShowCallPermissionRationale : ContactsEvent
    data object ShowCallPermissionDenied : ContactsEvent
}
