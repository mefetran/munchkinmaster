package org.mefetran.munchkinmaster.ui.screen.player

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
import org.mefetran.munchkinmaster.model.Sex
import org.mefetran.munchkinmaster.repository.MockPlayerRepository
import org.mefetran.munchkinmaster.repository.PlayerRepository
import org.mefetran.munchkinmaster.util.coroutineScope

interface PlayerComponent {
    val state: Value<PlayerUiState>

    fun onBackClick()
    fun onLevelChange(newValue: Int)
    fun onPowerChange(newValue: Int)
    fun onSexChange()
}

class DefaultPlayerComponent(
    componentContext: ComponentContext,
    private val playerId: Long,
    private val onFinished: () -> Unit,
) : PlayerComponent, ComponentContext by componentContext, KoinComponent {
    private val playerRepository: PlayerRepository by inject()
    private val scope = coroutineScope()
    private val _state = MutableValue(PlayerUiState())
    override val state: Value<PlayerUiState> = _state

    init {
        scope.launch {
            playerRepository
                .getPlayerById(playerId)
                .collectLatest { player ->
                    _state.update { it.copy(player = player) }
                }
        }
    }

    override fun onLevelChange(newValue: Int) {
        scope.launch {
            _state.value.player
                ?.copy(level = newValue.coerceIn(1, 10))
                ?.let { updated ->
                    playerRepository.updatePlayer(updated)
                }
        }
    }

    override fun onPowerChange(newValue: Int) {
        scope.launch {
            _state.value.player
                ?.copy(power = newValue)
                ?.let { updated ->
                    playerRepository.updatePlayer(updated)
                }
        }
    }

    override fun onBackClick() = onFinished()

    override fun onSexChange() {
        scope.launch {
            _state.value.player?.let { player ->
                val updated = player.copy(
                    sex = when (player.sex) {
                        Sex.male -> Sex.female
                        Sex.female -> Sex.male
                    }
                )
                playerRepository.updatePlayer(updated)
            }
        }
    }
}

class MockPlayerComponent(
    private val playerId: Long,
    private val onFinished: () -> Unit,
) : PlayerComponent {
    private val playerRepository = MockPlayerRepository()
    private val scope = CoroutineScope(Dispatchers.Default + SupervisorJob())
    private val _state = MutableValue(PlayerUiState())
    override val state: Value<PlayerUiState> = _state

    init {
        scope.launch {
            playerRepository
                .getPlayerById(playerId)
                .collectLatest { player ->
                    _state.update { it.copy(player = player) }
                }
        }
    }

    override fun onLevelChange(newValue: Int) {
        scope.launch {
            _state.value.player
                ?.copy(level = newValue.coerceIn(1, 10))
                ?.let { updated ->
                    playerRepository.updatePlayer(updated)
                }
        }
    }

    override fun onPowerChange(newValue: Int) {
        scope.launch {
            _state.value.player
                ?.copy(power = newValue)
                ?.let { updated ->
                    playerRepository.updatePlayer(updated)
                }
        }
    }

    override fun onBackClick() = onFinished()

    override fun onSexChange() {
        scope.launch {
            _state.value.player?.let { player ->
                val updated = player.copy(
                    sex = when (player.sex) {
                        Sex.male -> Sex.female
                        Sex.female -> Sex.male
                    }
                )
                playerRepository.updatePlayer(updated)
            }
        }
    }
}