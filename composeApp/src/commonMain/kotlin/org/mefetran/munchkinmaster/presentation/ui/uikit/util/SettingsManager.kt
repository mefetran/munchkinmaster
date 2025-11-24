package org.mefetran.munchkinmaster.presentation.ui.uikit.util

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
import kotlin.getValue

private const val ShowLeadersKey = "show_leaders"
private const val LocaleKey = "locale"

data class SettingsState(
    val showLeaders: Boolean = false,
    val locale: String? = null,
)

object SettingsManager : KoinComponent {
    private val dataStore: DataStore<Preferences> by inject()
    private val scope = CoroutineScope(Dispatchers.Default)
    private val _state = MutableStateFlow(SettingsState())
    val state = _state.asStateFlow()

    init {
        scope.launch {
            val showLeadersPrefsKey = booleanPreferencesKey(ShowLeadersKey)
            val localePrefsKey = stringPreferencesKey(LocaleKey)
            val showLeaders = dataStore.data.first()[showLeadersPrefsKey]
            val locale = dataStore.data.first()[localePrefsKey]

            _state.update {
                it.copy(
                    showLeaders = showLeaders ?: false,
                    locale = locale,
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
}