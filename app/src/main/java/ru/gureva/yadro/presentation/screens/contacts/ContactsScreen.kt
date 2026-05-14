package ru.gureva.yadro.presentation.screens.contacts

import android.Manifest
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.SnackbarResult
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil3.compose.AsyncImage
import coil3.toUri
import ru.gureva.yadro.R
import ru.gureva.yadro.domain.model.Contact
import ru.gureva.yadro.presentation.ui.components.CustomButton
import ru.gureva.yadro.presentation.ui.permission.PermissionStatus
import ru.gureva.yadro.presentation.ui.permission.contacts.ContactsDeniedPermanentlyScreen
import ru.gureva.yadro.presentation.ui.permission.contacts.ContactsShouldShowRationaleScreen
import ru.gureva.yadro.presentation.ui.permission.rememberPermissionHandler
import ru.gureva.yadro.utils.openAppSettings

@Composable
fun ContactsScreen(viewModel: ContactsViewModel = hiltViewModel()) {
    val state by viewModel.state.collectAsStateWithLifecycle()
    val dispatch = viewModel::dispatch

    val context = LocalContext.current
    val (readContactsPermissionStatus, requestReadContactsPermission) =
        rememberPermissionHandler(Manifest.permission.READ_CONTACTS)
    val (writeContactsPermissionStatus, requestWriteContactsPermission) =
        rememberPermissionHandler(Manifest.permission.WRITE_CONTACTS)

    val snackbarHostState = remember { SnackbarHostState() }

    Scaffold(
        snackbarHost = { SnackbarHost(snackbarHostState) }
    ) { padding ->
        when (readContactsPermissionStatus) {
            PermissionStatus.Granted -> ContactsScreenContent(state, dispatch,
                writeContactsPermissionStatus, requestWriteContactsPermission)
            PermissionStatus.ShouldShowRationale -> ContactsShouldShowRationaleScreen(
                onRequestPermission = { requestReadContactsPermission() }
            )
            PermissionStatus.DeniedPermanently -> ContactsDeniedPermanentlyScreen(
                onOpenSettings = { context.openAppSettings() },
                onContinue = { requestReadContactsPermission() }
            )
            PermissionStatus.RequestRequired -> {
                LaunchedEffect(Unit) { requestReadContactsPermission() }
            }
            null -> {}
        }
    }

    LaunchedEffect(Unit) {
        viewModel.sideEffect.collect {
            when (it) {
                is ContactsSideEffect.ShowSnackbar -> {
                    val snackbar = snackbarHostState.showSnackbar(
                        message = it.message,
                        actionLabel = it.action,
                        duration = SnackbarDuration.Short
                    )

                    if (snackbar == SnackbarResult.ActionPerformed) {
                        when (it.action) {
                            context.getString(R.string.open_settings) -> {
                                context.openAppSettings()
                            }
                            context.getString(R.string.allow) -> {
                                requestWriteContactsPermission()
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
internal fun ContactsScreenContent(
    state: ContactsState,
    dispatch: (ContactsEvent) -> Unit,
    writeContactsPermissionStatus: PermissionStatus?,
    requestWriteContactsPermission: () -> Unit
) {
    LaunchedEffect(Unit) {
        dispatch(ContactsEvent.LoadContacts)
    }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .systemBarsPadding(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Header()
        if (state.isLoading) {
            CircularProgressIndicator()
        }
        else {
            ContactsList(state.contacts, modifier = Modifier.weight(1f))
            Spacer(modifier = Modifier.height(8.dp))
            CustomButton(
                title = stringResource(R.string.delete_duplicates),
                modifier = Modifier.padding(8.dp),
                onClick = {
                    when (writeContactsPermissionStatus) {
                        PermissionStatus.DeniedPermanently -> { dispatch(ContactsEvent.ShowWriteContactsPermissionDenied) }
                        PermissionStatus.Granted -> { dispatch(ContactsEvent.DeleteDuplicateContacts) }
                        PermissionStatus.RequestRequired -> { requestWriteContactsPermission() }
                        PermissionStatus.ShouldShowRationale -> { dispatch(ContactsEvent.ShowWriteContactsPermissionRationale) }
                        null -> {}
                    }
                },
            )
        }
    }
}

@Composable
internal fun Header() {
    Box(
        contentAlignment = Alignment.Center,
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 20.dp)
    ) {
        Text(
            text = stringResource(R.string.contacts),
            style = MaterialTheme.typography.titleMedium
        )
    }
}

@Composable
internal fun ContactsList(
    contacts: Map<Char, List<Contact>>,
    modifier: Modifier = Modifier
) {
    LazyColumn(
        modifier = modifier.padding(start = 8.dp)
    ) {
        contacts.forEach { (key, contactsList) ->
            item {
                Text(
                    text = "$key",
                    style = MaterialTheme.typography.titleSmall,
                    color = MaterialTheme.colorScheme.secondary,
                    modifier = Modifier.padding(vertical = 2.dp)
                )
            }
            itemsIndexed(
                items = contactsList,
                key = { index, item -> item.id }
            ) { index, item ->
                ContactItem(item)
                if (index < contactsList.size - 1) {
                    HorizontalDivider(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(start = 56.dp),
                        color = MaterialTheme.colorScheme.surfaceContainerHighest
                    )
                }
            }
        }
    }
}

@Composable
internal fun ContactItem(
    contact: Contact
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp)
    ) {
        AsyncImage(
            model = contact.image?.toUri() ?: R.drawable.avatar,
            contentDescription = contact.name,
            contentScale = ContentScale.Crop,
            modifier = Modifier
                .size(44.dp)
                .clip(CircleShape)
        )
        Spacer(modifier = Modifier.width(12.dp))
        Column(
            modifier = Modifier.height(44.dp),
            verticalArrangement = Arrangement.Center
        ) {
            Text(
                text = contact.name,
                style = MaterialTheme.typography.titleMedium
            )
            Text(
                text = contact.phone,
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.secondary
            )
        }
    }
}
