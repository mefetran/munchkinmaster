package org.mefetran.munchkinmaster.presentation.ui.screen.battle

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.spring
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.calculateEndPadding
import androidx.compose.foundation.layout.calculateStartPadding
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawing
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.PageSize
import androidx.compose.foundation.pager.PagerState
import androidx.compose.foundation.pager.VerticalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowUpward
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.minimumInteractiveComponentSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.arkivanov.decompose.extensions.compose.subscribeAsState
import munchkinmaster.composeapp.generated.resources.Res
import munchkinmaster.composeapp.generated.resources.ic_dice
import munchkinmaster.composeapp.generated.resources.monster
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource
import org.mefetran.munchkinmaster.domain.model.Monster
import org.mefetran.munchkinmaster.domain.model.Player
import org.mefetran.munchkinmaster.presentation.ui.screen.avatar.AvatarModalBottomSheet
import org.mefetran.munchkinmaster.presentation.ui.screen.dice.DiceScreen
import org.mefetran.munchkinmaster.presentation.ui.screen.selectplayer.SelectPlayerModalBottomSheet
import org.mefetran.munchkinmaster.presentation.ui.uikit.card.BattleMonsterCard
import org.mefetran.munchkinmaster.presentation.ui.uikit.card.BattlePlayerCard
import org.mefetran.munchkinmaster.presentation.ui.uikit.util.WindowSizeClass
import org.mefetran.munchkinmaster.presentation.ui.uikit.util.conditional
import org.mefetran.munchkinmaster.presentation.ui.uikit.util.currentWindowSizeClass
import org.mefetran.munchkinmaster.presentation.ui.uikit.util.rememberScreenHeight
import org.mefetran.munchkinmaster.presentation.ui.uikit.util.rememberScreenWidth

private const val ArrowRotateDegrees = 180f
private const val HorizontalPageSizeFixedMediumScreen = 440
private const val HorizontalPageSizeFixedExpandedScreen = 440
private const val VerticalPlayerPageSizeFixedExpandedScreen = 208
private const val VerticalMonsterPageSizeFixedExpandedScreen = 192
private const val HorizontalPadding = 24
private const val PageSpacingPadding = 12
private const val ScoreVerticalPadding = 16
private const val ScoreHorizontalPadding = 4

