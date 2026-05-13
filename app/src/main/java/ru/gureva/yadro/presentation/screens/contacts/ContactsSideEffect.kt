package ru.gureva.yadro.presentation.screens.contacts

sealed interface ContactsSideEffect {
    data class ShowSnackbar(
        val message: String,
        val action: String? = null
    ) : ContactsSideEffect
}
