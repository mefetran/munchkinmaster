package org.mefetran.munchkinmaster.ui.screen.playerlist

import com.arkivanov.decompose.ComponentContext
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
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject
import org.mefetran.munchkinmaster.model.Player
import org.mefetran.munchkinmaster.repository.MockPlayerRepository
import org.mefetran.munchkinmaster.repository.PlayerRepository
import org.mefetran.munchkinmaster.util.coroutineScope
import kotlin.getValue

interface PlayerListComponent {
    val playerListState: Value<List<Player>>
    val state: Value<PlayerListState>

    fun onPlayerClick(playerId: Long)
    fun onAddPlayerClick()
    fun onDeletePlayers()
    fun onAddToDelete(playerId: Long)
    fun hideErrorMessage()
    fun onDeleteModeOn()
    fun onDeleteModeOff()
}

class DefaultPlayerListComponent(
    componentContext: ComponentContext,
    private val openPlayer: (playerId: Long) -> Unit,
    private val openCreatePlayer: () -> Unit,
) : PlayerListComponent, ComponentContext by componentContext, KoinComponent {
    private val playerRepository: PlayerRepository by inject()
    private val scope = coroutineScope()
    private val _playerListState = MutableValue(emptyList<Player>())
    override val playerListState: Value<List<Player>> = _playerListState

    private val _state = MutableValue(PlayerListState())
    override val state: Value<PlayerListState> = _state

    init {
        scope.launch {
            playerRepository
                .getPlayersAsFlow()
                .collectLatest { players ->
                    _playerListState.update { players }
                }
        }
    }

    private fun showErrorMessage() {
        _state.update {
            it.copy(
                errorMessageResId = Res.string.error_delete_players
            )
        }
    }

    override fun onPlayerClick(playerId: Long) = openPlayer(playerId)

    override fun onAddPlayerClick() = openCreatePlayer()
    override fun onDeletePlayers() {
        scope.launch {
            try {
                val deletedCount =
                    playerRepository.deletePlayersByIds(_state.value.playerIdsToDelete)
                if (deletedCount != _state.value.playerIdsToDelete.size) {
                    showErrorMessage()
                }
                _state.update {
                    it.copy(
                        isDeleteMode = false,
                        playerIdsToDelete = emptySet()
                    )
                }
            } catch (e: Exception) {
                e.printStackTrace()
                showErrorMessage()
            }
        }
    }

    override fun onAddToDelete(playerId: Long) {
        val newSet = _state.value.playerIdsToDelete.toMutableSet()
        if (newSet.contains(playerId)) {
            newSet.remove(playerId)
        } else {
            newSet.add(playerId)
        }

        _state.update {
            it.copy(
                isDeleteMode = newSet.isNotEmpty(),
                playerIdsToDelete = newSet
            )
        }
    }

    override fun hideErrorMessage() {
        _state.update {
            it.copy(
                errorMessageResId = null
            )
        }
    }

    override fun onDeleteModeOn() {
        _state.update { it.copy(isDeleteMode = true, playerIdsToDelete = emptySet()) }
    }

    override fun onDeleteModeOff() {
        _state.update { it.copy(isDeleteMode = false, playerIdsToDelete = emptySet()) }
    }
}

class MockPlayerListComponent : PlayerListComponent {
    private val playerRepository = MockPlayerRepository()
    private val scope = CoroutineScope(Dispatchers.Default + SupervisorJob())
    private val _playerListState = MutableValue(emptyList<Player>())
    override val playerListState: Value<List<Player>> = _playerListState

    private val _state = MutableValue(PlayerListState())
    override val state: Value<PlayerListState> = _state

    override fun onDeletePlayers() {
        TODO("Not yet implemented")
    }

    override fun onAddToDelete(playerId: Long) {
        val newSet = _state.value.playerIdsToDelete.toMutableSet()
        if (newSet.contains(playerId)) {
            newSet.remove(playerId)
        } else {
            newSet.add(playerId)
        }

        _state.update {
            it.copy(
                isDeleteMode = newSet.isNotEmpty(),
                playerIdsToDelete = newSet
            )
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
        TODO("Not yet implemented")
    }

    override fun onDeleteModeOn() {
        _state.update { it.copy(isDeleteMode = true, playerIdsToDelete = emptySet()) }
    }

    override fun onDeleteModeOff() {
        _state.update { it.copy(isDeleteMode = false, playerIdsToDelete = emptySet()) }
    }
}