package org.mefetran.munchkinmaster.domain.usecase.player

import org.mefetran.munchkinmaster.domain.repository.PlayerRepository

class GetPlayerByIdUseCase(
    private val playerRepository: PlayerRepository,
) {
    operator fun invoke(playerId: Long) = playerRepository.getPlayerById(playerId)
}