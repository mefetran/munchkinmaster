package org.mefetran.munchkinmaster.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Player(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val name: String,
    val sex: String,
    val level: Int,
    val power: Int,
    val avatar: String,
)