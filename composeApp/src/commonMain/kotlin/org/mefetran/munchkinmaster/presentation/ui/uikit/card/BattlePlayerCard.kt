package org.mefetran.munchkinmaster.presentation.ui.uikit.card

import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Female
import androidx.compose.material.icons.filled.Male
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.ripple
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import munchkinmaster.composeapp.generated.resources.Res
import munchkinmaster.composeapp.generated.resources.ic_total_power
import munchkinmaster.composeapp.generated.resources.level
import munchkinmaster.composeapp.generated.resources.modificator
import munchkinmaster.composeapp.generated.resources.power
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource
import org.jetbrains.compose.ui.tooling.preview.Preview
import org.mefetran.munchkinmaster.domain.model.Avatar
import org.mefetran.munchkinmaster.domain.model.Player
import org.mefetran.munchkinmaster.domain.model.Sex
import org.mefetran.munchkinmaster.presentation.util.getDrawableResource
import org.mefetran.munchkinmaster.presentation.util.totalStrength

@Composable
fun BattlePlayerCard(
    player: Player,
    modifier: Modifier = Modifier,
    onAvatarClick: () -> Unit,
    onSexChange: () -> Unit,
    onLevelChange: (Int) -> Unit,
    onPowerChange: (Int) -> Unit,
    onModificatorChange: (Int) -> Unit,
    onDeleteClick: (() -> Unit)? = null,
) {
    Card(
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.primaryContainer,
        ),
        modifier = modifier,
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .padding(top = 12.dp)
                .padding(horizontal = 12.dp)
                .fillMaxWidth(),
        ) {
            Image(
                painter = painterResource(
                    player.avatar.getDrawableResource()
                ),
                contentDescription = null,
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .size(64.dp)
                    .clip(CircleShape)
                    .border(3.dp, MaterialTheme.colorScheme.primary, CircleShape)
                    .clickable(
                        interactionSource = remember { MutableInteractionSource() },
                        indication = ripple(bounded = false),
                        onClick = onAvatarClick
                    )
            )
            Column(
                modifier = Modifier.padding(start = 8.dp).weight(1f)
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    Text(
                        text = player.name,
                        style = MaterialTheme.typography.headlineSmall,
                        fontWeight = FontWeight.Bold
                    )
                    Icon(
                        imageVector = if (player.sex == Sex.male) Icons.Default.Male else Icons.Default.Female,
                        contentDescription = null,
                        tint = if (player.sex == Sex.male) Color.Blue else Color.Magenta,
                        modifier = Modifier.padding(start = 8.dp).size(24.dp)
                            .clip(RoundedCornerShape(8.dp))
                            .clickable(
                                interactionSource = remember { MutableInteractionSource() },
                                indication = ripple(bounded = true),
                                onClick = onSexChange
                            )
                    )
                }
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    Icon(
                        painter = painterResource(Res.drawable.ic_total_power),
                        contentDescription = null,
                        tint = Color.Unspecified,
                        modifier = Modifier.size(24.dp)
                    )
                    Text(
                        text = "${player.totalStrength()}",
                        style = MaterialTheme.typography.titleLarge,
                        modifier = Modifier.padding(start = 12.dp)
                    )
                }
            }
            onDeleteClick?.let {
                IconButton(
                    onClick = it,
                ) {
                    Icon(
                        imageVector = Icons.Default.Close,
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.onPrimaryContainer
                    )
                }
            }
        }
        Row(
            horizontalArrangement = Arrangement.SpaceBetween,
            modifier = Modifier.padding(12.dp).fillMaxWidth()
        ) {
            StatusCard(
                title = stringResource(Res.string.level),
                value = player.level,
                onValueChange = onLevelChange
            )
            StatusCard(
                title = stringResource(Res.string.power),
                value = player.power,
                onValueChange = onPowerChange
            )
            StatusCard(
                title = stringResource(Res.string.modificator),
                value = player.modificator,
                onValueChange = onModificatorChange
            )
        }
    }
}

@Preview(showBackground = true, backgroundColor = 0xFFFFFFFF, locale = "en")
@Composable
fun BattlePlayerCardPreview() {
    BattlePlayerCard(
        player = Player(
            id = 1,
            name = "Denis",
            sex = Sex.male,
            level = 3,
            power = 2,
            avatar = Avatar.female2,
            modificator = 1,
        ),
        onAvatarClick = {},
        onSexChange = {},
        onLevelChange = {},
        onModificatorChange = {},
        onPowerChange = {},
        modifier = Modifier.padding(16.dp),
        onDeleteClick = {},
    )
}