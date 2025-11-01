package org.mefetran.munchkinmaster.usecase.player

import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.test.runTest
import org.mefetran.munchkinmaster.FakePlayerRepository
import org.mefetran.munchkinmaster.domain.model.Avatar
import org.mefetran.munchkinmaster.domain.model.Player
import org.mefetran.munchkinmaster.domain.model.Sex
import org.mefetran.munchkinmaster.domain.usecase.player.GetPlayersUseCase
import org.mefetran.munchkinmaster.domain.usecase.player.GetPlayersUseCaseImpl
import kotlin.test.Test
import kotlin.test.BeforeTest
import kotlin.test.assertEquals
import kotlin.test.assertNotNull

class GetPlayersUseCaseTest {
    private lateinit var repository: FakePlayerRepository
    private lateinit var useCase: GetPlayersUseCase
    private val players = buildList {
        add(
            Player(
                id = 0,
                name = "Denis",
                sex = Sex.male,
                level = 5,
                power = 6,
                avatar = Avatar.male1
            )
        )
        add(
            Player(
                id = 1,
                name = "Lida",
                sex = Sex.female,
                level = 6,
                power = 5,
                avatar = Avatar.female1
            )
        )
        add(
            Player(
                id = 2,
                name = "Kate",
                sex = Sex.female,
                level = 1,
                power = 0,
                avatar = Avatar.female4
            )
        )
    }

    @BeforeTest
    fun before() {
        repository = FakePlayerRepository()
        useCase = GetPlayersUseCaseImpl(repository)
    }

    @Test
    fun `Should return a list of players`() = runTest {
        // arrange
        repository.players = players

        // act

        val result = useCase.execute(Unit).firstOrNull()

        assertNotNull(result)
        assertEquals(true, result.isNotEmpty())
    }
}