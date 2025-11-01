package org.mefetran.munchkinmaster.usecase.player

import kotlinx.coroutines.test.runTest
import org.mefetran.munchkinmaster.FakePlayerRepository
import org.mefetran.munchkinmaster.domain.model.Avatar
import org.mefetran.munchkinmaster.domain.model.Player
import org.mefetran.munchkinmaster.domain.model.Sex
import org.mefetran.munchkinmaster.domain.usecase.player.DeletePlayersByIdsUseCase
import org.mefetran.munchkinmaster.domain.usecase.player.DeletePlayersByIdsUseCaseImpl
import kotlin.test.Test
import kotlin.test.BeforeTest
import kotlin.test.assertEquals

class DeletePlayersByIdsUseCaseTest {
    private lateinit var repository: FakePlayerRepository
    private lateinit var useCase: DeletePlayersByIdsUseCase

    @BeforeTest
    fun before() {
        repository = FakePlayerRepository()
        useCase = DeletePlayersByIdsUseCaseImpl(repository)
    }

    @Test
    fun `Should success delete players by their ids`() = runTest {
        // arrange
        repository.players = buildList {
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
        val size = repository.players.count()
        val playerIds = buildSet {
            add(0L)
            add(1L)
        }
        val params = DeletePlayersByIdsUseCase.Params(playerIds)

        // act
        val result = useCase.execute(params)

        // assert
        assertEquals(true, result)
        assertEquals(size - 2, repository.players.count())
    }
}