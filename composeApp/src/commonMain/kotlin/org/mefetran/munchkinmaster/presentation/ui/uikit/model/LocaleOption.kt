package org.mefetran.munchkinmaster.presentation.ui.uikit.model

import munchkinmaster.composeapp.generated.resources.Res
import munchkinmaster.composeapp.generated.resources.english
import munchkinmaster.composeapp.generated.resources.russian
import org.jetbrains.compose.resources.StringResource
import org.mefetran.munchkinmaster.presentation.ui.uikit.util.AppLocale

sealed interface LocaleOption : LocalizedName {
    val code: String
    data object EnglishLocale : LocaleOption {
        override val localizedName: StringResource = Res.string.english
        override val code: String = AppLocale.ENGLISH.code
    }

    data object RussianLocale : LocaleOption {
        override val localizedName: StringResource = Res.string.russian
        override val code: String = AppLocale.RUSSIAN.code
    }
}