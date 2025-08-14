package org.mefetran.munchkinmaster.ui.screen.player

import org.mefetran.munchkinmaster.model.Player

data class PlayerUiState(
    val isLoading: Boolean = true,
    val player: Player? = null,
)