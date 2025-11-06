package org.mefetran.munchkinmaster.presentation.ui.screen.settings

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import munchkinmaster.composeapp.generated.resources.Res
import munchkinmaster.composeapp.generated.resources.settings_title
import munchkinmaster.composeapp.generated.resources.theme
import org.jetbrains.compose.resources.stringResource
import org.jetbrains.compose.ui.tooling.preview.Preview
import org.mefetran.munchkinmaster.presentation.ui.theme.ThemeManager
import org.mefetran.munchkinmaster.presentation.ui.uikit.dropdown.CustomExposedDropdownMenu
import org.mefetran.munchkinmaster.presentation.ui.uikit.model.ThemeOption

@Composable
fun SettingsScreen(
    component: SettingsComponent,
    modifier: Modifier = Modifier,
) {
    val themeData by ThemeManager.themeDataState.collectAsStateWithLifecycle()
    val themesList = remember {
        listOf(
            ThemeOption.SystemTheme,
            ThemeOption.DarkTheme,
            ThemeOption.LightTheme,
        )
    }

    Surface(
        color = MaterialTheme.colorScheme.surface,
        modifier = modifier.fillMaxSize(),
    ) {
        Column(
            modifier = Modifier.fillMaxSize().verticalScroll(rememberScrollState())
        ) {
            Box(
                modifier = Modifier.fillMaxWidth().statusBarsPadding()
            ) {
                Text(
                    text = stringResource(Res.string.settings_title),
                    style = MaterialTheme.typography.headlineMedium.copy(color = MaterialTheme.colorScheme.primary),
                    modifier = Modifier.align(Alignment.Center)
                )
                IconButton(
                    onClick = component::onBackClick,
                    modifier = Modifier.align(Alignment.CenterStart)
                ) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.primary,
                    )
                }
            }
            CustomExposedDropdownMenu(
                options = themesList,
                label = stringResource(Res.string.theme),
                initialValue = when (themeData.isSystemTheme) {
                    true -> stringResource(themesList.first().localizedName)
                    false -> {
                        if (themeData.isDarkTheme) {
                            stringResource(
                                themesList.find { it == ThemeOption.DarkTheme }?.localizedName
                                    ?: themesList.first().localizedName
                            )
                        } else {
                            stringResource(
                                themesList.find { it == ThemeOption.LightTheme }?.localizedName
                                    ?: themesList.first().localizedName
                            )
                        }
                    }
                },
                dropdownText = { themeOption ->
                    Text(
                        text = stringResource(themeOption.localizedName),
                        style = MaterialTheme.typography.bodyLarge,
                    )
                },
                onOptionClicked = { themeOption -> 
                    component.onThemeOptionClick(themeOption)
                },
                modifier = Modifier.padding(all = 16.dp)
            )
        }
    }
}

@Preview(showBackground = true, backgroundColor = 0xFFFFFFFF, locale = "en")
@Composable
fun SettingsScreenPreview() {
    SettingsScreen(
        component = FakeSettingsComponent()
    )
}