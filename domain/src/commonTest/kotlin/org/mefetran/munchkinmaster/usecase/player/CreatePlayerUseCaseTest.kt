package org.mefetran.munchkinmaster.usecase.player
import kotlinx.coroutines.test.runTest
import org.mefetran.munchkinmaster.FakePlayerRepository
import org.mefetran.munchkinmaster.domain.model.Avatar
import org.mefetran.munchkinmaster.domain.model.Player
import org.mefetran.munchkinmaster.domain.model.Sex
import org.mefetran.munchkinmaster.domain.usecase.player.CreatePlayerUseCase
import org.mefetran.munchkinmaster.domain.usecase.player.CreatePlayerUseCaseImpl
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertEquals

class CreatePlayerUseCaseTest {
    private lateinit var repository: FakePlayerRepository
    private lateinit var useCase: CreatePlayerUseCase


    @BeforeTest
    fun before() {
        repository = FakePlayerRepository()
        useCase = CreatePlayerUseCaseImpl(repository)
    }

    @Test
    fun `Should success create player`() = runTest {
        // arrange
        val player = Player(
            id = 42,
            name = "Artem",
            sex = Sex.male,
            level = 4,
            power = 4,
            avatar = Avatar.male2,
        )

        // act
        val result = useCase.execute(CreatePlayerUseCase.Params(player))

        // assert
        assertEquals(true, result)
    }
}