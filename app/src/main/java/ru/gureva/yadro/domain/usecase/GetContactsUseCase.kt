package ru.gureva.yadro.domain.usecase

import ru.gureva.yadro.domain.model.Contact

interface GetContactsUseCase {
    suspend operator fun invoke(): List<Contact>
}
