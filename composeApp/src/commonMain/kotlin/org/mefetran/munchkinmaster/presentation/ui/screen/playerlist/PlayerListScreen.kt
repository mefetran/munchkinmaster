package org.mefetran.munchkinmaster.presentation.ui.screen.playerlist

import androidx.compose.animation.AnimatedContent
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawing
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.grid.rememberLazyGridState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.ReplayCircleFilled
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
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.DialogProperties
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigationevent.NavigationEventInfo
import androidx.navigationevent.compose.NavigationEventHandler
import androidx.navigationevent.compose.rememberNavigationEventState
import com.arkivanov.decompose.extensions.compose.subscribeAsState
import munchkinmaster.composeapp.generated.resources.Res
import munchkinmaster.composeapp.generated.resources.cancel
import munchkinmaster.composeapp.generated.resources.ic_dice
import munchkinmaster.composeapp.generated.resources.players_title
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource
import org.mefetran.munchkinmaster.presentation.ui.screen.dice.DiceScreen
import org.mefetran.munchkinmaster.presentation.ui.screen.settings.SettingsManager
import org.mefetran.munchkinmaster.presentation.ui.uikit.card.PlayerItem
import org.mefetran.munchkinmaster.presentation.ui.uikit.dialog.ErrorDialog
import org.mefetran.munchkinmaster.presentation.ui.uikit.util.conditional
import org.mefetran.munchkinmaster.presentation.util.PlatformVerticalScrollbar

private const val GridCellMinSize = 296
private const val ToolbarHeight = 56

@Composable
fun PlayerListScreen(
    component: PlayerListComponent,
    modifier: Modifier = Modifier,
) {
    val gridState = rememberLazyGridState()
    val playerList by component.playerListState.subscribeAsState()
    val state by component.state.subscribeAsState()
    val settingsState by SettingsManager.state.collectAsStateWithLifecycle()
    val maxLevelState = remember(playerList) {
        val playerLevel = playerList.firstOrNull()?.level
        val equalLevels = playerList.all { player -> player.level == playerLevel }
        if (equalLevels) null
        else {
            derivedStateOf { playerList.maxOfOrNull { player -> player.level } }
        }
    }
    val diceSlot by component.diceSlot.subscribeAsState()

    NavigationEventHandler(
        state = rememberNavigationEventState(NavigationEventInfo.None),
        isBackEnabled = state is PlayerListState.DeleteMode,
        onBackCompleted = {
            component.onDeleteModeOff()
        }
    )

    Surface(
        color = MaterialTheme.colorScheme.surface,
        modifier = modifier.fillMaxSize()
    ) {
        Column(
            modifier = Modifier
                .padding(WindowInsets.safeDrawing.asPaddingValues())
                .fillMaxSize()
        ) {
            PlayerListToolbar(
                state = state,
                hasPlayers = playerList.isNotEmpty(),
                modifier = Modifier.padding(horizontal = 16.dp),
                onDeleteModeOff = component::onDeleteModeOff,
                onDeletePlayers = component::onDeletePlayers,
                onResetPlayers = component::onResetPlayers,
                onDice = component::onDice,
                onAddPlayerClick = component::onAddPlayerClick,
                onSettingsClick = component::onSettingsClick
            )
            Box(
                modifier = Modifier.weight(1f).fillMaxWidth(),
            ) {
                LazyVerticalGrid(
                    modifier = Modifier.fillMaxSize(),
                    state = gridState,
                    columns = GridCells.Adaptive(minSize = GridCellMinSize.dp),
                ) {
                    items(playerList) { player ->
                        PlayerItem(
                            player = player,
                            highlight = player.level == maxLevelState?.value && settingsState.showLeaders,
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
                                    is PlayerListState.DeleteMode -> {
                                        component.onAddToDelete(player.id)
                                    }
                                    is PlayerListState.MainState -> {
                                        component.onDeleteModeOn()
                                        component.onAddToDelete(player.id)
                                    }
                                }
                            }
                        )
                    }
                }
                PlatformVerticalScrollbar(
                    scrollState = gridState,
                    modifier = Modifier.align(Alignment.CenterEnd).fillMaxHeight(),
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

        diceSlot.child?.let { child ->
            DiceScreen(child.instance)
        }
    }
}

@Composable
private fun DeleteModeBlock(
    hasPlayersToDelete: Boolean,
    amountOfPlayersToDelete: Int,
    modifier: Modifier = Modifier,
    onDeleteModeOff: () -> Unit,
    onDeletePlayers: () -> Unit,
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = modifier,
    ) {
        TextButton(
            onClick = onDeleteModeOff
        ) {
            Text(
                text = stringResource(Res.string.cancel),
                style = MaterialTheme.typography.titleMedium
                    .copy(color = MaterialTheme.colorScheme.primary),
            )
        }
        IconButton(
            onClick = onDeletePlayers,
            modifier = Modifier.padding(start = 8.dp)
        ) {
            Row {
                if (hasPlayersToDelete) {
                    Text(
                        text = "$amountOfPlayersToDelete",
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

@Composable
private fun MainStateBlock(
    hasPlayers: Boolean,
    modifier: Modifier = Modifier,
    onResetPlayers: () -> Unit,
    onDice: () -> Unit,
    onAddPlayerClick: () -> Unit,
    onSettingsClick: () -> Unit,
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = modifier,
    ) {
        if (hasPlayers) {
            IconButton(
                onClick = onResetPlayers,
            ) {
                Icon(
                    imageVector = Icons.Filled.ReplayCircleFilled,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.primary
                )
            }
        }
        IconButton(
            onClick = onDice,
        ) {
            Icon(
                painter = painterResource(Res.drawable.ic_dice),
                contentDescription = null,
                tint = MaterialTheme.colorScheme.primary,
                modifier = Modifier.size(24.dp)
            )
        }
        IconButton(
            onClick = onAddPlayerClick,
        ) {
            Icon(
                imageVector = Icons.Filled.Add,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.primary
            )
        }
        IconButton(
            onClick = onSettingsClick,
        ) {
            Icon(
                imageVector = Icons.Filled.Settings,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.primary
            )
        }
    }
}

@Composable
private fun PlayerListToolbar(
    state: PlayerListState,
    hasPlayers: Boolean,
    modifier: Modifier = Modifier,
    onDeleteModeOff: () -> Unit,
    onDeletePlayers: () -> Unit,
    onResetPlayers: () -> Unit,
    onDice: () -> Unit,
    onAddPlayerClick: () -> Unit,
    onSettingsClick: () -> Unit,
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = modifier
            .fillMaxWidth()
            .height(ToolbarHeight.dp)
    ) {
        Text(
            text = stringResource(Res.string.players_title),
            style = MaterialTheme
                .typography
                .headlineMedium
                .copy(color = MaterialTheme.colorScheme.primary),
            modifier = Modifier.weight(1f)
        )
        AnimatedContent(
            targetState = state
        ) { currentState ->
            when (currentState) {
                is PlayerListState.DeleteMode -> {
                    DeleteModeBlock(
                        hasPlayersToDelete = currentState.playerIdsToDelete.isNotEmpty(),
                        amountOfPlayersToDelete = currentState.playerIdsToDelete.size,
                        onDeleteModeOff = onDeleteModeOff,
                        onDeletePlayers = onDeletePlayers,
                    )
                }

                is PlayerListState.MainState -> {
                    MainStateBlock(
                        hasPlayers = hasPlayers,
                        onResetPlayers = onResetPlayers,
                        onDice = onDice,
                        onAddPlayerClick = onAddPlayerClick,
                        onSettingsClick = onSettingsClick
                    )
                }
            }
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
