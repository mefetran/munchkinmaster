package org.mefetran.munchkinmaster.domain.usecase.player

import org.mefetran.munchkinmaster.domain.repository.PlayerRepository
import org.mefetran.munchkinmaster.domain.usecase.UseCase

interface DeletePlayersByIdsUseCase : UseCase<DeletePlayersByIdsUseCase.Params, Boolean> {
    data class Params(
        val playerIds: Set<Long>,
    )
}

class DeletePlayersByIdsUseCaseImpl(
    private val playerRepository: PlayerRepository,
) : DeletePlayersByIdsUseCase {
    override suspend fun execute(input: DeletePlayersByIdsUseCase.Params) = playerRepository.deletePlayersByIds(input.playerIds)
}