@Composable
fun BattleScreen(
    component: BattleComponent,
    modifier: Modifier = Modifier,
) {
    val windowSizeClass = currentWindowSizeClass()
    val screenWidth = rememberScreenWidth()
    val screenHeight = rememberScreenHeight()
    val horizontalPageSizeFixed = when {
        windowSizeClass.isWidthAtLeastBreakpoint(WindowSizeClass.WIDTH_DP_EXPANDED_LOWER_BOUND) -> HorizontalPageSizeFixedExpandedScreen.dp
        windowSizeClass.isWidthAtLeastBreakpoint(WindowSizeClass.WIDTH_DP_MEDIUM_LOWER_BOUND) -> HorizontalPageSizeFixedMediumScreen.dp
        else -> screenWidth - HorizontalPadding.dp * 2
    }
    val verticalPlayerPageSizeFixed = when {
        windowSizeClass.isHeightAtLeastBreakpoint(WindowSizeClass.WIDTH_DP_EXPANDED_LOWER_BOUND) -> VerticalPlayerPageSizeFixedExpandedScreen.dp

        else -> screenHeight / 4
    }
    val verticalMonsterPageSizeFixed = when {
        windowSizeClass.isHeightAtLeastBreakpoint(WindowSizeClass.WIDTH_DP_EXPANDED_LOWER_BOUND) -> VerticalMonsterPageSizeFixedExpandedScreen.dp

        else -> screenHeight / 4
    }
    val layoutDirection = LocalLayoutDirection.current
    val playersPagerState = rememberPagerState(pageCount = { component.players.size })
    val monstersPagerState = rememberPagerState(pageCount = { component.monsters.size })
    val isVerticalPager by remember(windowSizeClass) {
        mutableStateOf(windowSizeClass.isHeightAtLeastBreakpoint(WindowSizeClass.HEIGHT_DP_EXPANDED_LOWER_BOUND))
    }
    val isWide by remember(windowSizeClass) { mutableStateOf(windowSizeClass.isWidthAtLeastBreakpoint(WindowSizeClass.WIDTH_DP_EXPANDED_LOWER_BOUND)) }
    val windowInsetsPaddings = WindowInsets.safeDrawing.asPaddingValues()
    val selectAvatarSlot by component.selectAvatarSlot.subscribeAsState()
    val selectPlayerSlot by component.selectPlayerSlot.subscribeAsState()
    val diceSlot by component.diceSlot.subscribeAsState()

    Surface(
        color = MaterialTheme.colorScheme.surface,
        modifier = modifier.fillMaxSize()
    ) {
        LaunchedEffect(component.players.size) {
            if (component.players.isNotEmpty()) {
                playersPagerState.animateScrollToPage(playersPagerState.pageCount - 1)
            }
        }

        LaunchedEffect(component.monsters.size) {
            if (component.monsters.isNotEmpty()) {
                monstersPagerState.animateScrollToPage(monstersPagerState.pageCount - 1)
            }
        }

        Box(
            modifier = Modifier
                .fillMaxSize()
                .conditional(
                    condition = !isVerticalPager,
                    ifTrue = {
                        verticalScroll(rememberScrollState())
                    }
                ),
        ) {
            Column(
                modifier = Modifier
                    .padding(vertical = 56.dp)
                    .align(Alignment.Center),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {
                if (!isVerticalPager) {
                    AddPlayerButton(
                        enabled = component.players.size < 2,
                        onClick = component::onAddPlayerClick
                    )
                }
                if (isVerticalPager) {
                    PlayersVerticalPager(
                        players = component.players,
                        state = playersPagerState,
                        pageSize = verticalPlayerPageSizeFixed,
                        cardWidth = horizontalPageSizeFixed,
                        onPlayerAvatarClick = component::onPlayerAvatarClick,
                        onPlayerSexChange = component::onPlayerSexChange,
                        onPlayerLevelChange = component::onPlayerLevelChange,
                        onPlayerPowerChange = component::onPlayerPowerChange,
                        onPlayerModificatorChange = component::onPlayerModificatorChange,
                        onDeletePlayerClick = component::onDeletePlayerClick,
                    )
                } else {
                    PlayersHorizontalPager(
                        players = component.players,
                        state = playersPagerState,
                        contentPadding = PaddingValues(
                            start = windowInsetsPaddings
                                .calculateStartPadding(layoutDirection) + HorizontalPadding.dp,
                            end = windowInsetsPaddings
                                .calculateEndPadding(layoutDirection) + HorizontalPadding.dp,
                        ),
                        pageSize = horizontalPageSizeFixed,
                        onPlayerAvatarClick = component::onPlayerAvatarClick,
                        onPlayerSexChange = component::onPlayerSexChange,
                        onPlayerLevelChange = component::onPlayerLevelChange,
                        onPlayerPowerChange = component::onPlayerPowerChange,
                        onPlayerModificatorChange = component::onPlayerModificatorChange,
                        onDeletePlayerClick = component::onDeletePlayerClick,
                    )
                }
                if (isVerticalPager) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        AddPlayerButton(
                            enabled = component.players.size < 2,
                            onClick = component::onAddPlayerClick
                        )
                        BattleScoreBlock(
                            playersPower = component
                                .players
                                .sumOf { it.level + it.power + it.modificator },
                            monstersPower = component
                                .monsters
                                .sumOf { it.level + it.modificator },
                            modifier = Modifier.padding(vertical = 8.dp)
                        )
                        AddMonsterButton(
                            onClick = component::onAddMonsterClick
                        )
                    }
                } else {
                    BattleScoreBlock(
                        playersPower = component
                            .players
                            .sumOf { it.level + it.power + it.modificator },
                        monstersPower = component
                            .monsters
                            .sumOf { it.level + it.modificator },
                        modifier = Modifier.padding(vertical = 8.dp)
                    )
                }
                if (isVerticalPager && !isWide) {
                    MonstersVerticalPager(
                        monsters = component.monsters,
                        state = monstersPagerState,
                        pageSize = verticalMonsterPageSizeFixed,
                        cardWidth = horizontalPageSizeFixed,
                        modifier = Modifier.height(if (component.monsters.size > 1) verticalPlayerPageSizeFixed * 2 + 48.dp else verticalPlayerPageSizeFixed),
                        onMonsterLevelChange = component::onMonsterLevelChange,
                        onMonsterModificatorChange = component::onMonsterModificatorChange,
                        onDeleteMonsterClick = component::onDeleteMonsterClick,
                        onCloneMonster = component::onCloneMonster,
                    )
                } else {
                    MonstersHorizontalPager(
                        monsters = component.monsters,
                        state = monstersPagerState,
                        pageSize = horizontalPageSizeFixed,
                        contentPadding = PaddingValues(
                            start = windowInsetsPaddings
                                .calculateStartPadding(layoutDirection) + HorizontalPadding.dp,
                            end = windowInsetsPaddings
                                .calculateEndPadding(layoutDirection) + HorizontalPadding.dp
                        ),
                        onMonsterLevelChange = component::onMonsterLevelChange,
                        onMonsterModificatorChange = component::onMonsterModificatorChange,
                        onDeleteMonsterClick = component::onDeleteMonsterClick,
                        onCloneMonster = component::onCloneMonster,
                    )
                }
                if (!isVerticalPager) {
                    AddMonsterButton(
                        onClick = component::onAddMonsterClick
                    )
                }
            }
            BattleToolbar(
                modifier = Modifier
                    .statusBarsPadding()
                    .align(Alignment.TopCenter),
                onBackClick = component::onBackClick,
                onDice = component::onDice,
            )
        }

        selectAvatarSlot.child?.let { child ->
            AvatarModalBottomSheet(
                component = child.instance,
            )
        }

        selectPlayerSlot.child?.let { child ->
            SelectPlayerModalBottomSheet(
                component = child.instance
            )
        }

        diceSlot.child?.let { child ->
            DiceScreen(child.instance)
        }
    }
}

