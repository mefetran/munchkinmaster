package org.mefetran.munchkinmaster.ui.screen.createplayer

import androidx.compose.ui.text.input.TextFieldValue
import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.value.MutableValue
import com.arkivanov.decompose.value.Value
import com.arkivanov.decompose.value.update
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import munchkinmaster.composeapp.generated.resources.Res
import munchkinmaster.composeapp.generated.resources.error_create_player
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject
import org.mefetran.munchkinmaster.model.Player
import org.mefetran.munchkinmaster.model.Sex
import org.mefetran.munchkinmaster.repository.PlayerRepository
import org.mefetran.munchkinmaster.util.coroutineScope
import kotlin.getValue

interface CreatePlayerComponent {
    val state: Value<CreatePlayerState>
    val nameTextValue: Value<TextFieldValue>

    fun onNameValueChange(newValue: TextFieldValue)
    fun onCreatePlayerClick()
    fun onBackClick()
    fun onSexChange()
    fun hideErrorMessage()
}

class DefaultCreatePlayerComponent(
    componentContext: ComponentContext,
    private val onFinished: () -> Unit
) : CreatePlayerComponent, ComponentContext by componentContext, KoinComponent {
    private val playerRepository: PlayerRepository by inject()
    private val scope = coroutineScope()

    private val _state = MutableValue(CreatePlayerState())
    override val state: Value<CreatePlayerState> = _state
    private val _nameTextValue = MutableValue(TextFieldValue())
    override val nameTextValue: Value<TextFieldValue> = _nameTextValue

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
                name = _nameTextValue.value.text,
                sex = _state.value.sex,
                level = 1,
                power = 0,
                avatar = "",
            )
            val newId = playerRepository.insert(player)
            if (newId == -1L) {
                showErrorMessage()
                return@launch
            }
            withContext(Dispatchers.Main) {
                onFinished()
            }
        }
    }

    override fun onNameValueChange(newValue: TextFieldValue) {
        _nameTextValue.update { newValue }
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
}

class MockCreatePlayerComponent() : CreatePlayerComponent {
    private val _state = MutableValue(CreatePlayerState())
    override val state: Value<CreatePlayerState> = _state

    private val _nameTextValue = MutableValue(TextFieldValue())
    override val nameTextValue: Value<TextFieldValue> = _nameTextValue

    private fun showErrorMessage() {
        _state.update {
            it.copy(
                errorMessageResId = Res.string.error_create_player
            )
        }
    }

    override fun onCreatePlayerClick() {
        showErrorMessage()
    }

    override fun onNameValueChange(newValue: TextFieldValue) {
        _nameTextValue.update { newValue }
    }

    override fun onBackClick() { }

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