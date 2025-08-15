package org.mefetran.munchkinmaster.ui.screen.playerlist

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawing
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Female
import androidx.compose.material.icons.filled.Male
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil3.compose.AsyncImage
import com.arkivanov.decompose.extensions.compose.subscribeAsState
import munchkinmaster.composeapp.generated.resources.Res
import munchkinmaster.composeapp.generated.resources.level
import munchkinmaster.composeapp.generated.resources.players_title
import munchkinmaster.composeapp.generated.resources.power
import munchkinmaster.composeapp.generated.resources.total_power
import org.jetbrains.compose.resources.stringResource
import org.jetbrains.compose.ui.tooling.preview.Preview
import org.mefetran.munchkinmaster.model.Player
import org.mefetran.munchkinmaster.model.Sex
import org.mefetran.munchkinmaster.ui.theme.hennyPennyTextStyle

@Composable
fun PlayerListScreen(
    component: PlayerListComponent,
    modifier: Modifier = Modifier,
) {
    val playerList by component.playerListState.subscribeAsState()

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
                    modifier = Modifier.padding(16.dp).fillMaxWidth()
                ) {
                    Text(
                        text = stringResource(Res.string.players_title),
                        style = MaterialTheme.typography.headlineMedium.copy(color = MaterialTheme.colorScheme.primary),
                        modifier = Modifier.weight(1f)
                    )
                    TextButton(
                        onClick = component::onAddPlayerClick
                    ) {
                        Text(
                            text = "+1",
                            style = MaterialTheme.typography.headlineMedium.copy(color = MaterialTheme.colorScheme.primary),
                        )
                    }
                }
            }
            items(playerList) { player ->
                PlayerItem(
                    player = player,
                    modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp),
                    onClick = {
                        component.onPlayerClick(player.id)
                    }
                )
            }
        }
    }
}

@Composable
private fun PlayerItem(
    player: Player,
    modifier: Modifier = Modifier,
    onClick: () -> Unit
) {
    Card(
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.primaryContainer,
        ),
        modifier = modifier.fillMaxWidth(),
        onClick = onClick,
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.padding(12.dp).fillMaxWidth()
        ) {
            AsyncImage(
                model = player.avatar,
                contentDescription = "Avatar",
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .size(56.dp)
                    .clip(CircleShape)
                    .border(3.dp, MaterialTheme.colorScheme.primary, CircleShape)
            )
            Row(Modifier.weight(1f)) {
                Column(
                    modifier = Modifier.padding(start = 8.dp)
                ) {
                    Text(
                        text = player.name,
                        style = MaterialTheme.typography.headlineSmall,
                        fontWeight = FontWeight.Bold
                    )
                    Icon(
                        imageVector = if (player.sex.lowercase() == Sex.male.name) Icons.Default.Male else Icons.Default.Female,
                        contentDescription = "Gender",
                        tint = if (player.sex.lowercase() == Sex.male.name) Color.Blue else Color.Magenta,
                        modifier = Modifier.size(24.dp)
                    )
                }
                Column(
                    modifier = Modifier.padding(start = 8.dp)
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = stringResource(Res.string.level) + ":",
                            style = MaterialTheme.typography.bodyLarge
                        )
                        Text(
                            text = player.level.toString(),
                            style = hennyPennyTextStyle,
                            modifier = Modifier.padding(start = 8.dp)
                        )
                    }
                    Row(
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = stringResource(Res.string.power) + ":",
                            style = MaterialTheme.typography.bodyLarge,
                        )
                        Text(
                            text = player.power.toString(),
                            style = hennyPennyTextStyle,
                            modifier = Modifier.padding(start = 8.dp)
                        )
                    }
                }
            }
            Box(
                contentAlignment = Alignment.Center,
                modifier = Modifier
                    .padding(start = 12.dp)
                    .clip(RoundedCornerShape(16.dp))
                    .border(3.dp, MaterialTheme.colorScheme.primary, RoundedCornerShape(16.dp))
            ) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier.padding(12.dp)
                ) {
                    Text(
                        text = stringResource(Res.string.total_power),
                        style = MaterialTheme.typography.bodyLarge,
                    )
                    Text(
                        text = "${player.level + player.power}",
                        style = hennyPennyTextStyle.copy(fontSize = 24.sp),
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
            sex = "female",
            level = 2,
            power = 2,
            avatar = "",
        ),
        modifier = Modifier.padding(16.dp),
        onClick = {}
    )
}

@Preview
@Composable
fun PlayerListScreenPreview() {
    PlayerListScreen(
        component = MockPlayerListComponent()
    )
}