package ru.gureva.yadro.presentation.screens.contacts

sealed interface ContactsEvent {
    data object LoadContacts : ContactsEvent
    data object DeleteDuplicateContacts : ContactsEvent
    data object ShowWriteContactsPermissionRationale : ContactsEvent
    data object ShowWriteContactsPermissionDenied : ContactsEvent
}
