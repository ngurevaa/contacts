package ru.gureva.yadro.domain.repository

import ru.gureva.yadro.domain.model.Contact

interface ContactRepository {
    suspend fun getAllContacts(): List<Contact>
    suspend fun deleteDuplicates(): Int
}
