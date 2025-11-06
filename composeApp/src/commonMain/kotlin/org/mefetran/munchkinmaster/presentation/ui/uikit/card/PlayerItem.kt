package org.mefetran.munchkinmaster.presentation.ui.uikit.card

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Female
import androidx.compose.material.icons.filled.Male
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.ripple
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.dropShadow
import androidx.compose.ui.draw.innerShadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import munchkinmaster.composeapp.generated.resources.Res
import munchkinmaster.composeapp.generated.resources.ic_level
import munchkinmaster.composeapp.generated.resources.ic_power
import munchkinmaster.composeapp.generated.resources.ic_total_power
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.ui.tooling.preview.Preview
import org.mefetran.munchkinmaster.domain.model.Avatar
import org.mefetran.munchkinmaster.domain.model.Player
import org.mefetran.munchkinmaster.domain.model.Sex
import org.mefetran.munchkinmaster.presentation.ui.uikit.util.conditional
import org.mefetran.munchkinmaster.presentation.ui.uikit.util.getAndroidContext
import org.mefetran.munchkinmaster.presentation.ui.uikit.util.performHapticFeedback
import org.mefetran.munchkinmaster.presentation.util.getDrawableResource
import org.mefetran.munchkinmaster.presentation.util.totalStrength

@Composable
fun PlayerItem(
    player: Player,
    modifier: Modifier = Modifier,
    highlight: Boolean = false,
    onClick: () -> Unit,
    onLongClick: () -> Unit,
) {
    val context = getAndroidContext()
    val glowColor = MaterialTheme.colorScheme.tertiary

    Card(
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.primaryContainer,
        ),
        border = if (highlight) BorderStroke(1.dp, color = glowColor) else null,
        modifier = modifier
            .conditional(
                condition = highlight,
                ifTrue = {
                    dropShadow(
                        shape = CardDefaults.shape
                    ) {
                        radius = 45f
                        color = glowColor
                    }
                }
            )
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
            )
            .conditional(
                condition = highlight,
                ifTrue = {
                    innerShadow(
                        shape = CardDefaults.shape
                    ) {
                        radius = 75f
                        color = glowColor
                    }
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

@Preview(showBackground = true, backgroundColor = 0xFFFFFFFF, locale = "en")
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
        highlight = true,
        modifier = Modifier.padding(32.dp),
        onClick = {},
        onLongClick = {},
    )
}
