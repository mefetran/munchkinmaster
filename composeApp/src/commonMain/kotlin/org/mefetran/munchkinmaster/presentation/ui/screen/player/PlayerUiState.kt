package org.mefetran.munchkinmaster.presentation.ui.screen.player

import org.mefetran.munchkinmaster.domain.model.Player

data class PlayerUiState(
    val isLoading: Boolean = true,
    val player: Player? = null,
)