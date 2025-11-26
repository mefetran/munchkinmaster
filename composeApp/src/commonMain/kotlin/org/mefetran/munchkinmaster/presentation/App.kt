package org.mefetran.munchkinmaster.presentation

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.getValue
import androidx.compose.runtime.key
import androidx.compose.ui.Modifier
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import org.jetbrains.compose.ui.tooling.preview.Preview
import org.mefetran.munchkinmaster.presentation.ui.screen.root.RootComponent
import org.mefetran.munchkinmaster.presentation.ui.screen.root.RootContent
import org.mefetran.munchkinmaster.presentation.ui.theme.MunchkinMasterTheme
import org.mefetran.munchkinmaster.presentation.ui.theme.ThemeManager
import org.mefetran.munchkinmaster.presentation.ui.uikit.util.LocalAppLocale
import org.mefetran.munchkinmaster.presentation.ui.screen.settings.SettingsManager
import org.mefetran.munchkinmaster.presentation.ui.uikit.util.KeepScreenOn

@Composable
@Preview
fun App(rootComponent: RootComponent) {
    val themeData by ThemeManager.themeDataState.collectAsStateWithLifecycle()
    val settingsState by SettingsManager.state.collectAsStateWithLifecycle()

    KeepScreenOn(settingsState.keepScreenOn)

    MunchkinMasterTheme(
        darkTheme = when (themeData.isSystemTheme) {
            true -> isSystemInDarkTheme()
            false -> themeData.isDarkTheme
        }
    ) {
        CompositionLocalProvider(
            LocalAppLocale provides settingsState.locale
        ) {
            key(settingsState.locale) {
                RootContent(
                    component = rootComponent,
                    modifier = Modifier.fillMaxSize()
                )
            }
        }
    }
}