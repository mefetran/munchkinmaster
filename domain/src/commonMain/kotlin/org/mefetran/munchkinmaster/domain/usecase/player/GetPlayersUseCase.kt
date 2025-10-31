package org.mefetran.munchkinmaster.domain.usecase.player

import org.mefetran.munchkinmaster.domain.repository.PlayerRepository

class GetPlayersUseCase(
    private val playerRepository: PlayerRepository,
) {
    operator fun invoke() = playerRepository.getPlayersAsFlow()
}