package ru.gureva.yadro.presentation.screens.contacts

sealed interface ContactsSideEffect {
    data class ShowSnackbar(val message: String) : ContactsSideEffect
}
