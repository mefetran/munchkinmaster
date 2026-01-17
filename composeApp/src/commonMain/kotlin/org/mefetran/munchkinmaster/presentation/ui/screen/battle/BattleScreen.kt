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
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawing
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.PageSize
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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.unit.dp
import com.arkivanov.decompose.extensions.compose.subscribeAsState
import munchkinmaster.composeapp.generated.resources.Res
import munchkinmaster.composeapp.generated.resources.ic_dice
import munchkinmaster.composeapp.generated.resources.monster
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource
import org.jetbrains.compose.ui.tooling.preview.Preview
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
private const val IconHorizontalPadding = 16

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
    val playersPower by remember {
        derivedStateOf { component.players.sumOf { it.level + it.power + it.modificator } }
    }
    val monstersPower by remember {
        derivedStateOf { component.monsters.sumOf { it.level + it.modificator } }
    }
    val showAddPlayerIcon by remember {
        derivedStateOf { component.players.size < 2 }
    }
    val isVerticalPager by remember {
        derivedStateOf {
            windowSizeClass.isHeightAtLeastBreakpoint(WindowSizeClass.HEIGHT_DP_EXPANDED_LOWER_BOUND)
        }
    }
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
                playersPagerState.animateScrollToPage(component.players.lastIndex)
            }
        }

        LaunchedEffect(component.monsters.size) {
            if (component.monsters.isNotEmpty()) {
                monstersPagerState.animateScrollToPage(component.monsters.lastIndex)
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
                AnimatedContent(
                    targetState = showAddPlayerIcon,
                ) { state ->
                    when (state) {
                        true -> {
                            IconButton(
                                onClick = {
                                    component.onAddPlayerClick()
                                },
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
                if (isVerticalPager) {
                    VerticalPager(
                        state = playersPagerState,
                        pageSize = PageSize.Fixed(verticalPlayerPageSizeFixed),
                        pageSpacing = PageSpacingPadding.dp,
                    ) { page ->
                        val player = component.players[page]
                        BattlePlayerCard(
                            player = player,
                            modifier = Modifier.width(horizontalPageSizeFixed),
                            onAvatarClick = {
                                component.onPlayerAvatarClick(player)
                            },
                            onSexChange = {
                                component.onPlayerSexChange(player)
                            },
                            onLevelChange = {
                                component.onPlayerLevelChange(player, it)
                            },
                            onPowerChange = {
                                component.onPlayerPowerChange(player, it)
                            },
                            onModificatorChange = {
                                component.onPlayerModificatorChange(player, it)
                            },
                            onDeleteClick = if (page > 0) {
                                { component.onDeletePlayerClick(player) }
                            } else null
                        )
                    }
                } else {
                    HorizontalPager(
                        state = playersPagerState,
                        contentPadding = PaddingValues(
                            start = windowInsetsPaddings
                                .calculateStartPadding(layoutDirection) + HorizontalPadding.dp,
                            end = windowInsetsPaddings
                                .calculateEndPadding(layoutDirection) + HorizontalPadding.dp,
                        ),
                        pageSize = PageSize.Fixed(horizontalPageSizeFixed),
                        pageSpacing = PageSpacingPadding.dp,
                    ) { page ->
                        val player = component.players[page]
                        BattlePlayerCard(
                            player = player,
                            onAvatarClick = {
                                component.onPlayerAvatarClick(player)
                            },
                            onSexChange = {
                                component.onPlayerSexChange(player)
                            },
                            onLevelChange = {
                                component.onPlayerLevelChange(player, it)
                            },
                            onPowerChange = {
                                component.onPlayerPowerChange(player, it)
                            },
                            onModificatorChange = {
                                component.onPlayerModificatorChange(player, it)
                            },
                            onDeleteClick = if (page > 0) {
                                { component.onDeletePlayerClick(player) }
                            } else null
                        )
                    }
                }
                Row(
                    modifier = Modifier.padding(vertical = 8.dp)
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
                if (isVerticalPager) {
                    VerticalPager(
                        state = monstersPagerState,
                        pageSize = PageSize.Fixed(verticalMonsterPageSizeFixed),
                        pageSpacing = PageSpacingPadding.dp,
                        modifier = Modifier.height(if (component.monsters.size > 1) verticalPlayerPageSizeFixed * 2 + 48.dp else verticalPlayerPageSizeFixed)
                    ) { page ->
                        val monster = component.monsters[page]
                        BattleMonsterCard(
                            name = stringResource(Res.string.monster) + " ${page + 1}",
                            monster = monster,
                            modifier = Modifier.width(horizontalPageSizeFixed),
                            onLevelChange = {
                                component.onMonsterLevelChange(monster, it)
                            },
                            onModificatorChange = {
                                component.onMonsterModificatorChange(monster, it)
                            },
                            onDeleteClick = if (component.monsters.size > 1) {
                                {
                                    component.onDeleteMonsterClick(monster)
                                }
                            } else null,
                            onCloneMonsterClick = {
                                component.onCloneMonster(monster)
                            }
                        )
                    }
                } else {
                    HorizontalPager(
                        state = monstersPagerState,
                        pageSize = PageSize.Fixed(horizontalPageSizeFixed),
                        contentPadding = PaddingValues(
                            start = windowInsetsPaddings
                                .calculateStartPadding(layoutDirection) + HorizontalPadding.dp,
                            end = windowInsetsPaddings
                                .calculateEndPadding(layoutDirection) + HorizontalPadding.dp
                        ),
                        pageSpacing = PageSpacingPadding.dp,
                    ) { page ->
                        val monster = component.monsters[page]
                        BattleMonsterCard(
                            name = stringResource(Res.string.monster) + " ${page + 1}",
                            monster = monster,
                            onLevelChange = {
                                component.onMonsterLevelChange(monster, it)
                            },
                            onModificatorChange = {
                                component.onMonsterModificatorChange(monster, it)
                            },
                            onDeleteClick = if (component.monsters.size > 1) {
                                {
                                    component.onDeleteMonsterClick(monster)
                                }
                            } else null,
                            onCloneMonsterClick = {
                                component.onCloneMonster(monster)
                            }
                        )
                    }
                }
                IconButton(
                    onClick = {
                        component.onAddMonsterClick()
                    },
                ) {
                    Icon(
                        imageVector = Icons.Filled.Add,
                        contentDescription = null,
                    )
                }
            }
            IconButton(
                onClick = component::onBackClick,
                modifier = Modifier
                    .statusBarsPadding()
                    .padding(horizontal = IconHorizontalPadding.dp)
                    .align(Alignment.TopStart)
            ) {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.primary,
                )
            }
            IconButton(
                onClick = component::onDice,
                modifier = Modifier
                    .statusBarsPadding()
                    .padding(horizontal = IconHorizontalPadding.dp)
                    .align(Alignment.TopEnd)
            ) {
                Icon(
                    painter = painterResource(Res.drawable.ic_dice),
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.primary,
                    modifier = Modifier.size(24.dp)
                )
            }
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
fun BattleArrowIndicator(
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

@Preview(showBackground = true, backgroundColor = 0xFFFFFFFF, locale = "en")
@Composable
fun BattleScreenPreview() {
    BattleScreen(
        component = FakeBattleComponent(),
    )
}
