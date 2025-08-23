package org.mefetran.munchkinmaster.ui.screen.player

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
import kotlinx.serialization.Serializable
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject
import org.mefetran.munchkinmaster.model.Avatar
import org.mefetran.munchkinmaster.model.Sex
import org.mefetran.munchkinmaster.repository.FakePlayerRepository
import org.mefetran.munchkinmaster.repository.PlayerRepository
import org.mefetran.munchkinmaster.ui.screen.avatar.AvatarComponent
import org.mefetran.munchkinmaster.ui.screen.avatar.DefaultAvatarComponent
import org.mefetran.munchkinmaster.util.coroutineScope

interface PlayerComponent {
    val state: Value<PlayerUiState>
    val selectAvatarSlot: Value<ChildSlot<*, AvatarComponent>>

    fun onBackClick()
    fun onLevelChange(newValue: Int)
    fun onPowerChange(newValue: Int)
    fun onSexChange()
    fun onAvatarClick()
}

class DefaultPlayerComponent(
    componentContext: ComponentContext,
    private val playerId: Long,
    private val onFinished: () -> Unit,
) : PlayerComponent, ComponentContext by componentContext, KoinComponent {
    private val playerRepository: PlayerRepository by inject()
    private val scope = coroutineScope()
    private val avatarNavigation = SlotNavigation<AvatarConfig>()
    private val _state = MutableValue(PlayerUiState())
    override val state: Value<PlayerUiState> = _state
    override val selectAvatarSlot: Value<ChildSlot<*, AvatarComponent>> = childSlot(
        source = avatarNavigation,
        serializer = AvatarConfig.serializer(),
        handleBackButton = true,
    ) { config, componentContext ->
        DefaultAvatarComponent(
            componentContext = componentContext,
            avatar = config.currentAvatar,
            onAvatarChange = { newAvatar ->
                onAvatarChange(newAvatar)
            },
            onFinished = avatarNavigation::dismiss
        )
    }

    init {
        scope.launch {
            playerRepository
                .getPlayerById(playerId)
                .collectLatest { player ->
                    _state.update { it.copy(player = player) }
                }
        }
    }

    private fun onAvatarChange(newAvatar: Avatar) {
        scope.launch {
            _state.value.player
                ?.copy(avatar = newAvatar)
                ?.let { updated ->
                    playerRepository.updatePlayer(updated)
                }
        }
    }

    override fun onAvatarClick() {
        avatarNavigation.activate(AvatarConfig(_state.value.player?.avatar ?: Avatar.female2))
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

    @Serializable
    private data class AvatarConfig(
        val currentAvatar: Avatar
    )
}

class FakePlayerComponent(
    private val playerId: Long,
    private val onFinished: () -> Unit,
) : PlayerComponent {
    private val playerRepository = FakePlayerRepository()
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

    override val selectAvatarSlot: Value<ChildSlot<*, AvatarComponent>> = MutableValue(
        ChildSlot<Any, AvatarComponent>(null)
    )

    override fun onAvatarClick() {}

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