package org.mefetran.munchkinmaster.presentation.ui.screen.settings

import com.arkivanov.decompose.ComponentContext
import org.mefetran.munchkinmaster.presentation.ui.theme.ThemeData
import org.mefetran.munchkinmaster.presentation.ui.theme.ThemeManager
import org.mefetran.munchkinmaster.presentation.ui.uikit.model.LocaleOption
import org.mefetran.munchkinmaster.presentation.ui.uikit.model.ThemeOption
import org.mefetran.munchkinmaster.presentation.ui.uikit.util.SettingsManager

interface SettingsComponent {
    fun onBackClick()
    fun onThemeOptionClick(themeOption: ThemeOption)
    fun onShowLeadersClick(newValue: Boolean)
    fun onLocaleOptionClick(localeOption: LocaleOption)
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

    override fun onShowLeadersClick(newValue: Boolean) {
        SettingsManager.showLeaders(newValue)
    }

    override fun onLocaleOptionClick(localeOption: LocaleOption) {
        SettingsManager.updateLocale(
            newValue = localeOption.code
        )
    }
}

class FakeSettingsComponent : SettingsComponent {
    override fun onBackClick() {

    }

    override fun onThemeOptionClick(themeOption: ThemeOption) {

    }

    override fun onShowLeadersClick(newValue: Boolean) {

    }

    override fun onLocaleOptionClick(localeOption: LocaleOption) {

    }
}
