package ru.gureva.yadro.presentation.screens.contacts

sealed interface ContactsEvent {
    data object LoadContacts : ContactsEvent
}
