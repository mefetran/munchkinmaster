package org.mefetran.munchkinmaster.presentation.ui.screen.settings

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject
import org.mefetran.munchkinmaster.domain.model.MaxLevel
import kotlin.getValue

private const val ShowLeadersKey = "show_leaders"
private const val LocaleKey = "locale"
private const val MaxLevelKey = "max_level"
private const val KeepScreenOnKey = "keep_screen_on"

data class SettingsState(
    val showLeaders: Boolean = false,
    val locale: String? = null,
    val maxLevel: MaxLevel = MaxLevel.Standard,
    val keepScreenOn: Boolean = false,
)

object SettingsManager : KoinComponent {
    private val dataStore: DataStore<Preferences> by inject()
    private val scope = CoroutineScope(Dispatchers.Default)
    private val _state = MutableStateFlow(SettingsState())
    val state = _state.asStateFlow()

    init {
        scope.launch {
            val showLeadersPrefsKey = booleanPreferencesKey(ShowLeadersKey)
            val showLeaders = dataStore.data.first()[showLeadersPrefsKey] ?: false
            val localePrefsKey = stringPreferencesKey(LocaleKey)
            val locale = dataStore.data.first()[localePrefsKey]
            val maxLevelPrefsKey = stringPreferencesKey(MaxLevelKey)
            val maxLevelDataStoreValue = dataStore.data.first()[maxLevelPrefsKey]
            val maxLevel = MaxLevel
                .entries
                .firstOrNull { level -> level.name == maxLevelDataStoreValue } ?: MaxLevel.Standard
            val keepScreenOnPrefsKey = booleanPreferencesKey(KeepScreenOnKey)
            val keepScreenOn = dataStore.data.first()[keepScreenOnPrefsKey] ?: false

            _state.update {
                it.copy(
                    showLeaders = showLeaders,
                    locale = locale,
                    maxLevel = maxLevel,
                    keepScreenOn = keepScreenOn,
                )
            }
        }
    }

    fun showLeaders(newValue: Boolean) {
        scope.launch {
            _state.update { it.copy(showLeaders = newValue) }

            val showLeadersPrefsKey = booleanPreferencesKey(ShowLeadersKey)
            dataStore.edit { mutablePreferences ->
                mutablePreferences[showLeadersPrefsKey] = newValue
            }
        }
    }

    fun updateLocale(newValue: String) {
        scope.launch {
            _state.update { it.copy(locale = newValue) }

            val localePrefsKey = stringPreferencesKey(LocaleKey)
            dataStore.edit { mutablePreferences ->
                mutablePreferences[localePrefsKey] = newValue
            }
        }
    }

    fun updateMaxLevel(newMaxLevel: MaxLevel) {
        scope.launch {
            _state.update { it.copy(maxLevel = newMaxLevel) }

            val maxLevelPrefsKey = stringPreferencesKey(MaxLevelKey)
            dataStore.edit { mutablePreferences ->
                mutablePreferences[maxLevelPrefsKey] = newMaxLevel.name
            }
        }
    }

    fun keepScreenOn(isOn: Boolean) {
        scope.launch {
            _state.update { it.copy(keepScreenOn = isOn) }

            val keepScreenOnPrefsKey = booleanPreferencesKey(KeepScreenOnKey)
            dataStore.edit { mutablePreferences ->
                mutablePreferences[keepScreenOnPrefsKey] = isOn
            }
        }
    }
}