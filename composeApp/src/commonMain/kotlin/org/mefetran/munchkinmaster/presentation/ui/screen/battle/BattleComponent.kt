package org.mefetran.munchkinmaster.presentation.ui.screen.battle

import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.runtime.snapshots.SnapshotStateMap
import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.router.slot.ChildSlot
import com.arkivanov.decompose.router.slot.SlotNavigation
import com.arkivanov.decompose.router.slot.activate
import com.arkivanov.decompose.router.slot.childSlot
import com.arkivanov.decompose.router.slot.dismiss
import com.arkivanov.decompose.value.MutableValue
import com.arkivanov.decompose.value.Value
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject
import org.mefetran.munchkinmaster.domain.model.Avatar
import org.mefetran.munchkinmaster.domain.model.Monster
import org.mefetran.munchkinmaster.domain.model.Player
import org.mefetran.munchkinmaster.domain.model.Sex
import org.mefetran.munchkinmaster.domain.usecase.player.GetPlayerByIdUseCase
import org.mefetran.munchkinmaster.domain.usecase.player.UpdatePlayerUseCase
import org.mefetran.munchkinmaster.presentation.ui.screen.avatar.AvatarComponent
import org.mefetran.munchkinmaster.presentation.ui.screen.avatar.DefaultAvatarComponent
import org.mefetran.munchkinmaster.presentation.ui.screen.dice.DefaultDiceComponent
import org.mefetran.munchkinmaster.presentation.ui.screen.dice.DiceComponent
import org.mefetran.munchkinmaster.presentation.ui.screen.selectplayer.DefaultSelectPlayerComponent
import org.mefetran.munchkinmaster.presentation.ui.screen.selectplayer.SelectPlayerComponent
import org.mefetran.munchkinmaster.presentation.util.AvatarConfig
import org.mefetran.munchkinmaster.presentation.util.DiceConfig
import org.mefetran.munchkinmaster.presentation.util.SelectPlayerConfig
import org.mefetran.munchkinmaster.presentation.util.coroutineScope

interface BattleComponent {
    val players: SnapshotStateList<Player>
    val monsters: SnapshotStateList<Monster>
    val modificators: SnapshotStateMap<Long, Int>

    val selectAvatarSlot: Value<ChildSlot<*, AvatarComponent>>
    val selectPlayerSlot: Value<ChildSlot<*, SelectPlayerComponent>>

    val diceSlot: Value<ChildSlot<*, DiceComponent>>

    fun onBackClick()
    fun onLevelChange(player: Player, newValue: Int)
    fun onPowerChange(player: Player, newValue: Int)
    fun onModificatorChange(player: Player, newValue: Int)
    fun onSexChange(player: Player)
    fun onDeletePlayerClick(player: Player)
    fun onDeleteMonsterClick(monster: Monster)
    fun onAvatarClick(player: Player)
    fun onAddPlayerClick()
    fun onAddMonsterClick()
    fun onMonsterLevelChange(monster: Monster, newValue: Int)
    fun onMonsterModificatorChange(monster: Monster, newValue: Int)
    fun onCloneMonster(monster: Monster)
    fun onDice()
}

