package org.mefetran.munchkinmaster.data.storage

import kotlinx.coroutines.flow.Flow
import org.mefetran.munchkinmaster.data.model.PlayerEntity

interface PlayerStorage {
    suspend fun savePlayerEntity(playerEntity: PlayerEntity): Boolean
    suspend fun updatePlayerEntity(playerEntity: PlayerEntity): Boolean
    fun getPlayerEntitiesAsFlow(): Flow<List<PlayerEntity>>
    fun getPlayerEntityById(playerId: Long): Flow<PlayerEntity?>
    suspend fun deleteAllPlayerEntities()
    suspend fun deletePlayerEntityById(playerId: Long): Boolean
    suspend fun deletePlayerEntitiesByIds(playerIds: Set<Long>): Boolean
}