package dev.soncresityindustries.schat.manager.ui.components.settings

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun SettingsTextField(
    label: String,
    disabled: Boolean = false,
    pref: String,
    error: Boolean = false,
    supportingText: String? = null,
    onPrefChange: (String) -> Unit,
) {
    Box(modifier = Modifier.padding(horizontal = 18.dp, vertical = 10.dp)) {
        OutlinedTextField(
            modifier = Modifier.fillMaxWidth(),
            value = pref,
            onValueChange = onPrefChange,
            enabled = !disabled,
            label = { Text(label) },
            isError = error,
            singleLine = true,
            supportingText = if (supportingText != null) { -> Text(supportingText) } else null
        )
    }
}