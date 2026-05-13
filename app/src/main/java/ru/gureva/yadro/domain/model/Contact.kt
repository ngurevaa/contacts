package ru.gureva.yadro.domain.model

data class Contact(
    val id: Long,
    val name: String,
    val phone: String,
    val image: String?
)
