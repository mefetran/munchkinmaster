package org.mefetran.munchkinmaster.presentation.ui.screen.settings

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawing
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
import munchkinmaster.composeapp.generated.resources.english
import munchkinmaster.composeapp.generated.resources.epic_munchkin
import munchkinmaster.composeapp.generated.resources.keep_screen_on
import munchkinmaster.composeapp.generated.resources.language
import munchkinmaster.composeapp.generated.resources.level_limit
import munchkinmaster.composeapp.generated.resources.russian
import munchkinmaster.composeapp.generated.resources.settings_title
import munchkinmaster.composeapp.generated.resources.show_leaders
import munchkinmaster.composeapp.generated.resources.standard
import munchkinmaster.composeapp.generated.resources.theme
import munchkinmaster.composeapp.generated.resources.unlimited
import org.jetbrains.compose.resources.stringResource
import org.jetbrains.compose.ui.tooling.preview.Preview
import org.mefetran.munchkinmaster.domain.model.MaxLevel
import org.mefetran.munchkinmaster.presentation.ui.theme.ThemeManager
import org.mefetran.munchkinmaster.presentation.ui.uikit.dropdown.CustomExposedDropdownMenu
import org.mefetran.munchkinmaster.presentation.ui.uikit.model.LocaleOption
import org.mefetran.munchkinmaster.presentation.ui.uikit.model.MaxLevelOption
import org.mefetran.munchkinmaster.presentation.ui.uikit.model.ThemeOption
import org.mefetran.munchkinmaster.presentation.ui.uikit.switch.SwitchButton
import org.mefetran.munchkinmaster.presentation.util.PlatformType
import org.mefetran.munchkinmaster.presentation.util.platformType
import java.util.Locale

@Composable
fun SettingsScreen(
    component: SettingsComponent,
    modifier: Modifier = Modifier,
) {
    val themeData by ThemeManager.themeDataState.collectAsStateWithLifecycle()
    val settingsState by SettingsManager.state.collectAsStateWithLifecycle()
    val themesList = remember {
        listOf(
            ThemeOption.SystemTheme,
            ThemeOption.DarkTheme,
            ThemeOption.LightTheme,
        )
    }
    val localesList = remember {
        listOf(
            LocaleOption.EnglishLocale,
            LocaleOption.RussianLocale,
        )
    }
    val levelLimiters = remember {
        listOf(
            MaxLevelOption.Standard,
            MaxLevelOption.EpicMunchkin,
            MaxLevelOption.Unlimited,
        )
    }

    Surface(
        color = MaterialTheme.colorScheme.surface,
        modifier = modifier.fillMaxSize(),
    ) {
        Column(
            modifier = Modifier
                .padding(WindowInsets.safeDrawing.asPaddingValues())
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .statusBarsPadding()
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
                modifier = Modifier.padding(horizontal = 16.dp).padding(top = 16.dp, bottom = 8.dp)
            )
            CustomExposedDropdownMenu(
                options = localesList,
                label = stringResource(Res.string.language),
                initialValue = when (settingsState.locale) {
                    "en" -> stringResource(Res.string.english)
                    "ru" -> stringResource(Res.string.russian)
                    null -> stringResource(
                        when (Locale.getDefault().toString().substring(0, 2)) {
                            "en" -> Res.string.english
                            "ru" -> Res.string.russian
                            else -> Res.string.english
                        }
                    )

                    else -> stringResource(Res.string.english)
                },
                dropdownText = { localeOption ->
                    Text(
                        text = stringResource(localeOption.localizedName),
                        style = MaterialTheme.typography.bodyLarge,
                    )
                },
                onOptionClicked = { localeOption ->
                    component.onLocaleOptionClick(localeOption)
                },
                modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)
            )
            CustomExposedDropdownMenu(
                options = levelLimiters,
                label = stringResource(Res.string.level_limit),
                initialValue = when (settingsState.maxLevel) {
                    MaxLevel.Standard -> stringResource(Res.string.standard)
                    MaxLevel.EpicMunchkin -> stringResource(Res.string.epic_munchkin)
                    MaxLevel.Unlimited -> stringResource(Res.string.unlimited)
                },
                dropdownText = { maxLevelOption ->
                    Text(
                        text = stringResource(maxLevelOption.localizedName),
                        style = MaterialTheme.typography.bodyLarge,
                    )
                },
                onOptionClicked = { maxLevelOption ->
                    component.onMaxLevelOptionClick(maxLevelOption)
                },
                modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)
            )
            SwitchButton(
                title = stringResource(Res.string.show_leaders),
                checked = settingsState.showLeaders,
                onCheckedChange = { newValue ->
                    component.onShowLeadersClick(newValue)
                },
                modifier = Modifier.padding(horizontal = 16.dp, vertical = 4.dp)
            )
            if (platformType == PlatformType.Android) {
                SwitchButton(
                    title = stringResource(Res.string.keep_screen_on),
                    checked = settingsState.keepScreenOn,
                    onCheckedChange = { newValue ->
                        component.onKeepScreenOnClick(newValue)
                    },
                    modifier = Modifier.padding(horizontal = 16.dp, vertical = 4.dp)
                )
            }
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
