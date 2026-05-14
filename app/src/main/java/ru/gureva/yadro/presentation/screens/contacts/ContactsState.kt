package ru.gureva.yadro.presentation.screens.contacts

import ru.gureva.yadro.domain.model.Contact

data class ContactsState(
    val contacts: Map<Char, List<Contact>> = mapOf(),
    val isLoading: Boolean = false
)
