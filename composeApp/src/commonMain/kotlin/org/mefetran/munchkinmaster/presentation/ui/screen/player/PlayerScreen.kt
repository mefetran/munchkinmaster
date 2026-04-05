package org.mefetran.munchkinmaster.presentation.ui.screen.player

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Female
import androidx.compose.material.icons.filled.Male
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
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigationevent.NavigationEventInfo
import androidx.navigationevent.compose.NavigationEventHandler
import androidx.navigationevent.compose.rememberNavigationEventState
import com.arkivanov.decompose.extensions.compose.subscribeAsState
import munchkinmaster.composeapp.generated.resources.Res
import munchkinmaster.composeapp.generated.resources.avatar_female_1
import munchkinmaster.composeapp.generated.resources.ic_battle
import munchkinmaster.composeapp.generated.resources.ic_total_power
import munchkinmaster.composeapp.generated.resources.kill
import munchkinmaster.composeapp.generated.resources.level
import munchkinmaster.composeapp.generated.resources.power
import munchkinmaster.composeapp.generated.resources.start_battle
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource
import org.mefetran.munchkinmaster.domain.model.Sex
import org.mefetran.munchkinmaster.presentation.ui.screen.avatar.AvatarModalBottomSheet
import org.mefetran.munchkinmaster.presentation.ui.uikit.card.StatusCard
import org.mefetran.munchkinmaster.presentation.util.PlatformVerticalScrollbar
import org.mefetran.munchkinmaster.presentation.util.getDrawableResource
import org.mefetran.munchkinmaster.presentation.util.totalStrength

private const val StartBattleButtonSize = 72
private const val StartBattleButtonIconSize = StartBattleButtonSize / 2
private const val StatusBlockHorizontalArrangement = 24

@Composable
fun PlayerScreen(
    component: PlayerComponent,
    modifier: Modifier = Modifier,
) {
    val scrollState = rememberScrollState()
    val state by component.state.subscribeAsState()
    val selectAvatarSlot by component.selectAvatarSlot.subscribeAsState()

    NavigationEventHandler(
        state = rememberNavigationEventState(NavigationEventInfo.None),
        onBackCompleted = {
            component.onBackClick()
        }
    )

    Surface(
        color = MaterialTheme.colorScheme.surface,
        modifier = modifier.fillMaxSize()
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .statusBarsPadding()
                .navigationBarsPadding(),
            contentAlignment = Alignment.Center,
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .verticalScroll(scrollState),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Image(
                    painter = painterResource(
                        state.player?.avatar?.getDrawableResource() ?: Res.drawable.avatar_female_1
                    ),
                    contentDescription = null,
                    contentScale = ContentScale.Crop,
                    modifier = Modifier
                        .padding(top = 32.dp)
                        .size(120.dp)
                        .clip(CircleShape)
                        .border(3.dp, MaterialTheme.colorScheme.primary, CircleShape)
                        .clickable(
                            interactionSource = remember { MutableInteractionSource() },
                            indication = ripple(bounded = false),
                            onClick = component::onAvatarClick
                        )
                )
                NameWithSex(
                    name = state.player?.name ?: "",
                    sex = state.player?.sex ?: Sex.female,
                    modifier = Modifier.padding(top = 16.dp),
                    onSexChange = component::onSexChange
                )
                TotalPower(
                    totalPower = state.player?.totalStrength() ?: 0,
                    modifier = Modifier.padding(top = 8.dp)
                )
                Spacer(modifier = Modifier.height(24.dp))
                StatusBlock(
                    level = state.player?.level ?: 0,
                    power = state.player?.power ?: 0,
                    modifier = Modifier.align(Alignment.CenterHorizontally),
                    onLevelChange = component::onLevelChange,
                    onPowerChange = component::onPowerChange
                )
                Spacer(modifier = Modifier.height(32.dp))
                StartBattleButton { component.onBattleClick() }
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    stringResource(Res.string.start_battle),
                    style = MaterialTheme.typography.labelLarge
                )
                Spacer(Modifier.height(32.dp))
            }
            PlayerToolbar(
                modifier = Modifier
                    .align(Alignment.TopCenter),
                onBackClick = component::onBackClick,
                onKillClick = component::onKillClick,
            )
            PlatformVerticalScrollbar(
                scrollState = scrollState,
                modifier = Modifier.align(Alignment.CenterEnd).fillMaxHeight()
            )
        }

        selectAvatarSlot.child?.let { child ->
            AvatarModalBottomSheet(
                component = child.instance,
            )
        }
    }
}

