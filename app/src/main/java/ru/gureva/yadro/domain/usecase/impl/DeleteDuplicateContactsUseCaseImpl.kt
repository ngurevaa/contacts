package ru.gureva.yadro.domain.usecase.impl

import ru.gureva.yadro.domain.repository.ContactRepository
import ru.gureva.yadro.domain.usecase.DeleteDuplicateContactsUseCase
import javax.inject.Inject

class DeleteDuplicateContactsUseCaseImpl @Inject constructor(
    private val contactRepository: ContactRepository
) : DeleteDuplicateContactsUseCase {
    override suspend operator fun invoke(): Int {
        return contactRepository.deleteDuplicates()
    }
}
