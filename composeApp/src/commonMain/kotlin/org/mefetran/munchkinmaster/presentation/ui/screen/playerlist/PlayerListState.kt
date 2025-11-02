package org.mefetran.munchkinmaster.presentation.ui.screen.playerlist

import androidx.compose.runtime.snapshots.SnapshotStateSet
import org.jetbrains.compose.resources.StringResource

sealed interface PlayerListState {
    val errorMessageResId: StringResource?

    data class MainState(
        override val errorMessageResId: StringResource? = null,
    ) : PlayerListState

    data class DeleteMode(
        override val errorMessageResId: StringResource? = null,
        val playerIdsToDelete: SnapshotStateSet<Long> = SnapshotStateSet()
    ) : PlayerListState
}