@Composable
private fun StartBattleButton(
    modifier: Modifier = Modifier,
    onClick: () -> Unit,
) {
    Box(
        contentAlignment = Alignment.Center,
        modifier = modifier
            .size(StartBattleButtonSize.dp)
            .clip(CircleShape)
            .background(MaterialTheme.colorScheme.primaryContainer, shape = CircleShape)
            .border(2.dp, MaterialTheme.colorScheme.primary, CircleShape)
            .clickable(
                interactionSource = remember { MutableInteractionSource() },
                indication = ripple(bounded = false),
                onClick = onClick
            )
    ) {
        Icon(
            painter = painterResource(Res.drawable.ic_battle),
            contentDescription = null,
            tint = MaterialTheme.colorScheme.onPrimaryContainer,
            modifier = Modifier.size(StartBattleButtonIconSize.dp)
        )
    }
}

@Composable
private fun PlayerToolbar(
    modifier: Modifier = Modifier,
    onBackClick: () -> Unit,
    onKillClick: () -> Unit,
) {
    Box(
        modifier
            .fillMaxWidth()
            .statusBarsPadding()
    ) {
        IconButton(
            onClick = onBackClick,
            modifier = Modifier.align(Alignment.CenterStart)
        ) {
            Icon(
                imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.primary,
            )
        }
        TextButton(
            onClick = onKillClick,
            modifier = Modifier.align(Alignment.CenterEnd)
        ) {
            Text(
                text = stringResource(Res.string.kill),
                style = MaterialTheme.typography.bodyLarge
            )
        }
    }
}

@Composable
private fun StatusBlock(
    level: Int,
    power: Int,
    modifier: Modifier = Modifier,
    onLevelChange: (Int) -> Unit,
    onPowerChange: (Int) -> Unit,
) {
    Row(
        horizontalArrangement = Arrangement.spacedBy(StatusBlockHorizontalArrangement.dp),
        modifier = modifier
    ) {
        StatusCard(
            title = stringResource(Res.string.level),
            value = level,
            onValueChange = onLevelChange
        )
        StatusCard(
            title = stringResource(Res.string.power),
            value = power,
            onValueChange = onPowerChange
        )
    }
}

@Composable
private fun TotalPower(
    totalPower: Int,
    modifier: Modifier = Modifier
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = modifier
    ) {
        Icon(
            painter = painterResource(Res.drawable.ic_total_power),
            contentDescription = null,
            tint = Color.Unspecified,
            modifier = Modifier.size(24.dp)
        )
        Text(
            text = "$totalPower",
            style = MaterialTheme.typography.titleLarge,
            modifier = Modifier.padding(start = 12.dp)
        )
    }
}

@Composable
private fun NameWithSex(
    name: String,
    sex: Sex,
    modifier: Modifier = Modifier,
    onSexChange: () -> Unit,
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = modifier
    ) {
        Text(
            text = name,
            style = MaterialTheme.typography.headlineSmall,
            fontWeight = FontWeight.Bold
        )
        Icon(
            imageVector = if (sex == Sex.male) Icons.Default.Male else Icons.Default.Female,
            contentDescription = null,
            tint = if (sex == Sex.male) Color.Blue else Color.Magenta,
            modifier = Modifier.padding(start = 8.dp).size(24.dp)
                .clip(RoundedCornerShape(8.dp))
                .clickable(
                    interactionSource = remember { MutableInteractionSource() },
                    indication = ripple(bounded = true),
                    onClick = onSexChange
                )
        )
    }
}

@Preview
@Composable
fun PlayerScreenPreview() {
    PlayerScreen(
        component = FakePlayerComponent(
            playerId = 1,
            onFinished = {},
        ),
    )
}