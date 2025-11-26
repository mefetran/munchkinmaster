package org.mefetran.munchkinmaster.presentation.ui.screen.playerlist

import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.router.slot.ChildSlot
import com.arkivanov.decompose.router.slot.SlotNavigation
import com.arkivanov.decompose.router.slot.activate
import com.arkivanov.decompose.router.slot.childSlot
import com.arkivanov.decompose.router.slot.dismiss
import com.arkivanov.decompose.value.MutableValue
import com.arkivanov.decompose.value.Value
import com.arkivanov.decompose.value.update
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import munchkinmaster.composeapp.generated.resources.Res
import munchkinmaster.composeapp.generated.resources.error_delete_players
import org.jetbrains.compose.resources.StringResource
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject
import org.mefetran.munchkinmaster.data.repository.player.PlayerRepositoryImpl
import org.mefetran.munchkinmaster.data.storage.inmemory.InMemoryPlayerStorage
import org.mefetran.munchkinmaster.domain.model.Player
import org.mefetran.munchkinmaster.domain.usecase.player.DeletePlayersByIdsUseCase
import org.mefetran.munchkinmaster.domain.usecase.player.GetPlayersUseCase
import org.mefetran.munchkinmaster.domain.usecase.player.UpdatePlayerUseCase
import org.mefetran.munchkinmaster.presentation.ui.screen.dice.DefaultDiceComponent
import org.mefetran.munchkinmaster.presentation.ui.screen.dice.DiceComponent
import org.mefetran.munchkinmaster.presentation.util.DiceConfig
import org.mefetran.munchkinmaster.presentation.util.coroutineScope
import kotlin.getValue

interface PlayerListComponent {
    val playerListState: Value<List<Player>>
    val state: Value<PlayerListState>

    val diceSlot: Value<ChildSlot<*, DiceComponent>>

    fun onPlayerClick(playerId: Long)
    fun onAddPlayerClick()
    fun onDeletePlayers()
    fun onAddToDelete(playerId: Long)
    fun hideErrorMessage()
    fun onDeleteModeOn()
    fun onDeleteModeOff()
    fun onSettingsClick()
    fun onResetPlayers()
    fun onDice()
}

class DefaultPlayerListComponent(
    componentContext: ComponentContext,
    private val openPlayer: (playerId: Long) -> Unit,
    private val openCreatePlayer: () -> Unit,
    private val openSettings: () -> Unit,
) : PlayerListComponent, ComponentContext by componentContext, KoinComponent {
    private val scope = coroutineScope()
    private val getPlayersUseCase: GetPlayersUseCase by inject()
    private val deletePlayersByIdsUseCase: DeletePlayersByIdsUseCase by inject()
    private val updatePlayerUseCase: UpdatePlayerUseCase by inject()
    private val diceNavigation = SlotNavigation<DiceConfig>()
    private val _playerListState = MutableValue(emptyList<Player>())
    override val playerListState: Value<List<Player>> = _playerListState

    private val _state = MutableValue<PlayerListState>(PlayerListState.MainState())
    override val state: Value<PlayerListState> = _state

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
            getPlayersUseCase.execute(Unit).collectLatest { players ->
                _playerListState.update { players }
            }
        }
    }

    private fun reduce(transform: (PlayerListState) -> PlayerListState) {
        _state.update(transform)
    }

    private fun showErrorMessage(errorMessageResId: StringResource) {
        _state.update { current ->
            when (current) {
                is PlayerListState.DeleteMode -> current.copy(errorMessageResId = errorMessageResId)
                is PlayerListState.MainState -> current.copy(errorMessageResId = errorMessageResId)
            }
        }
    }

    override fun onPlayerClick(playerId: Long) = openPlayer(playerId)

    override fun onAddPlayerClick() = openCreatePlayer()
    override fun onDeletePlayers() {
        val currentState = _state.value
        if (currentState !is PlayerListState.DeleteMode) return

        scope.launch {
            try {
                val successDeleted =
                    deletePlayersByIdsUseCase.execute(DeletePlayersByIdsUseCase.Params(currentState.playerIdsToDelete))

                if (!successDeleted) {
                    showErrorMessage(Res.string.error_delete_players)
                }

                reduce { PlayerListState.MainState() }
            } catch (e: Exception) {
                e.printStackTrace()
                showErrorMessage(Res.string.error_delete_players)
            }
        }
    }

    override fun onAddToDelete(playerId: Long) {
        val currentState = _state.value
        if (currentState !is PlayerListState.DeleteMode) return

        val newSet = currentState.playerIdsToDelete
        if (newSet.contains(playerId)) {
            newSet.remove(playerId)
        } else {
            newSet.add(playerId)
        }

        reduce {
            if (newSet.isNotEmpty()) {
                it
            } else PlayerListState.MainState()
        }
    }

    override fun hideErrorMessage() {
        _state.update { current ->
            when (current) {
                is PlayerListState.DeleteMode -> current.copy(errorMessageResId = null)
                is PlayerListState.MainState -> current.copy(errorMessageResId = null)
            }
        }
    }

    override fun onDeleteModeOn() {
        reduce { PlayerListState.DeleteMode() }
    }

    override fun onDeleteModeOff() {
        reduce { PlayerListState.MainState() }
    }

    override fun onSettingsClick() {
        openSettings()
    }

    override fun onResetPlayers() {
        scope.launch {
            val players = _playerListState.value

            players.forEach { player ->
                updatePlayerUseCase.execute(
                    UpdatePlayerUseCase.Params(
                        player.copy(
                            level = 1,
                            power = 0,
                        )
                    )
                )
            }
        }
    }

    override fun onDice() {
        diceNavigation.activate(DiceConfig)
    }
}

class FakePlayerListComponent : PlayerListComponent {
    private val playerRepository = PlayerRepositoryImpl(InMemoryPlayerStorage())
    private val scope = CoroutineScope(Dispatchers.Default + SupervisorJob())
    private val _playerListState = MutableValue(emptyList<Player>())
    override val playerListState: Value<List<Player>> = _playerListState

    private val _state = MutableValue<PlayerListState>(PlayerListState.MainState())
    override val state: Value<PlayerListState> = _state
    override val diceSlot: Value<ChildSlot<*, DiceComponent>> = MutableValue(ChildSlot<Any, DiceComponent>())

    override fun onDice() {
    }

    private fun reduce(transform: (PlayerListState) -> PlayerListState) {
        _state.update(transform)
    }

    override fun onDeletePlayers() {
    }

    override fun onAddToDelete(playerId: Long) {
        val currentState = _state.value
        if (currentState !is PlayerListState.DeleteMode) return

        val newSet = currentState.playerIdsToDelete
        if (newSet.contains(playerId)) {
            newSet.remove(playerId)
        } else {
            newSet.add(playerId)
        }

        reduce {
            if (newSet.isNotEmpty()) {
                it
            } else PlayerListState.MainState()
        }
    }

    init {
        scope.launch {
            playerRepository
                .getPlayersAsFlow()
                .collectLatest { players ->
                    _playerListState.update { players }
                }
        }
    }

    override fun onPlayerClick(playerId: Long) {}

    override fun onAddPlayerClick() {}
    override fun hideErrorMessage() {
    }

    override fun onDeleteModeOn() {
        reduce { PlayerListState.DeleteMode() }
    }

    override fun onDeleteModeOff() {
        reduce { PlayerListState.MainState() }
    }

    override fun onSettingsClick() {

    }

    override fun onResetPlayers() {

    }
}