class DefaultBattleComponent(
    componentContext: ComponentContext,
    private val mainPlayerId: Long,
    private val onFinished: () -> Unit,
) : BattleComponent, ComponentContext by componentContext, KoinComponent {

    private val scope = coroutineScope()
    private var jobHelperPlayer: Job? = null
    private val getPlayerByIdUseCase: GetPlayerByIdUseCase by inject()
    private val updatePlayerUseCase: UpdatePlayerUseCase by inject()
    private val avatarNavigation = SlotNavigation<AvatarConfig>()
    private val selectPlayerNavigation = SlotNavigation<SelectPlayerConfig>()
    private val diceNavigation = SlotNavigation<DiceConfig>()

    override val players: SnapshotStateList<Player> = SnapshotStateList()
    override val monsters: SnapshotStateList<Monster> = SnapshotStateList<Monster>().apply {
        add(
            Monster(
                id = 1,
                level = 1,
                modificator = 0
            )
        )
    }
    override val modificators: SnapshotStateMap<Long, Int> = SnapshotStateMap()
    override val selectAvatarSlot: Value<ChildSlot<*, AvatarComponent>> = childSlot(
        source = avatarNavigation,
        serializer = AvatarConfig.serializer(),
        key = "SelectAvatarSlot",
        handleBackButton = true,
    ) { config, componentContext ->
        DefaultAvatarComponent(
            componentContext = componentContext,
            avatar = config.currentAvatar,
            playerId = config.playerId,
            onAvatarChange = { newAvatar, playerId ->
                playerId?.let {
                    onAvatarChange(newAvatar, playerId)
                }
            },
            onFinished = avatarNavigation::dismiss
        )
    }
    override val selectPlayerSlot: Value<ChildSlot<*, SelectPlayerComponent>> = childSlot(
        source = selectPlayerNavigation,
        serializer = SelectPlayerConfig.serializer(),
        key = "SelectPlayerSlot",
        handleBackButton = true,
    ) { _, componentContext ->
        DefaultSelectPlayerComponent(
            componentContext = componentContext,
            exceptPlayerId = mainPlayerId,
            onFinished = { selectedPlayerId ->
                selectedPlayerId?.let {
                    getHelperPlayer(selectedPlayerId)
                }
                selectPlayerNavigation.dismiss()
            }
        )
    }
    override val diceSlot: Value<ChildSlot<*, DiceComponent>> = childSlot(
        source = diceNavigation,
        serializer = DiceConfig.serializer(),
        key = "DiceSlot",
        handleBackButton = true,
    ) { _, componentContext ->
        DefaultDiceComponent(
            componentContext = componentContext,
            onDismiss = diceNavigation::dismiss
        )
    }

    init {
        scope.launch {
            getPlayerByIdUseCase
                .execute(GetPlayerByIdUseCase.Params(mainPlayerId))
                .collectLatest { player ->
                    player?.let {
                        val index = players.indexOfFirst { it.id == mainPlayerId }
                        val modificators = modificators[player.id]
                        if (index != -1) {
                            players[index] = player.copy(modificator = modificators ?: 0)
                        } else {
                            players.add(player)
                        }
                    }
                }
        }
    }

    override fun onBackClick() = onFinished()

    override fun onDeleteMonsterClick(monster: Monster) {
        monsters.remove(monster)
    }

    override fun onAvatarClick(player: Player) {
        avatarNavigation.activate(AvatarConfig(player.avatar, player.id))
    }

    override fun onAddPlayerClick() {
        selectPlayerNavigation.activate(SelectPlayerConfig)
    }

    override fun onAddMonsterClick() {
        monsters.add(
            Monster(
                id = monsters.lastIndex + 2,
                level = 1,
                modificator = 0,
            )
        )
    }

    override fun onLevelChange(player: Player, newValue: Int) {
        scope.launch {
            player
                .copy(level = newValue)
                .let { updated ->
                    updatePlayerUseCase.execute(UpdatePlayerUseCase.Params(updated))
                }
        }
    }

    override fun onPowerChange(player: Player, newValue: Int) {
        scope.launch {
            player
                .copy(power = newValue)
                .let { updated ->
                    updatePlayerUseCase.execute(UpdatePlayerUseCase.Params(updated))
                }
        }
    }

    override fun onModificatorChange(player: Player, newValue: Int) {
        scope.launch {
            val index = players.indexOfFirst { it.id == player.id }
            if (index == -1) return@launch

            modificators[player.id] = newValue
            players[index] = player.copy(modificator = newValue)
        }
    }

    override fun onSexChange(player: Player) {
        scope.launch {
            player
                .let { _ ->
                    val updated = player.copy(
                        sex = when (player.sex) {
                            Sex.male -> Sex.female
                            Sex.female -> Sex.male
                        }
                    )
                    updatePlayerUseCase.execute(UpdatePlayerUseCase.Params(updated))
                }
        }
    }

    override fun onDeletePlayerClick(player: Player) {
        jobHelperPlayer?.cancel()
        players.remove(player)
    }

    override fun onMonsterLevelChange(
        monster: Monster,
        newValue: Int
    ) {
        val index = monsters.indexOfFirst { it.id == monster.id }
        if (index != -1) {
            monsters[index] = monster.copy(level = newValue)
        }
    }

    override fun onMonsterModificatorChange(
        monster: Monster,
        newValue: Int
    ) {
        val index = monsters.indexOfFirst { it.id == monster.id }
        if (index != -1) {
            monsters[index] = monster.copy(modificator = newValue)
        }
    }

    override fun onCloneMonster(monster: Monster) {
        monsters.add(monster.copy(id = monsters.lastIndex + 2))
    }

    private fun onAvatarChange(newAvatar: Avatar, playerId: Long) {
        scope.launch {
            val player = players.firstOrNull { player -> player.id == playerId }
            player
                ?.copy(avatar = newAvatar)
                ?.let { updated ->
                    updatePlayerUseCase.execute(UpdatePlayerUseCase.Params(updated))
                }
        }
    }

    private fun getHelperPlayer(playerId: Long) {
        jobHelperPlayer = scope.launch {
            getPlayerByIdUseCase
                .execute(GetPlayerByIdUseCase.Params(playerId))
                .collectLatest { player ->
                    player?.let {
                        val index = players.indexOfFirst { it.id == playerId }
                        val modificators = modificators[player.id]
                        if (index != -1) {
                            players[index] = player.copy(modificator = modificators ?: 0)
                        } else {
                            players.add(player)
                        }
                    }
                }
        }
    }

    override fun onDice() {
        diceNavigation.activate(DiceConfig)
    }
}

