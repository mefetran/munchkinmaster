package org.mefetran.munchkinmaster.repository

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.map
import org.mefetran.munchkinmaster.db.PlayerDao
import org.mefetran.munchkinmaster.model.Player

interface PlayerRepository {
    suspend fun insert(item: Player)
    suspend fun updatePlayer(item: Player): Int
    fun getPlayersAsFlow(): Flow<List<Player>>
    fun getPlayerById(playerId: Long): Flow<Player?>
    suspend fun deleteAllPlayers()
    suspend fun deletePlayerById(playerId: Long): Int
}

class DefaultPlayerRepository(
    private val playerDao: PlayerDao
) : PlayerRepository {
    override suspend fun insert(item: Player) = playerDao.insert(item)
    override suspend fun updatePlayer(item: Player): Int = playerDao.updatePlayer(item)
    override fun getPlayersAsFlow(): Flow<List<Player>> = playerDao.getPlayersAsFlow()
    override fun getPlayerById(playerId: Long): Flow<Player?> = playerDao.getPlayerById(playerId)

    override suspend fun deleteAllPlayers() = playerDao.deleteAllPlayers()
    override suspend fun deletePlayerById(playerId: Long): Int =
        playerDao.deletePlayerById(playerId)
}

class MockPlayerRepository() : PlayerRepository {
    private val playersFlow = MutableStateFlow(
        listOf(
            Player(
                id = 1,
                name = "Denis",
                sex = "male",
                level = 1,
                power = 1,
                avatar = "",
            ),
            Player(
                id = 2,
                name = "Lida",
                sex = "female",
                level = 2,
                power = 2,
                avatar = "",
            )
        )
    )

    override suspend fun insert(item: Player) {
        playersFlow.value = playersFlow.value + item
    }

    override suspend fun updatePlayer(item: Player): Int {
        val updated = playersFlow.value.map { if (it.id == item.id) item else it }
        val changed = updated != playersFlow.value
        playersFlow.value = updated
        return if (changed) 1 else 0
    }

    override fun getPlayersAsFlow(): Flow<List<Player>> = playersFlow

    override fun getPlayerById(playerId: Long): Flow<Player?> =
        playersFlow.map { list -> list.firstOrNull { it.id == playerId } }


    override suspend fun deleteAllPlayers() {
        playersFlow.value = emptyList()
    }

    override suspend fun deletePlayerById(playerId: Long): Int {
        val beforeSize = playersFlow.value.size
        playersFlow.value = playersFlow.value.filterNot { it.id == playerId }
        return if (beforeSize != playersFlow.value.size) 1 else 0
    }
}