@Composable
private fun BattleArrowIndicator(
    playersPower: Int,
    monstersPower: Int,
    modifier: Modifier = Modifier,
) {
    val isPlayersWinning by remember(playersPower, monstersPower) {
        derivedStateOf {
            playersPower > monstersPower
        }
    }

    var rotationOffset by remember { mutableFloatStateOf(0f) }

    LaunchedEffect(isPlayersWinning) {
        rotationOffset = if (isPlayersWinning) 0f else ArrowRotateDegrees
    }

    val rotation by animateFloatAsState(
        targetValue = rotationOffset,
        animationSpec = spring(
            dampingRatio = Spring.DampingRatioMediumBouncy,
            stiffness = Spring.StiffnessMedium,
        ),
        label = "arrowRotation"
    )

    Icon(
        imageVector = Icons.Default.ArrowUpward,
        contentDescription = null,
        tint = if (isPlayersWinning) MaterialTheme.colorScheme.primary
        else MaterialTheme.colorScheme.error,
        modifier = modifier
            .size(40.dp)
            .graphicsLayer {
                rotationZ = rotation
            }
    )
}

@Composable
private fun BattleToolbar(
    modifier: Modifier = Modifier,
    onBackClick: () -> Unit,
    onDice: () -> Unit,
) {
    Box(
        contentAlignment = Alignment.Center,
        modifier = modifier.fillMaxWidth()
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
        IconButton(
            onClick = onDice,
            modifier = Modifier.align(Alignment.CenterEnd)
        ) {
            Icon(
                painter = painterResource(Res.drawable.ic_dice),
                contentDescription = null,
                tint = MaterialTheme.colorScheme.primary,
                modifier = Modifier.size(24.dp)
            )
        }
    }
}

