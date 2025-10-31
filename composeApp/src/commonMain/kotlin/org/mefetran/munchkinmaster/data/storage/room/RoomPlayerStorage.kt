package org.mefetran.munchkinmaster.data.storage.room

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import org.mefetran.munchkinmaster.data.storage.PlayerStorage
import org.mefetran.munchkinmaster.data.db.PlayerDao
import org.mefetran.munchkinmaster.data.model.PlayerEntity
import org.mefetran.munchkinmaster.data.model.room.toPlayerEntity
import org.mefetran.munchkinmaster.data.model.room.toPlayerRoomEntity

class RoomPlayerStorage(
    private val playerDao: PlayerDao
) : PlayerStorage {
    override suspend fun createPlayerEntity(playerEntity: PlayerEntity) =
        playerDao.insert(playerEntity.toPlayerRoomEntity()) >= 0

    override suspend fun updatePlayerEntity(playerEntity: PlayerEntity) =
        playerDao.updatePlayer(playerEntity.toPlayerRoomEntity()) > 0

    override fun getPlayerEntitiesAsFlow(): Flow<List<PlayerEntity>> = playerDao.getPlayersAsFlow()
        .map { playerRoomEntities -> playerRoomEntities.map { playerRoomEntity -> playerRoomEntity.toPlayerEntity() } }

    override fun getPlayerEntityById(playerId: Long): Flow<PlayerEntity?> = playerDao.getPlayerById(playerId).map { it?.toPlayerEntity() }

    override suspend fun deleteAllPlayerEntities() = playerDao.deleteAllPlayers()

    override suspend fun deletePlayerEntityById(playerId: Long) = playerDao.deletePlayerById(playerId) > 0

    override suspend fun deletePlayerEntitiesByIds(playerIds: Set<Long>) = playerDao.deletePlayersByIds(playerIds) > 0
}