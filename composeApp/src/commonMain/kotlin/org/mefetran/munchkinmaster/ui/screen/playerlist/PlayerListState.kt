package org.mefetran.munchkinmaster.ui.screen.playerlist

import org.jetbrains.compose.resources.StringResource

data class PlayerListState(
    val isDeleteMode: Boolean = false,
    val errorMessageResId: StringResource? = null,
    val playerIdsToDelete: Set<Long> = emptySet(),
)