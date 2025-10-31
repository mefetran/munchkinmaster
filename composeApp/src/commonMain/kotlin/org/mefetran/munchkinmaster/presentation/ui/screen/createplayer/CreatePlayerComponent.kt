package org.mefetran.munchkinmaster.presentation.ui.screen.createplayer

import androidx.compose.foundation.text.input.TextFieldState
import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.router.slot.ChildSlot
import com.arkivanov.decompose.router.slot.SlotNavigation
import com.arkivanov.decompose.router.slot.activate
import com.arkivanov.decompose.router.slot.childSlot
import com.arkivanov.decompose.router.slot.dismiss
import com.arkivanov.decompose.value.MutableValue
import com.arkivanov.decompose.value.Value
import com.arkivanov.decompose.value.update
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import kotlinx.serialization.Serializable
import munchkinmaster.composeapp.generated.resources.Res
import munchkinmaster.composeapp.generated.resources.error_create_player
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject
import org.mefetran.munchkinmaster.domain.model.Avatar
import org.mefetran.munchkinmaster.domain.model.Player
import org.mefetran.munchkinmaster.domain.model.Sex
import org.mefetran.munchkinmaster.domain.repository.PlayerRepository
import org.mefetran.munchkinmaster.presentation.ui.screen.avatar.AvatarComponent
import org.mefetran.munchkinmaster.presentation.ui.screen.avatar.DefaultAvatarComponent
import org.mefetran.munchkinmaster.presentation.util.coroutineScope

interface CreatePlayerComponent {
    val state: Value<CreatePlayerState>
    val nameState: TextFieldState
    val selectAvatarSlot: Value<ChildSlot<*, AvatarComponent>>

    fun onCreatePlayerClick()
    fun onBackClick()
    fun onSexChange()
    fun hideErrorMessage()
    fun onAvatarClick()
}

class DefaultCreatePlayerComponent(
    componentContext: ComponentContext,
    private val onFinished: () -> Unit
) : CreatePlayerComponent, ComponentContext by componentContext, KoinComponent {
    private val playerRepository: PlayerRepository by inject()
    private val scope = coroutineScope()
    private val avatarNavigation = SlotNavigation<AvatarConfig>()

    private val _state = MutableValue(CreatePlayerState())
    override val state: Value<CreatePlayerState> = _state

    override val nameState: TextFieldState = TextFieldState()

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

    private fun showErrorMessage() {
        _state.update {
            it.copy(
                errorMessageResId = Res.string.error_create_player
            )
        }
    }

    override fun onCreatePlayerClick() {
        scope.launch {
            val player = Player(
                name = nameState.text.trim().toString(),
                sex = _state.value.sex,
                level = 1,
                power = 0,
                avatar = _state.value.selectedAvatar,
            )
            val successSaved = playerRepository.savePlayer(player)
            if (!successSaved) {
                showErrorMessage()
                return@launch
            }
            withContext(Dispatchers.Main) {
                onFinished()
            }
        }
    }


    private fun onAvatarChange(newAvatar: Avatar) {
        _state.update {
            it.copy(
                selectedAvatar = newAvatar
            )
        }
    }

    override fun onAvatarClick() {
        avatarNavigation.activate(AvatarConfig(_state.value.selectedAvatar))
    }

    override fun onBackClick() = onFinished()

    override fun hideErrorMessage() {
        _state.update {
            it.copy(
                errorMessageResId = null
            )
        }
    }

    override fun onSexChange() {
        _state.update {
            it.copy(
                sex = if (it.sex == Sex.male) Sex.female else Sex.male
            )
        }
    }

    @Serializable
    private data class AvatarConfig(
        val currentAvatar: Avatar
    )
}

class FakeCreatePlayerComponent() : CreatePlayerComponent {
    private val _state = MutableValue(CreatePlayerState())
    override val state: Value<CreatePlayerState> = _state

    override val nameState: TextFieldState = TextFieldState()

    private fun showErrorMessage() {
        _state.update {
            it.copy(
                errorMessageResId = Res.string.error_create_player
            )
        }
    }

    override val selectAvatarSlot: Value<ChildSlot<*, AvatarComponent>> = MutableValue(
        ChildSlot<Any, AvatarComponent>(null)
    )

    override fun onAvatarClick() { }

    override fun onCreatePlayerClick() {
        showErrorMessage()
    }

    override fun onBackClick() {}

    override fun hideErrorMessage() {
        _state.update {
            it.copy(
                errorMessageResId = null
            )
        }
    }

    override fun onSexChange() {
        _state.update {
            it.copy(
                sex = if (it.sex == Sex.male) Sex.female else Sex.male
            )
        }
    }
}