@Composable
private fun BattleScoreBlock(
    playersPower: Int,
    monstersPower: Int,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier
    ) {
        Text(
            text = playersPower.toString(),
            style = MaterialTheme.typography.headlineLarge,
            modifier = Modifier
                .padding(bottom = ScoreVerticalPadding.dp)
                .padding(horizontal = ScoreHorizontalPadding.dp)
        )
        BattleArrowIndicator(
            playersPower = playersPower,
            monstersPower = monstersPower,
            modifier = Modifier.align(Alignment.CenterVertically)
        )
        Text(
            text = monstersPower.toString(),
            style = MaterialTheme.typography.headlineLarge,
            modifier = Modifier
                .padding(top = ScoreVerticalPadding.dp)
                .padding(horizontal = ScoreHorizontalPadding.dp)
        )
    }
}

@Composable
private fun AddPlayerButton(
    enabled: Boolean,
    modifier: Modifier = Modifier,
    onClick: () -> Unit,
) {
    AnimatedContent(
        targetState = enabled,
        modifier = modifier,
    ) { state ->
        when (state) {
            true -> {
                IconButton(
                    onClick = onClick,
                ) {
                    Icon(
                        imageVector = Icons.Filled.Add,
                        contentDescription = null,
                    )
                }
            }

            false -> {
                Icon(
                    imageVector = Icons.Filled.Lock,
                    contentDescription = null,
                    modifier = Modifier.minimumInteractiveComponentSize()
                )
            }
        }
    }
}

@Composable
private fun AddMonsterButton(
    modifier: Modifier = Modifier,
    onClick: () -> Unit,
) {
    IconButton(
        onClick = onClick,
        modifier = modifier,
    ) {
        Icon(
            imageVector = Icons.Filled.Add,
            contentDescription = null,
        )
    }
}

@Composable
private fun PlayersVerticalPager(
    players: SnapshotStateList<Player>,
    state: PagerState,
    pageSize: Dp,
    cardWidth: Dp,
    modifier: Modifier = Modifier,
    contentPadding: PaddingValues = PaddingValues(),
    onPlayerAvatarClick: (Player) -> Unit,
    onPlayerSexChange: (Player) -> Unit,
    onPlayerLevelChange: (Player, Int) -> Unit,
    onPlayerPowerChange: (Player, Int) -> Unit,
    onPlayerModificatorChange: (Player, Int) -> Unit,
    onDeletePlayerClick: (Player) -> Unit,
) {
    VerticalPager(
        state = state,
        pageSize = PageSize.Fixed(pageSize),
        pageSpacing = PageSpacingPadding.dp,
        modifier = modifier,
        contentPadding = contentPadding
    ) { page ->
        val player = players[page]
        BattlePlayerCard(
            player = player,
            modifier = Modifier.width(cardWidth),
            onAvatarClick = {
                onPlayerAvatarClick(player)
            },
            onSexChange = {
                onPlayerSexChange(player)
            },
            onLevelChange = { level ->
                onPlayerLevelChange(player, level)
            },
            onPowerChange = { power ->
                onPlayerPowerChange(player, power)
            },
            onModificatorChange = {
                onPlayerModificatorChange(player, it)
            },
            onDeleteClick = if (page > 0) {
                { onDeletePlayerClick(player) }
            } else null
        )
    }
}

