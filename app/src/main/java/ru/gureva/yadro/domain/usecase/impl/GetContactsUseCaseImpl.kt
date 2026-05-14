package ru.gureva.yadro.domain.usecase.impl

import ru.gureva.yadro.domain.model.Contact
import ru.gureva.yadro.domain.repository.ContactRepository
import ru.gureva.yadro.domain.usecase.GetContactsUseCase
import javax.inject.Inject

class GetContactsUseCaseImpl @Inject constructor(
    private val contactRepository: ContactRepository
) : GetContactsUseCase {
    override suspend fun invoke(): List<Contact> {
        return contactRepository.getAllContacts()
    }
}
