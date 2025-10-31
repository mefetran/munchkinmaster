package org.mefetran.munchkinmaster.domain.usecase.player

import org.mefetran.munchkinmaster.domain.repository.PlayerRepository

class DeletePlayersByIdsUseCase(
    private val playerRepository: PlayerRepository,
) {
    suspend operator fun invoke(playerIds: Set<Long>) = playerRepository.deletePlayersByIds(playerIds)
}