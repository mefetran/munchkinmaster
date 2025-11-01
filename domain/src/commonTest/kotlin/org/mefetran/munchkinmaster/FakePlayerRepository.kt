package org.mefetran.munchkinmaster

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf
import org.mefetran.munchkinmaster.domain.model.Player
import org.mefetran.munchkinmaster.domain.repository.PlayerRepository

class FakePlayerRepository : PlayerRepository {

    var players = emptyList<Player>()

    override suspend fun createPlayer(player: Player): Boolean {
        players = players + player

        return players.contains(player)
    }

    override suspend fun updatePlayer(player: Player): Boolean {
        val privPlayer = players.firstOrNull { it.id == player.id }

        if (privPlayer != null) {
            players = players - privPlayer
            players = players + player

            return players.contains(player) && !players.contains(privPlayer)
        }

        return false
    }

    override fun getPlayersAsFlow(): Flow<List<Player>> = flowOf(players)

    override fun getPlayerById(playerId: Long): Flow<Player?> = flowOf(players.firstOrNull { it.id == playerId })

    override suspend fun deleteAllPlayers() {
        players = emptyList()
    }

    override suspend fun deletePlayerById(playerId: Long): Boolean {
        val player = players.firstOrNull { it.id == playerId }

        player?.let { players = players - player }

        return players.contains(player)
    }

    override suspend fun deletePlayersByIds(playerIds: Set<Long>): Boolean {
        val playersMutable = players.toMutableList()
        playersMutable.removeAll { player -> playerIds.contains(player.id) }
        players = playersMutable.toList()

        return !players.any { player -> playerIds.contains(player.id) }
    }
}