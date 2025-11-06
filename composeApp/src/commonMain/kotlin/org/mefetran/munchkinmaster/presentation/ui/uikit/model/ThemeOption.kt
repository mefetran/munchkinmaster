package org.mefetran.munchkinmaster.presentation.ui.uikit.model

import munchkinmaster.composeapp.generated.resources.Res
import munchkinmaster.composeapp.generated.resources.dark
import munchkinmaster.composeapp.generated.resources.light
import munchkinmaster.composeapp.generated.resources.system_theme
import org.jetbrains.compose.resources.StringResource

sealed interface ThemeOption : LocalizedName {
    data object SystemTheme : ThemeOption {
        override val localizedName: StringResource = Res.string.system_theme
    }

    data object DarkTheme : ThemeOption {
        override val localizedName: StringResource = Res.string.dark
    }

    data object LightTheme : ThemeOption {
        override val localizedName: StringResource = Res.string.light
    }
}