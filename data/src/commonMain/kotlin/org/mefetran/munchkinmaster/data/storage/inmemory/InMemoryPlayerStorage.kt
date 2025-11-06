package org.mefetran.munchkinmaster.data.storage.inmemory

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.map
import org.mefetran.munchkinmaster.data.model.PlayerEntity
import org.mefetran.munchkinmaster.data.storage.PlayerStorage
import org.mefetran.munchkinmaster.domain.model.Avatar
import org.mefetran.munchkinmaster.domain.model.Sex

class InMemoryPlayerStorage : PlayerStorage {
    private val playersFlow = MutableStateFlow(
        listOf(
            PlayerEntity(
                id = 1,
                name = "Denis",
                sex = Sex.male.ordinal,
                level = 1,
                power = 1,
                avatar = Avatar.male2.ordinal,
            ),
            PlayerEntity(
                id = 2,
                name = "Lida",
                sex = Sex.female.ordinal,
                level = 2,
                power = 2,
                avatar = Avatar.female2.ordinal,
            )
        )
    )

    override suspend fun createPlayerEntity(playerEntity: PlayerEntity): Boolean {
        playersFlow.value += playerEntity
        return playersFlow.value.contains(playerEntity)
    }

    override suspend fun updatePlayerEntity(playerEntity: PlayerEntity): Boolean {
        val updated = playersFlow.value.map { if (it.id == playerEntity.id) playerEntity else it }
        val changed = updated != playersFlow.value
        playersFlow.value = updated
        return changed
    }

    override fun getPlayerEntitiesAsFlow(): Flow<List<PlayerEntity>> = playersFlow

    override fun getPlayerEntityById(playerId: Long): Flow<PlayerEntity?> = playersFlow.map { list -> list.firstOrNull { it.id == playerId } }

    override suspend fun deleteAllPlayerEntities() {
        playersFlow.value = emptyList()
    }

    override suspend fun deletePlayerEntityById(playerId: Long): Boolean {
        val beforeSize = playersFlow.value.size
        playersFlow.value = playersFlow.value.filterNot { it.id == playerId }
        return beforeSize != playersFlow.value.size
    }

    override suspend fun deletePlayerEntitiesByIds(playerIds: Set<Long>): Boolean {
        val beforeSize = playersFlow.value.size
        playersFlow.value = playersFlow.value.filterNot { it.id in playerIds }

        return beforeSize - playersFlow.value.size > 0
    }
}