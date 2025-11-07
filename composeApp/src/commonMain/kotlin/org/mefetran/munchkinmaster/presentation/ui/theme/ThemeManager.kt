package org.mefetran.munchkinmaster.presentation.ui.theme

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

private const val IsDarkTheme = "is_dark_theme"
private const val IsSystemTheme = "is_system_theme"

data class ThemeData(
    val isDarkTheme: Boolean = true,
    val isSystemTheme: Boolean = true,
)

object ThemeManager: KoinComponent {
    private val dataStore: DataStore<Preferences> by inject()
    private val scope = CoroutineScope(Dispatchers.Default)
    private val _themeDataState = MutableStateFlow(ThemeData())
    val themeDataState = _themeDataState.asStateFlow()

    init {
        scope.launch {
            val isDarkThemeKey = booleanPreferencesKey(IsDarkTheme)
            val isSystemThemeKey = booleanPreferencesKey(IsSystemTheme)
            val isDarkTheme = dataStore.data.first()[isDarkThemeKey]
            val isSystemTheme = dataStore.data.first()[isSystemThemeKey]

            _themeDataState.update {
                it.copy(
                    isDarkTheme = isDarkTheme ?: true,
                    isSystemTheme = isSystemTheme ?: true,
                )
            }
        }
    }

    fun toggleTheme(newThemeData: ThemeData) {
        scope.launch {
            _themeDataState.update { newThemeData }

            val isDarkThemeKey = booleanPreferencesKey(IsDarkTheme)
            val isSystemThemeKey = booleanPreferencesKey(IsSystemTheme)

            dataStore.edit { mutablePreferences ->
                mutablePreferences[isDarkThemeKey] = newThemeData.isDarkTheme
                mutablePreferences[isSystemThemeKey] = newThemeData.isSystemTheme
            }
        }
    }
}
