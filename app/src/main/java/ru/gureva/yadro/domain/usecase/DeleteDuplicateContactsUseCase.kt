package ru.gureva.yadro.domain.usecase

interface DeleteDuplicateContactsUseCase {
    suspend operator fun invoke(): Int
}
