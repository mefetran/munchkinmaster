package org.mefetran.munchkinmaster.presentation.ui.screen.playerlist

import androidx.compose.animation.AnimatedContent
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawing
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.backhandler.BackHandler
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.DialogProperties
import com.arkivanov.decompose.extensions.compose.subscribeAsState
import munchkinmaster.composeapp.generated.resources.Res
import munchkinmaster.composeapp.generated.resources.cancel
import munchkinmaster.composeapp.generated.resources.players_title
import org.jetbrains.compose.resources.stringResource
import org.jetbrains.compose.ui.tooling.preview.Preview
import org.mefetran.munchkinmaster.presentation.ui.uikit.card.PlayerItem
import org.mefetran.munchkinmaster.presentation.ui.uikit.dialog.ErrorDialog
import org.mefetran.munchkinmaster.presentation.ui.uikit.util.conditional

@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun PlayerListScreen(
    component: PlayerListComponent,
    modifier: Modifier = Modifier,
) {
    val playerList by component.playerListState.subscribeAsState()
    val state by component.state.subscribeAsState()
    val maxLevelState = remember(playerList) {
        val playerLevel = playerList.firstOrNull()?.level
        val equalLevels = playerList.all { player -> player.level == playerLevel }
        if (equalLevels) null
        else {
            derivedStateOf { playerList.maxOfOrNull { player -> player.level } }
        }
    }

    BackHandler(
        enabled = state is PlayerListState.DeleteMode
    ) {
        component.onDeleteModeOff()
    }

    Surface(
        color = MaterialTheme.colorScheme.surface,
        modifier = modifier.fillMaxSize()
    ) {
        LazyColumn(
            contentPadding = WindowInsets.safeDrawing.asPaddingValues()
        ) {
            item {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.padding(16.dp).fillMaxWidth().height(56.dp)
                ) {
                    Text(
                        text = stringResource(Res.string.players_title),
                        style = MaterialTheme.typography.headlineMedium.copy(color = MaterialTheme.colorScheme.primary),
                        modifier = Modifier.weight(1f)
                    )
                    AnimatedContent(
                        targetState = state
                    ) { currentState ->
                        when (currentState) {
                            is PlayerListState.DeleteMode -> {
                                Row(
                                    verticalAlignment = Alignment.CenterVertically,
                                ) {
                                    TextButton(
                                        onClick = component::onDeleteModeOff
                                    ) {
                                        Text(
                                            text = stringResource(Res.string.cancel),
                                            style = MaterialTheme.typography.titleMedium
                                                .copy(color = MaterialTheme.colorScheme.primary),
                                        )
                                    }
                                    IconButton(
                                        onClick = component::onDeletePlayers,
                                        modifier = Modifier.padding(start = 8.dp)
                                    ) {
                                        Row {
                                            if (currentState.playerIdsToDelete.isNotEmpty()) {
                                                Text(
                                                    text = "${currentState.playerIdsToDelete.size}",
                                                    style = MaterialTheme.typography.titleMedium.copy(
                                                        color = MaterialTheme.colorScheme.primary
                                                    ),
                                                )
                                            }
                                            Icon(
                                                imageVector = Icons.Filled.Delete,
                                                contentDescription = null,
                                                tint = MaterialTheme.colorScheme.primary
                                            )
                                        }
                                    }
                                }
                            }

                            is PlayerListState.MainState -> {
                                Row(
                                    verticalAlignment = Alignment.CenterVertically,
                                ) {
                                    IconButton(
                                        onClick = component::onAddPlayerClick,
                                    ) {
                                        Icon(
                                            imageVector = Icons.Filled.Add,
                                            contentDescription = null,
                                            tint = MaterialTheme.colorScheme.primary
                                        )
                                    }
                                    IconButton(
                                        onClick = component::onSettingsClick,
                                    ) {
                                        Icon(
                                            imageVector = Icons.Filled.Settings,
                                            contentDescription = null,
                                            tint = MaterialTheme.colorScheme.primary
                                        )
                                    }
                                }
                            }
                        }
                    }
                }
            }
            items(playerList) { player ->
                PlayerItem(
                    player = player,
                    highlight = player.level == maxLevelState?.value,
                    modifier = Modifier
                        .padding(horizontal = 16.dp, vertical = 8.dp)
                        .conditional(
                            condition = when (val currentState = state) {
                                is PlayerListState.DeleteMode -> currentState.playerIdsToDelete.contains(
                                    player.id
                                )

                                is PlayerListState.MainState -> false
                            },
                            ifTrue = {
                                border(
                                    width = 2.dp,
                                    color = MaterialTheme.colorScheme.secondary,
                                    shape = CardDefaults.shape,
                                )
                            },
                        ),
                    onClick = {
                        when (state) {
                            is PlayerListState.DeleteMode -> component.onAddToDelete(player.id)
                            is PlayerListState.MainState -> component.onPlayerClick(player.id)
                        }
                    },
                    onLongClick = {
                        when (state) {
                            is PlayerListState.DeleteMode -> {}
                            is PlayerListState.MainState -> {
                                component.onDeleteModeOn()
                                component.onAddToDelete(player.id)
                            }
                        }
                    }
                )
            }
        }

        state.errorMessageResId?.let { errorMessageResId ->
            ErrorDialog(
                errorMessageResId = errorMessageResId,
                onDismissRequest = component::hideErrorMessage,
                onOkClick = component::hideErrorMessage,
                properties = DialogProperties(dismissOnClickOutside = false)
            )
        }
    }
}

@Preview(showBackground = true, backgroundColor = 0xFFFFFFFF, locale = "ru")
@Composable
fun PlayerListScreenPreview() {
    PlayerListScreen(
        component = FakePlayerListComponent()
    )
}
