package org.mefetran.munchkinmaster.domain.usecase.dice

import org.mefetran.munchkinmaster.domain.model.Dice
import org.mefetran.munchkinmaster.domain.usecase.UseCase

interface GetRandomDiceUseCase : UseCase<Unit, Dice>

class GetRandomDiceUseCaseImpl : GetRandomDiceUseCase {
    override suspend fun execute(input: Unit): Dice {
        val randomNumber = (0 until 6).random()
        val dice = Dice.entries[randomNumber]

        return dice
    }
}
