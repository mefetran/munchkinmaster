package org.mefetran.munchkinmaster.data.db

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow
import org.mefetran.munchkinmaster.data.model.room.PlayerRoomEntity

@Dao
interface PlayerDao {
    @Insert
    suspend fun insert(playerRoomEntity: PlayerRoomEntity): Long

    @Update
    suspend fun updatePlayer(playerRoomEntity: PlayerRoomEntity): Int

    @Query("SELECT * FROM PlayerRoomEntity")
    fun getPlayersAsFlow(): Flow<List<PlayerRoomEntity>>

    @Query("SELECT * FROM PlayerRoomEntity WHERE id = :playerId LIMIT 1")
    fun getPlayerById(playerId: Long): Flow<PlayerRoomEntity?>

    @Query("DELETE FROM PlayerRoomEntity")
    suspend fun deleteAllPlayers()

    @Query("DELETE FROM PlayerRoomEntity WHERE id = :playerId")
    suspend fun deletePlayerById(playerId: Long): Int

    @Query("DELETE FROM PlayerRoomEntity WHERE id IN (:playerIds)")
    suspend fun deletePlayersByIds(playerIds: Set<Long>): Int
}