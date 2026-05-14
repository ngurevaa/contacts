package ru.gureva.yadro.presentation.ui.permission.contacts

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import ru.gureva.yadro.R
import ru.gureva.yadro.presentation.ui.components.CustomButton
import ru.gureva.yadro.presentation.ui.components.CustomTextButton

@Composable
fun ContactsShouldShowRationaleScreen(
    onRequestPermission: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .navigationBarsPadding()
            .padding(24.dp)
    ) {
        Spacer(modifier = Modifier.weight(1f))
        Text(
            text = stringResource(R.string.contacts_permission_rationale_title),
            style = MaterialTheme.typography.headlineMedium
        )
        Spacer(modifier = Modifier.height(24.dp))
        Text(
            text = stringResource(R.string.contacts_permission_rationale_desc),
            style = MaterialTheme.typography.titleMedium,
            color = MaterialTheme.colorScheme.secondary
        )
        Spacer(modifier = Modifier.weight(1f))
        CustomButton(
            title = stringResource(R.string.allow),
            onClick = { onRequestPermission() }
        )
        CustomTextButton(
            title = stringResource(R.string.not_now),
            onClick = {
            }
        )
    }
}
