package org.mefetran.munchkinmaster.domain.model

const val PLAYER_MIN_LEVEL = 1

data class Player(
    val id: Long = 0,
    val name: String,
    val sex: Sex,
    val level: Int,
    val power: Int,
    val avatar: Avatar,
    val modificator: Int = 0,
)