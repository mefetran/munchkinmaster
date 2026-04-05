package org.mefetran.munchkinmaster.usecase.player

import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import org.mefetran.munchkinmaster.FakePlayerRepository
import org.mefetran.munchkinmaster.domain.model.Avatar
import org.mefetran.munchkinmaster.domain.model.MaxLevel
import org.mefetran.munchkinmaster.domain.model.PLAYER_MIN_LEVEL
import org.mefetran.munchkinmaster.domain.model.Player
import org.mefetran.munchkinmaster.domain.model.Sex
import org.mefetran.munchkinmaster.domain.usecase.player.UpdatePlayerLevelUseCase
import org.mefetran.munchkinmaster.domain.usecase.player.UpdatePlayerLevelUseCaseImpl
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertEquals

private const val PLAYER_ID = 42L
private const val EMPTY_LEVEL = 0

class UpdatePlayerLevelUseCaseTest {
    private lateinit var repository: FakePlayerRepository
    private lateinit var useCase: UpdatePlayerLevelUseCase
    private val player = Player(
        id = PLAYER_ID,
        name = "Maxim",
        sex = Sex.male,
        level = 9,
        power = 5,
        avatar = Avatar.male3,
    )

    @BeforeTest
    fun before() {
        repository = FakePlayerRepository()
        useCase = UpdatePlayerLevelUseCaseImpl(repository)
    }

    @Test
    fun `Should update the level of a player between 1 and maxLevel`() = runTest {
        // arrange
        repository.createPlayer(player)
        val newLevel = 10
        val maxLevel = MaxLevel.Standard
        val params = UpdatePlayerLevelUseCase.Params(
            player = player,
            newLevel = newLevel,
            maxLevel = maxLevel
        )

        // act
        val result = useCase.execute(params)
        val updatedPlayer = repository.getPlayerById(PLAYER_ID).first()

        assertEquals(true, result)
        assertEquals(10, updatedPlayer?.level ?: EMPTY_LEVEL)
    }

    @Test
    fun `Should update the level of a player by min level`() = runTest {
        // arrange
        repository.createPlayer(player)
        val newLevel = PLAYER_MIN_LEVEL - 1
        val maxLevel = MaxLevel.Standard
        val params = UpdatePlayerLevelUseCase.Params(
            player = player,
            newLevel = newLevel,
            maxLevel = maxLevel
        )

        // act
        val result = useCase.execute(params)
        val updatedPlayer = repository.getPlayerById(PLAYER_ID).first()

        assertEquals(true, result)
        assertEquals(PLAYER_MIN_LEVEL, updatedPlayer?.level ?: EMPTY_LEVEL)
    }
}
