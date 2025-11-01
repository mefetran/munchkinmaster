package org.mefetran.munchkinmaster.usecase.player

import kotlinx.coroutines.test.runTest
import org.mefetran.munchkinmaster.FakePlayerRepository
import org.mefetran.munchkinmaster.domain.model.Avatar
import org.mefetran.munchkinmaster.domain.model.Player
import org.mefetran.munchkinmaster.domain.model.Sex
import org.mefetran.munchkinmaster.domain.usecase.player.UpdatePlayerUseCase
import org.mefetran.munchkinmaster.domain.usecase.player.UpdatePlayerUseCaseImpl
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertEquals

class UpdatePlayerUseCaseTest {
    private lateinit var repository: FakePlayerRepository
    private lateinit var useCase: UpdatePlayerUseCase

    @BeforeTest
    fun before() {
        repository = FakePlayerRepository()
        useCase = UpdatePlayerUseCaseImpl(repository)
    }

    @Test
    fun `Should success update existing player using its ID`() = runTest {
        // arrange
        val player = Player(
            id = 42,
            name = "Artem",
            sex = Sex.male,
            level = 4,
            power = 4,
            avatar = Avatar.male2,
        )
        repository.createPlayer(player)
        val updatedPlayer = player.copy(sex = Sex.female)
        val params = UpdatePlayerUseCase.Params(updatedPlayer)

        // act
        val result = useCase.execute(params)

        // assert
        assertEquals(true, result)
    }

    @Test
    fun `Should failure to update not existing player using its ID`() = runTest {
        // arrange
        val player = Player(
            id = 42,
            name = "Artem",
            sex = Sex.male,
            level = 4,
            power = 4,
            avatar = Avatar.male2,
        )
        val params = UpdatePlayerUseCase.Params(player)

        // act
        val result = useCase.execute(params)

        // assert
        assertEquals(false, result)
    }
}