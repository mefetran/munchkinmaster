package org.mefetran.munchkinmaster.presentation.ui.screen.settings

import com.arkivanov.decompose.ComponentContext
import org.mefetran.munchkinmaster.presentation.ui.theme.ThemeData
import org.mefetran.munchkinmaster.presentation.ui.theme.ThemeManager
import org.mefetran.munchkinmaster.presentation.ui.uikit.model.ThemeOption

interface SettingsComponent {
    fun onBackClick()
    fun onThemeOptionClick(themeOption: ThemeOption)
}

class DefaultSettingsComponent(
    componentContext: ComponentContext,
    private val onFinished: () -> Unit,
) : SettingsComponent, ComponentContext by componentContext {
    override fun onBackClick() {
        onFinished()
    }

    override fun onThemeOptionClick(themeOption: ThemeOption) {
        ThemeManager.toggleTheme(
            newThemeData = when (themeOption) {
                ThemeOption.DarkTheme -> ThemeData(isSystemTheme = false, isDarkTheme = true)
                ThemeOption.LightTheme -> ThemeData(isSystemTheme = false, isDarkTheme = false)
                ThemeOption.SystemTheme -> ThemeData(isSystemTheme = true, isDarkTheme = true)
            }
        )
    }
}

class FakeSettingsComponent : SettingsComponent {
    override fun onBackClick() {

    }

    override fun onThemeOptionClick(themeOption: ThemeOption) {

    }
}