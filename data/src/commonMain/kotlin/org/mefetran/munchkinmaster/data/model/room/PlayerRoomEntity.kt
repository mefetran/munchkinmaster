package org.mefetran.munchkinmaster.data.model.room

import androidx.room.Entity
import androidx.room.PrimaryKey
import org.mefetran.munchkinmaster.data.model.PlayerEntity

@Entity
data class PlayerRoomEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val name: String,
    val sex: Int,
    val level: Int,
    val power: Int,
    val avatar: Int,
)

fun PlayerRoomEntity.toPlayerEntity() = PlayerEntity(
    id = this.id,
    name = this.name,
    sex = this.sex,
    level = this.level,
    power = this.power,
    avatar = this.avatar,
)

fun PlayerEntity.toPlayerRoomEntity() = PlayerRoomEntity(
    id = this.id,
    name = this.name,
    sex = this.sex,
    level = this.level,
    power = this.power,
    avatar = this.avatar,
)