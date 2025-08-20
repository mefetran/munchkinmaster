package org.mefetran.munchkinmaster.ui.screen.avatar

import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.value.MutableValue
import com.arkivanov.decompose.value.Value
import com.arkivanov.decompose.value.update
import org.mefetran.munchkinmaster.model.Avatar

interface AvatarComponent {
    val state: Value<AvatarState>

    fun onAvatarClick(newAvatar: Avatar)
    fun onClose()
}

class DefaultAvatarComponent(
    componentContext: ComponentContext,
    avatar: Avatar,
    private val onAvatarChange: (avatar: Avatar) -> Unit,
    private val onFinished: () -> Unit,
) : AvatarComponent, ComponentContext by componentContext {
    private val _state = MutableValue(AvatarState(avatar))
    override val state: Value<AvatarState> = _state

    override fun onAvatarClick(newAvatar: Avatar) {
        _state.update { AvatarState(newAvatar) }
        onAvatarChange(newAvatar)
    }

    override fun onClose() {
        onAvatarClick(_state.value.selectedAvatar)
        onFinished()
    }
}

class MockAvatarComponent(
    avatar: Avatar,
    private val onFinished: () -> Unit,
) : AvatarComponent {
    private val _state = MutableValue(AvatarState(avatar))
    override val state: Value<AvatarState> = _state

    override fun onAvatarClick(newAvatar: Avatar) {
        _state.update { AvatarState(newAvatar) }
    }

    override fun onClose() {
        onFinished()
    }
}
