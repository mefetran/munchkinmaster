package org.mefetran.munchkinmaster.domain.usecase.player

import kotlinx.coroutines.flow.Flow
import org.mefetran.munchkinmaster.domain.model.Player
import org.mefetran.munchkinmaster.domain.repository.PlayerRepository
import org.mefetran.munchkinmaster.domain.usecase.UseCase

interface GetPlayerByIdUseCase : UseCase<GetPlayerByIdUseCase.Params, Flow<Player?>> {
    data class Params(
        val playerId: Long,
    )
}

class GetPlayerByIdUseCaseImpl(
    private val playerRepository: PlayerRepository,
) : GetPlayerByIdUseCase {
    override suspend fun execute(input: GetPlayerByIdUseCase.Params) = playerRepository.getPlayerById(input.playerId)
}