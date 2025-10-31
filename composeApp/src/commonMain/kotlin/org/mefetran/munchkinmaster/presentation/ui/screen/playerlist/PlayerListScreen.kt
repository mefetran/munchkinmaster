package org.mefetran.munchkinmaster.presentation.ui.screen.playerlist

import androidx.compose.animation.AnimatedContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawing
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Female
import androidx.compose.material.icons.filled.Male
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.ripple
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.backhandler.BackHandler
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.DialogProperties
import com.arkivanov.decompose.extensions.compose.subscribeAsState
import munchkinmaster.composeapp.generated.resources.Res
import munchkinmaster.composeapp.generated.resources.cancel
import munchkinmaster.composeapp.generated.resources.ic_level
import munchkinmaster.composeapp.generated.resources.ic_power
import munchkinmaster.composeapp.generated.resources.ic_total_power
import munchkinmaster.composeapp.generated.resources.players_title
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource
import org.jetbrains.compose.ui.tooling.preview.Preview
import org.mefetran.munchkinmaster.domain.model.Avatar
import org.mefetran.munchkinmaster.domain.model.Player
import org.mefetran.munchkinmaster.domain.model.Sex
import org.mefetran.munchkinmaster.domain.model.totalStrength
import org.mefetran.munchkinmaster.presentation.ui.uikit.dialog.ErrorDialog
import org.mefetran.munchkinmaster.presentation.ui.uikit.utils.conditional
import org.mefetran.munchkinmaster.presentation.ui.uikit.utils.getAndroidContext
import org.mefetran.munchkinmaster.presentation.ui.uikit.utils.performHapticFeedback
import org.mefetran.munchkinmaster.presentation.util.getDrawableResource

@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun PlayerListScreen(
    component: PlayerListComponent,
    modifier: Modifier = Modifier,
) {
    val playerList by component.playerListState.subscribeAsState()
    val state by component.state.subscribeAsState()
    val maxLevel = remember(playerList) {
        playerList.maxOfOrNull { player -> player.level }
    }

    BackHandler(
        enabled = state.isDeleteMode
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
                        targetState = state.isDeleteMode
                    ) { currentState ->
                        when (currentState) {
                            true -> {
                                Row(
                                    verticalAlignment = Alignment.CenterVertically,
                                ) {
                                    TextButton(
                                        onClick = component::onDeleteModeOff
                                    ) {
                                        Text(
                                            text = stringResource(Res.string.cancel),
                                            style = MaterialTheme.typography.titleMedium.copy(color = MaterialTheme.colorScheme.primary),
                                        )
                                    }
                                    IconButton(
                                        onClick = component::onDeletePlayers,
                                        modifier = Modifier.padding(start = 8.dp)
                                    ) {
                                        Row {
                                            if (state.playerIdsToDelete.isNotEmpty()) {
                                                Text(
                                                    text = "${state.playerIdsToDelete.size}",
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

                            false -> {
                                IconButton(
                                    onClick = component::onAddPlayerClick,
                                ) {
                                    Icon(
                                        imageVector = Icons.Filled.Add,
                                        contentDescription = null,
                                        tint = MaterialTheme.colorScheme.primary
                                    )
                                }
                            }
                        }
                    }
                }
            }
            items(playerList) { player ->
                PlayerItem(
                    player = player,
                    highlight = player.level == maxLevel,
                    modifier = Modifier
                        .padding(horizontal = 16.dp, vertical = 8.dp)
                        .conditional(
                            condition = state.playerIdsToDelete.contains(player.id),
                            ifTrue = {
                                border(
                                    width = 2.dp,
                                    color = MaterialTheme.colorScheme.secondary,
                                    shape = CardDefaults.shape,
                                )
                            },
                        ),
                    onClick = {
                        when (state.isDeleteMode) {
                            true -> component.onAddToDelete(player.id)
                            false -> component.onPlayerClick(player.id)
                        }
                    },
                    onLongClick = {
                        if (!state.isDeleteMode) {
                            component.onDeleteModeOn()
                            component.onAddToDelete(player.id)
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

@Composable
private fun PlayerItem(
    player: Player,
    modifier: Modifier = Modifier,
    highlight: Boolean = false,
    onClick: () -> Unit,
    onLongClick: () -> Unit,
) {
    val context = getAndroidContext()

    Card(
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.primaryContainer,
        ),
        modifier = modifier
            .fillMaxWidth()
            .clip(CardDefaults.shape)
            .combinedClickable(
                interactionSource = remember { MutableInteractionSource() },
                indication = ripple(),
                onClick = onClick,
                onLongClick = {
                    performHapticFeedback(context)
                    onLongClick()
                }
            ),
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.padding(12.dp).fillMaxWidth()
        ) {
            Image(
                painter = painterResource(player.avatar.getDrawableResource()),
                contentDescription = null,
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .size(56.dp)
                    .clip(CircleShape)
                    .border(3.dp, MaterialTheme.colorScheme.primary, CircleShape)
            )
            Column(
                modifier = Modifier.padding(start = 8.dp)
            ) {
                Row {
                    Text(
                        text = player.name,
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold
                    )
                    Icon(
                        imageVector = if (player.sex == Sex.male) Icons.Default.Male else Icons.Default.Female,
                        contentDescription = "Gender",
                        tint = MaterialTheme.colorScheme.onSurface,
                        modifier = Modifier.size(18.dp)
                    )
                }
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.padding(top = 8.dp)
                ) {
                    Icon(
                        painter = painterResource(Res.drawable.ic_level),
                        contentDescription = null,
                        tint = Color.Unspecified,
                        modifier = Modifier.size(24.dp)
                    )
                    Text(
                        text = player.level.toString(),
                        style = MaterialTheme.typography.titleSmall,
                        color = if (highlight) MaterialTheme.colorScheme.error else Color.Unspecified,
                        modifier = Modifier.padding(start = 12.dp)
                    )
                    Icon(
                        painter = painterResource(Res.drawable.ic_power),
                        contentDescription = null,
                        tint = Color.Unspecified,
                        modifier = Modifier.padding(start = 16.dp).size(24.dp)
                    )
                    Text(
                        text = player.power.toString(),
                        style = MaterialTheme.typography.titleSmall,
                        modifier = Modifier.padding(start = 12.dp)
                    )
                    Icon(
                        painter = painterResource(Res.drawable.ic_total_power),
                        contentDescription = null,
                        tint = Color.Unspecified,
                        modifier = Modifier.padding(start = 16.dp).size(24.dp)
                    )
                    Text(
                        text = "${player.totalStrength()}",
                        style = MaterialTheme.typography.titleSmall,
                        modifier = Modifier.padding(start = 12.dp)
                    )
                }
            }
        }
    }
}

@Preview
@Composable
fun PlayerItemPreview() {
    PlayerItem(
        player = Player(
            id = 2,
            name = "Lida",
            sex = Sex.female,
            level = 2,
            power = 2,
            avatar = Avatar.female2,
        ),
        modifier = Modifier.padding(16.dp),
        onClick = {},
        onLongClick = {},
    )
}

@Preview
@Composable
fun PlayerListScreenPreview() {
    PlayerListScreen(
        component = FakePlayerListComponent()
    )
}