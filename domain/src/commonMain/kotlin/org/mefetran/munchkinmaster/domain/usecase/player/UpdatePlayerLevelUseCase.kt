package org.mefetran.munchkinmaster.domain.usecase.player

import org.mefetran.munchkinmaster.domain.model.MaxLevel
import org.mefetran.munchkinmaster.domain.model.Player
import org.mefetran.munchkinmaster.domain.repository.PlayerRepository
import org.mefetran.munchkinmaster.domain.usecase.UseCase

interface UpdatePlayerLevelUseCase : UseCase<UpdatePlayerLevelUseCase.Params, Boolean> {
    data class Params(
        val player: Player,
        val newLevel: Int,
        val maxLevel: MaxLevel,
    )
}

class UpdatePlayerLevelUseCaseImpl(
    private val playerRepository: PlayerRepository,
) : UpdatePlayerLevelUseCase {
    override suspend fun execute(input: UpdatePlayerLevelUseCase.Params): Boolean {
        val updated = input.player.copy(level = input.newLevel.coerceIn(1, input.maxLevel.value))

        return playerRepository.updatePlayer(updated)
    }
}
