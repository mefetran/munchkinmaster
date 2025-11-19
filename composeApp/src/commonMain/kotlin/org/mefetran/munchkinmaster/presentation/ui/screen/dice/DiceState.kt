package org.mefetran.munchkinmaster.presentation.ui.screen.dice

import org.mefetran.munchkinmaster.domain.model.Dice

data class DiceState(
    val isDicing: Boolean = false,
    val currentDice: Dice = Dice.entries.random(),
    val rollId: Long = 0L,
)
