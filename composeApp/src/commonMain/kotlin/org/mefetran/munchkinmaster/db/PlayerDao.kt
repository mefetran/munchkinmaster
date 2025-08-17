package org.mefetran.munchkinmaster.db

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow
import org.mefetran.munchkinmaster.model.Player

@Dao
interface PlayerDao {
    @Insert
    suspend fun insert(item: Player): Long

    @Update
    suspend fun updatePlayer(item: Player): Int

    @Query("SELECT * FROM Player")
    fun getPlayersAsFlow(): Flow<List<Player>>

    @Query("SELECT * FROM Player WHERE id = :playerId LIMIT 1")
    fun getPlayerById(playerId: Long): Flow<Player?>

    @Query("DELETE FROM Player")
    suspend fun deleteAllPlayers()

    @Query("DELETE FROM Player WHERE id = :playerId")
    suspend fun deletePlayerById(playerId: Long): Int

    @Query("DELETE FROM Player WHERE id IN (:playerIds)")
    suspend fun deletePlayersByIds(playerIds: Set<Long>): Int
}