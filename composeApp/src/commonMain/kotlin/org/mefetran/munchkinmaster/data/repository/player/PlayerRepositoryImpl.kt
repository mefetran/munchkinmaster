package org.mefetran.munchkinmaster.data.repository.player

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import org.mefetran.munchkinmaster.data.model.toPlayer
import org.mefetran.munchkinmaster.data.model.toPlayerEntity
import org.mefetran.munchkinmaster.data.storage.PlayerStorage
import org.mefetran.munchkinmaster.domain.model.Player
import org.mefetran.munchkinmaster.domain.repository.PlayerRepository

class PlayerRepositoryImpl(
    private val playerStorage: PlayerStorage,
) : PlayerRepository {
    override suspend fun savePlayer(player: Player) =
        playerStorage.savePlayerEntity(player.toPlayerEntity())

    override suspend fun updatePlayer(player: Player) =
        playerStorage.updatePlayerEntity(player.toPlayerEntity())

    override fun getPlayersAsFlow(): Flow<List<Player>> = playerStorage.getPlayerEntitiesAsFlow()
        .map { playerEntities -> playerEntities.map { playerEntity -> playerEntity.toPlayer() } }

    override fun getPlayerById(playerId: Long): Flow<Player?> = playerStorage.getPlayerEntityById(playerId).map { playerEntity -> playerEntity?.toPlayer() }

    override suspend fun deleteAllPlayers() = playerStorage.deleteAllPlayerEntities()

    override suspend fun deletePlayerById(playerId: Long) = playerStorage.deletePlayerEntityById(playerId)

    override suspend fun deletePlayersByIds(playerIds: Set<Long>) = playerStorage.deletePlayerEntitiesByIds(playerIds)
}