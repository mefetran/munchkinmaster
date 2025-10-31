package org.mefetran.munchkinmaster.presentation.ui.screen.createplayer

import org.jetbrains.compose.resources.StringResource
import org.mefetran.munchkinmaster.domain.model.Avatar
import org.mefetran.munchkinmaster.domain.model.Sex

data class CreatePlayerState(
    val sex: Sex = Sex.female,
    val errorMessageResId: StringResource? = null,
    val selectedAvatar: Avatar = Avatar.female1
)