package org.mefetran.munchkinmaster.presentation.ui.uikit.dropdown

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuAnchorType
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import munchkinmaster.composeapp.generated.resources.Res
import munchkinmaster.composeapp.generated.resources.dark
import munchkinmaster.composeapp.generated.resources.light
import munchkinmaster.composeapp.generated.resources.system_theme
import munchkinmaster.composeapp.generated.resources.theme
import org.jetbrains.compose.resources.stringResource
import org.jetbrains.compose.ui.tooling.preview.Preview

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun <T : Any> CustomExposedDropdownMenu(
    options: List<T>,
    label: String,
    initialValue: String,
    modifier: Modifier = Modifier,
    dropdownText: @Composable (T) -> Unit,
    onOptionClicked: (T) -> Unit,
) {
    var expanded by remember { mutableStateOf(false) }

    ExposedDropdownMenuBox(
        expanded = expanded,
        onExpandedChange = { expanded = it },
    ) {
        OutlinedTextField(
            value = initialValue,
            onValueChange = {},
            modifier = modifier
                .fillMaxWidth()
                .menuAnchor(ExposedDropdownMenuAnchorType.PrimaryNotEditable),
            readOnly = true,
            singleLine = true,
            label = { Text(text = label) },
            trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded) },
            colors = ExposedDropdownMenuDefaults.textFieldColors()
        )
        ExposedDropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false }
        ) {
            options.forEach { option ->
                DropdownMenuItem(
                    text = { dropdownText(option) },
                    onClick = {
                        onOptionClicked(option)
                        expanded = false
                    },
                    contentPadding = ExposedDropdownMenuDefaults.ItemContentPadding
                )
            }
        }
    }
}

@Preview(showBackground = true, backgroundColor = 0xFFFFFFFF, locale = "ru")
@Composable
fun CustomExposedDropdownMenuPreview(modifier: Modifier = Modifier) {
    val options = listOf(
        stringResource(Res.string.system_theme),
        stringResource(Res.string.light),
        stringResource(Res.string.dark)
    )

    Surface(
        color = Color.White,
        modifier = modifier.fillMaxSize()
    ) {
        Column(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            if (options.isNotEmpty()) {
                CustomExposedDropdownMenu(
                    options = options,
                    label = stringResource(Res.string.theme),
                    dropdownText = { Text(text = it, style = MaterialTheme.typography.bodyLarge) },
                    initialValue = options.first(),
                    onOptionClicked = {},
                )
            }
        }
    }
}
