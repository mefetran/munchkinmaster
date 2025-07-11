package org.mefetran.munchkinmaster.db

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import kotlinx.coroutines.flow.Flow
import org.mefetran.munchkinmaster.model.PlayerEntity

@Dao
interface PlayerDao {
    @Insert
    suspend fun insert(item: PlayerEntity)

    @Query("SELECT * FROM PlayerEntity")
    fun getAllAsFlow(): Flow<List<PlayerEntity>>

    @Query("DELETE FROM PlayerEntity")
    suspend fun deleteAllPlayers()
}