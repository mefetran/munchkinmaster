package org.mefetran.munchkinmaster.domain.repository

import kotlinx.coroutines.flow.Flow
import org.mefetran.munchkinmaster.domain.model.Player

interface PlayerRepository {
    suspend fun createPlayer(player: Player): Boolean
    suspend fun updatePlayer(player: Player): Boolean
    fun getPlayersAsFlow(): Flow<List<Player>>
    fun getPlayerById(playerId: Long): Flow<Player?>
    suspend fun deleteAllPlayers()
    suspend fun deletePlayerById(playerId: Long): Boolean
    suspend fun deletePlayersByIds(playerIds: Set<Long>): Boolean
}