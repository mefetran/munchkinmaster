package org.mefetran.munchkinmaster.usecase.player

import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.test.runTest
import org.mefetran.munchkinmaster.FakePlayerRepository
import org.mefetran.munchkinmaster.domain.model.Avatar
import org.mefetran.munchkinmaster.domain.model.Player
import org.mefetran.munchkinmaster.domain.model.Sex
import org.mefetran.munchkinmaster.domain.usecase.player.GetPlayerByIdUseCase
import org.mefetran.munchkinmaster.domain.usecase.player.GetPlayerByIdUseCaseImpl
import kotlin.test.Test
import kotlin.test.BeforeTest
import kotlin.test.assertNotNull
import kotlin.test.assertNull

class GetPlayerByIdUseCaseTest {
    private lateinit var repository: FakePlayerRepository
    private lateinit var useCase: GetPlayerByIdUseCase

    @BeforeTest
    fun before() {
        repository = FakePlayerRepository()
        useCase = GetPlayerByIdUseCaseImpl(repository)
    }

    @Test
    fun `Should return a player by its ID`() = runTest {
        // arrange
        val player = Player(
            id = 42,
            name = "Denis",
            sex = Sex.male,
            level = 10,
            power = 100,
            avatar = Avatar.male3
        )
        repository.createPlayer(player)
        val params = GetPlayerByIdUseCase.Params(player.id)

        // act
        val gotPlayer = useCase.execute(params).firstOrNull()

        // assert
        assertNotNull(gotPlayer)
    }

    @Test
    fun `Should return null if player doesn't exist`() = runTest {
        // arrange
        val params = GetPlayerByIdUseCase.Params(0L)

        // act
        val gotPlayer = useCase.execute(params).firstOrNull()

        // assert
        assertNull(gotPlayer)
    }
}