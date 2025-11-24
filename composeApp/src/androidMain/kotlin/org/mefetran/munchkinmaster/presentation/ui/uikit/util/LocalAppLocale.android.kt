package org.mefetran.munchkinmaster.presentation.ui.uikit.util

import android.util.Log
import androidx.compose.runtime.Composable
import androidx.compose.runtime.ProvidedValue
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalResources
import java.util.Locale

actual object LocalAppLocale {

    private var default: Locale? = null
    actual val current: String
        @Composable get() = Locale.getDefault().toString().substring(0, 2)

    @Composable
    actual infix fun provides(value: String?): ProvidedValue<*> {
        val configuration = LocalConfiguration.current

        Log.d("my", "Locale: $value")

        if (default == null) {
            default = Locale.getDefault()
        }

        val new = when(value) {
            null -> default!!
            else -> Locale.forLanguageTag(value)
        }

        Locale.setDefault(new)
        configuration.setLocale(new)
        val resources = LocalResources.current

        resources.updateConfiguration(configuration, resources.displayMetrics)

        return LocalConfiguration.provides(configuration)
    }
}