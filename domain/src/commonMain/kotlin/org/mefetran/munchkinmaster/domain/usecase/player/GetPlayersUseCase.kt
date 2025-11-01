package org.mefetran.munchkinmaster.domain.usecase.player

import kotlinx.coroutines.flow.Flow
import org.mefetran.munchkinmaster.domain.model.Player
import org.mefetran.munchkinmaster.domain.repository.PlayerRepository
import org.mefetran.munchkinmaster.domain.usecase.UseCase

interface GetPlayersUseCase : UseCase<Unit, Flow<List<Player>>>

class GetPlayersUseCaseImpl(
    private val playerRepository: PlayerRepository,
) : GetPlayersUseCase {
    override suspend fun execute(input: Unit) = playerRepository.getPlayersAsFlow()
}