package org.mefetran.munchkinmaster.domain.usecase.player

import org.mefetran.munchkinmaster.domain.model.Player
import org.mefetran.munchkinmaster.domain.repository.PlayerRepository
import org.mefetran.munchkinmaster.domain.usecase.UseCase

interface UpdatePlayerUseCase : UseCase<UpdatePlayerUseCase.Params, Boolean> {
    data class Params(
        val player: Player,
    )
}

class UpdatePlayerUseCaseImpl(
    private val playerRepository: PlayerRepository,
) : UpdatePlayerUseCase {
    override suspend fun execute(input: UpdatePlayerUseCase.Params) = playerRepository.updatePlayer(input.player)
}