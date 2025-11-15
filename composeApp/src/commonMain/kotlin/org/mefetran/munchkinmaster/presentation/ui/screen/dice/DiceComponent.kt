package org.mefetran.munchkinmaster.presentation.ui.screen.dice

import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.value.MutableValue
import com.arkivanov.decompose.value.Value
import com.arkivanov.decompose.value.update
import kotlinx.coroutines.launch
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject
import org.mefetran.munchkinmaster.domain.model.Dice
import org.mefetran.munchkinmaster.domain.usecase.dice.GetRandomDiceUseCase
import org.mefetran.munchkinmaster.presentation.util.coroutineScope

interface DiceComponent {
    val state: Value<DiceState>

    fun dice()
    fun dismiss()
}

class DefaultDiceComponent(
    componentContext: ComponentContext,
    private val onDismiss: () -> Unit,
) : DiceComponent, ComponentContext by componentContext, KoinComponent {
    private val getRandomDiceUseCase: GetRandomDiceUseCase by inject()
    private val coroutineScope = coroutineScope()
    private val _state: MutableValue<DiceState> = MutableValue(DiceState())
    override val state: Value<DiceState> = _state

    override fun dice() {
        if (_state.value.isDicing) return

        coroutineScope.launch {
            var dice = getRandomDiceUseCase.execute(Unit)

            while (dice == _state.value.currentDice) {
                dice = getRandomDiceUseCase.execute(Unit)
            }

            _state.update { it.copy(currentDice = dice) }
        }
    }

    override fun dismiss() {
        onDismiss()
    }
}

class FakeDiceComponent : DiceComponent {
    private val _state: MutableValue<DiceState> = MutableValue(DiceState())
    override val state: Value<DiceState> = _state

    override fun dice() {
        _state.update { it.copy(currentDice = Dice.entries.random()) }
    }

    override fun dismiss() {
    }
}