@Composable
private fun PlayersHorizontalPager(
    players: SnapshotStateList<Player>,
    state: PagerState,
    pageSize: Dp,
    modifier: Modifier = Modifier,
    contentPadding: PaddingValues = PaddingValues(),
    onPlayerAvatarClick: (Player) -> Unit,
    onPlayerSexChange: (Player) -> Unit,
    onPlayerLevelChange: (Player, Int) -> Unit,
    onPlayerPowerChange: (Player, Int) -> Unit,
    onPlayerModificatorChange: (Player, Int) -> Unit,
    onDeletePlayerClick: (Player) -> Unit,
) {
    HorizontalPager(
        state = state,
        contentPadding = contentPadding,
        pageSize = PageSize.Fixed(pageSize),
        pageSpacing = PageSpacingPadding.dp,
        modifier = modifier,
    ) { page ->
        val player = players[page]
        BattlePlayerCard(
            player = player,
            onAvatarClick = {
                onPlayerAvatarClick(player)
            },
            onSexChange = {
                onPlayerSexChange(player)
            },
            onLevelChange = {
                onPlayerLevelChange(player, it)
            },
            onPowerChange = {
                onPlayerPowerChange(player, it)
            },
            onModificatorChange = {
                onPlayerModificatorChange(player, it)
            },
            onDeleteClick = if (page > 0) {
                { onDeletePlayerClick(player) }
            } else null
        )
    }
}

@Composable
private fun MonstersVerticalPager(
    monsters: SnapshotStateList<Monster>,
    state: PagerState,
    pageSize: Dp,
    cardWidth: Dp,
    modifier: Modifier = Modifier,
    onMonsterLevelChange: (Monster, Int) -> Unit,
    onMonsterModificatorChange: (Monster, Int) -> Unit,
    onDeleteMonsterClick: (Monster) -> Unit,
    onCloneMonster: (Monster) -> Unit,
) {
    VerticalPager(
        state = state,
        pageSize = PageSize.Fixed(pageSize),
        pageSpacing = PageSpacingPadding.dp,
        modifier = modifier
    ) { page ->
        val monster = monsters[page]
        BattleMonsterCard(
            name = stringResource(Res.string.monster) + " ${page + 1}",
            monster = monster,
            modifier = Modifier.width(cardWidth),
            onLevelChange = {
                onMonsterLevelChange(monster, it)
            },
            onModificatorChange = {
                onMonsterModificatorChange(monster, it)
            },
            onDeleteClick = if (monsters.size > 1) {
                {
                    onDeleteMonsterClick(monster)
                }
            } else null,
            onCloneMonsterClick = {
                onCloneMonster(monster)
            }
        )
    }
}

@Composable
private fun MonstersHorizontalPager(
    monsters: SnapshotStateList<Monster>,
    state: PagerState,
    pageSize: Dp,
    modifier: Modifier = Modifier,
    contentPadding: PaddingValues = PaddingValues(),
    onMonsterLevelChange: (Monster, Int) -> Unit,
    onMonsterModificatorChange: (Monster, Int) -> Unit,
    onDeleteMonsterClick: (Monster) -> Unit,
    onCloneMonster: (Monster) -> Unit,
) {
    HorizontalPager(
        state = state,
        pageSize = PageSize.Fixed(pageSize),
        contentPadding = contentPadding,
        pageSpacing = PageSpacingPadding.dp,
        modifier = modifier,
    ) { page ->
        val monster = monsters[page]
        BattleMonsterCard(
            name = stringResource(Res.string.monster) + " ${page + 1}",
            monster = monster,
            onLevelChange = {
                onMonsterLevelChange(monster, it)
            },
            onModificatorChange = {
                onMonsterModificatorChange(monster, it)
            },
            onDeleteClick = if (monsters.size > 1) {
                {
                    onDeleteMonsterClick(monster)
                }
            } else null,
            onCloneMonsterClick = {
                onCloneMonster(monster)
            }
        )
    }
}

@Preview(showBackground = true, backgroundColor = 0xFFFFFFFF, locale = "en")
@Composable
fun BattleScreenPreview() {
    BattleScreen(
        component = FakeBattleComponent(),
    )
}
