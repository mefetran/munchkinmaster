package org.mefetran.munchkinmaster.usecase.dice

import kotlinx.coroutines.test.runTest
import org.mefetran.munchkinmaster.domain.model.Dice
import org.mefetran.munchkinmaster.domain.usecase.dice.GetRandomDiceUseCase
import org.mefetran.munchkinmaster.domain.usecase.dice.GetRandomDiceUseCaseImpl
import kotlin.test.Test
import kotlin.test.BeforeTest
import kotlin.test.assertIs

class GetRandomDiceUseCaseTest {
    private lateinit var useCase: GetRandomDiceUseCase

    @BeforeTest
    fun before() {
        useCase = GetRandomDiceUseCaseImpl()
    }

    @Test
    fun `Should return a Dice enum entry`() = runTest {
        // act
        val dice = useCase.execute(Unit)

        // assert
        assertIs<Dice>(dice)
    }
}
