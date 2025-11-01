package org.mefetran.munchkinmaster.domain.usecase.player

import org.mefetran.munchkinmaster.domain.model.Player
import org.mefetran.munchkinmaster.domain.repository.PlayerRepository
import org.mefetran.munchkinmaster.domain.usecase.UseCase

interface CreatePlayerUseCase : UseCase<CreatePlayerUseCase.Params, Boolean> {
    data class Params(
        val player: Player,
    )
}

class CreatePlayerUseCaseImpl(
    private val playerRepository: PlayerRepository,
) : CreatePlayerUseCase {
    override suspend fun execute(input: CreatePlayerUseCase.Params) = playerRepository.createPlayer(input.player)
}