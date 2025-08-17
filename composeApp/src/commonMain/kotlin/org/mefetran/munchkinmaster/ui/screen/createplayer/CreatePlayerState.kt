package org.mefetran.munchkinmaster.ui.screen.createplayer

import org.jetbrains.compose.resources.StringResource
import org.mefetran.munchkinmaster.model.Sex

data class CreatePlayerState(
    val sex: Sex = Sex.female,
    val errorMessageResId: StringResource? = null,
)