class FakeBattleComponent : BattleComponent {
    override val players: SnapshotStateList<Player> = SnapshotStateList<Player>().apply {
        add(
            Player(
                id = 1,
                name = "Denis",
                sex = Sex.male,
                level = 1,
                power = 1,
                avatar = Avatar.male2,
            )
        )
        add(
            Player(
                id = 2,
                name = "Lida",
                sex = Sex.female,
                level = 2,
                power = 2,
                avatar = Avatar.female2,
            )
        )
    }

    override fun onDice() {

    }

    override val monsters: SnapshotStateList<Monster> = SnapshotStateList<Monster>().apply {
        add(
            Monster(
                id = 1,
                level = 1,
                modificator = 0
            )
        )
    }
    override val modificators: SnapshotStateMap<Long, Int> = SnapshotStateMap()
    override val selectAvatarSlot: Value<ChildSlot<*, AvatarComponent>> = MutableValue(
        ChildSlot<Any, AvatarComponent>(null)
    )
    override val selectPlayerSlot: Value<ChildSlot<*, SelectPlayerComponent>> = MutableValue(
        ChildSlot<Any, SelectPlayerComponent>(null)
    )

    override fun onBackClick() {

    }

    override fun onLevelChange(
        player: Player,
        newValue: Int
    ) {

    }

    override fun onPowerChange(
        player: Player,
        newValue: Int
    ) {

    }

    override fun onModificatorChange(
        player: Player,
        newValue: Int
    ) {

    }

    override fun onSexChange(player: Player) {

    }

    override fun onDeletePlayerClick(player: Player) {

    }

    override fun onDeleteMonsterClick(monster: Monster) {
        monsters.remove(monster)
    }

    override val diceSlot: Value<ChildSlot<*, DiceComponent>> = MutableValue(ChildSlot<Any, DiceComponent>())

    override fun onAvatarClick(player: Player) {

    }

    override fun onAddPlayerClick() {

    }

    override fun onAddMonsterClick() {
        monsters.add(
            Monster(
                id = monsters.lastIndex + 2,
                level = 1,
                modificator = 0,
            )
        )
    }

    override fun onMonsterLevelChange(
        monster: Monster,
        newValue: Int
    ) {

    }

    override fun onMonsterModificatorChange(
        monster: Monster,
        newValue: Int
    ) {

    }

    override fun onCloneMonster(monster: Monster) {
        monsters.add(monster.copy(id = monsters.lastIndex + 2))
    }
}
