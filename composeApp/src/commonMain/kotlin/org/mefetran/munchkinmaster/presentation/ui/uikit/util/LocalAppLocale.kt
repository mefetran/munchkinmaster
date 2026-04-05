package org.mefetran.munchkinmaster.presentation.ui.uikit.util

import androidx.compose.runtime.Composable
import androidx.compose.runtime.ProvidedValue

expect object LocalAppLocale {
    val current: String @Composable get
    @Composable infix fun provides(value: String?): ProvidedValue<*>
}

enum class AppLocale(val code: String) {
    RUSSIAN("ru"),
    ENGLISH("en"),
}