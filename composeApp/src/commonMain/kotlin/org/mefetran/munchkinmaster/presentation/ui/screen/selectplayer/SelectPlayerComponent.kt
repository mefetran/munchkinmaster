package org.mefetran.munchkinmaster.presentation.ui.screen.selectplayer

import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.value.MutableValue
import com.arkivanov.decompose.value.Value
import com.arkivanov.decompose.value.update
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject
import org.mefetran.munchkinmaster.data.repository.player.PlayerRepositoryImpl
import org.mefetran.munchkinmaster.data.storage.inmemory.InMemoryPlayerStorage
import org.mefetran.munchkinmaster.domain.model.Player
import org.mefetran.munchkinmaster.domain.usecase.player.GetPlayersUseCase
import org.mefetran.munchkinmaster.presentation.util.coroutineScope
import kotlin.getValue

interface SelectPlayerComponent {
    val playerListState: Value<List<Player>>

    fun onPlayerClick(selectedPlayer: Player)
    fun onClose()
}

class DefaultSelectPlayerComponent(
    componentContext: ComponentContext,
    private val exceptPlayerId: Long? = null,
    private val onFinished: (playerId: Long?) -> Unit,
) : SelectPlayerComponent, ComponentContext by componentContext, KoinComponent {
    private val scope = coroutineScope()
    private val getPlayersUseCase: GetPlayersUseCase by inject()
    private val _playerListState = MutableValue(emptyList<Player>())
    override val playerListState: Value<List<Player>> = _playerListState

    init {
        scope.launch {
            val players = getPlayersUseCase.execute(Unit).first().filter { it.id != exceptPlayerId }
            _playerListState.update { players }
        }
    }

    override fun onPlayerClick(selectedPlayer: Player) {
        onFinished(selectedPlayer.id)
    }

    override fun onClose() {
        onFinished(null)
    }
}

class FakeSelectPlayerComponent(
    private val onFinished: () -> Unit,
) : SelectPlayerComponent {
    private val playerRepository = PlayerRepositoryImpl(InMemoryPlayerStorage())
    private val scope = CoroutineScope(Dispatchers.Default + SupervisorJob())
    private val _playerListState = MutableValue(emptyList<Player>())
    override val playerListState: Value<List<Player>> = _playerListState

    init {
        scope.launch {
            val players = playerRepository.getPlayersAsFlow().first()
            _playerListState.update { players }
        }
    }

    override fun onPlayerClick(selectedPlayer: Player) {
        onFinished()
    }

    override fun onClose() {
        onFinished()
    }
}