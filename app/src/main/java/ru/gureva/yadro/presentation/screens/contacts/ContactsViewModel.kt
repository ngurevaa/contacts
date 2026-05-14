package ru.gureva.yadro.presentation.screens.contacts

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import ru.gureva.yadro.R
import ru.gureva.yadro.data.service.ContactService
import ru.gureva.yadro.domain.usecase.DeleteDuplicateContactsUseCase
import ru.gureva.yadro.domain.usecase.GetContactsUseCase
import ru.gureva.yadro.utils.ResourceManager
import javax.inject.Inject

@HiltViewModel
class ContactsViewModel @Inject constructor(
    private val getContactsUseCase: GetContactsUseCase,
    private val deleteDuplicateContactsUseCase: DeleteDuplicateContactsUseCase,
    private val resourceManager: ResourceManager
) : ViewModel() {
    private val _state = MutableStateFlow(ContactsState())
    val state = _state.asStateFlow()

    private val _sideEffect = MutableSharedFlow<ContactsSideEffect>()
    val sideEffect: SharedFlow<ContactsSideEffect> = _sideEffect

    fun dispatch(event: ContactsEvent) {
        when (event) {
            ContactsEvent.LoadContacts -> loadContacts()
            ContactsEvent.DeleteDuplicateContacts -> deleteDuplicateContacts()
            ContactsEvent.ShowWriteContactsPermissionDenied -> showWriteContactsPermissionDenied()
            ContactsEvent.ShowWriteContactsPermissionRationale -> showWriteContactsPermissionRationale()
        }
    }

    private fun loadContacts() {
        viewModelScope.launch {
            runCatching {
                _state.update { it.copy(isLoading = true) }
                getContactsUseCase()
            }
                .onSuccess { contacts ->
                    _state.update { it.copy(isLoading = false) }
                    val map = contacts.groupBy { it.name[0] }
                    _state.update { it.copy(contacts = map) }
                }
                .onFailure {
                    _state.update { it.copy(isLoading = false) }
                    _sideEffect.emit(
                        ContactsSideEffect.ShowSnackbar(
                            message = resourceManager.getString(R.string.contacts_loading_error)
                        )
                    )
                }
        }
    }

    private fun updateContacts() {
        viewModelScope.launch {
            runCatching {
                getContactsUseCase()
            }
                .onSuccess { contacts ->
                    val map = contacts.groupBy { it.name[0] }
                    _state.update { it.copy(contacts = map) }
                }
                .onFailure {
                    _sideEffect.emit(
                        ContactsSideEffect.ShowSnackbar(
                            message = resourceManager.getString(R.string.contacts_loading_error)
                        )
                    )
                }
        }
    }

    private fun deleteDuplicateContacts() {
        viewModelScope.launch {
            runCatching { deleteDuplicateContactsUseCase() }
                .onSuccess { status ->
                    when (status) {
                        ContactService.SUCCESS -> {
                            updateContacts()
                            _sideEffect.emit(
                                ContactsSideEffect.ShowSnackbar(
                                    message = resourceManager.getString(R.string.duplicates_successfully_deleted)
                                )
                            )
                        }
                        ContactService.ERROR -> {
                            _sideEffect.emit(
                                ContactsSideEffect.ShowSnackbar(
                                    message = resourceManager.getString(R.string.duplicates_deleting_error)
                                )
                            )
                        }
                        ContactService.NOT_FOUND -> {
                            _sideEffect.emit(
                                ContactsSideEffect.ShowSnackbar(
                                    message = resourceManager.getString(R.string.duplicates_not_found)
                                )
                            )
                        }
                    }
                }
                .onFailure {
                    ContactsSideEffect.ShowSnackbar(
                        message = resourceManager.getString(R.string.duplicates_deleting_error)
                    )
                }
        }
    }

    private fun showWriteContactsPermissionRationale() {
        viewModelScope.launch {
            _sideEffect.emit(
                ContactsSideEffect.ShowSnackbar(
                    message = resourceManager.getString(R.string.write_contacts_permission_rationale),
                    action = resourceManager.getString(R.string.allow)
                )
            )
        }
    }

    private fun showWriteContactsPermissionDenied() {
        viewModelScope.launch {
            _sideEffect.emit(
                ContactsSideEffect.ShowSnackbar(
                    message = resourceManager.getString(R.string.write_contacts_permission_denied),
                    action = resourceManager.getString(R.string.open_settings)
                )
            )
        }
    }
}
