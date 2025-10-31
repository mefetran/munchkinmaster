package org.mefetran.munchkinmaster.domain.usecase.player

import org.mefetran.munchkinmaster.domain.model.Player
import org.mefetran.munchkinmaster.domain.repository.PlayerRepository

class CreatePlayerUseCase(
    private val playerRepository: PlayerRepository,
) {
    suspend operator fun invoke(player: Player): Boolean = playerRepository.createPlayer(player)
}