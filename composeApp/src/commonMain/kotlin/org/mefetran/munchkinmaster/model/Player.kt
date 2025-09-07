package org.mefetran.munchkinmaster.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Player(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val name: String,
    val sex: Sex,
    val level: Int,
    val power: Int,
    val avatar: Avatar,
)

fun Player.totalStrength() = level + power