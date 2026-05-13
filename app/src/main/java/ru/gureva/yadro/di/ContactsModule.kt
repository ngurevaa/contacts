package ru.gureva.yadro.di

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import ru.gureva.yadro.data.repository.ContactRepositoryImpl
import ru.gureva.yadro.domain.repository.ContactRepository
import ru.gureva.yadro.domain.usecase.GetContactsUseCase
import ru.gureva.yadro.domain.usecase.impl.GetContactsUseCaseImpl

@Module
@InstallIn(SingletonComponent::class)
abstract class ContactsModule {
    @Binds
    abstract fun bindContactRepository(
        contactRepositoryImpl: ContactRepositoryImpl
    ): ContactRepository

    @Binds
    abstract fun bindGetContactsUseCase(
        getContactsUseCaseImpl: GetContactsUseCaseImpl
    ): GetContactsUseCase
}
