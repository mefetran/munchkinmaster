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
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject
import org.mefetran.munchkinmaster.model.Player
import org.mefetran.munchkinmaster.repository.MockPlayerRepository
import org.mefetran.munchkinmaster.repository.PlayerRepository
import org.mefetran.munchkinmaster.util.coroutineScope
import kotlin.getValue

interface PlayerListComponent {
    val playerListState: Value<List<Player>>

    fun onPlayerClick(playerId: Long)
    fun onAddPlayerClick()
}

class DefaultPlayerListComponent(
    componentContext: ComponentContext,
    private val openPlayer: (playerId: Long) -> Unit,
) : PlayerListComponent, ComponentContext by componentContext, KoinComponent {
    private val playerRepository: PlayerRepository by inject()
    private val scope = coroutineScope()
    private val _playerListState = MutableValue(emptyList<Player>())
    override val playerListState: Value<List<Player>> = _playerListState

    init {
        scope.launch {
            playerRepository
                .getPlayersAsFlow()
                .collectLatest { players ->
                    _playerListState.update { players }
                }
        }
    }

    override fun onPlayerClick(playerId: Long) = openPlayer(playerId)

    override fun onAddPlayerClick() {
        TODO("Not yet implemented")
    }
}

class MockPlayerListComponent : PlayerListComponent {
    private val playerRepository = MockPlayerRepository()
    private val scope = CoroutineScope(Dispatchers.Default + SupervisorJob())
    private val _playerListState = MutableValue(emptyList<Player>())
    override val playerListState: Value<List<Player>> = _playerListState

    init {
        scope.launch {
            playerRepository
                .getPlayersAsFlow()
                .collectLatest { players ->
                    _playerListState.update { players }
                }
        }
    }

    override fun onPlayerClick(playerId: Long) { }

    override fun onAddPlayerClick() { }
}