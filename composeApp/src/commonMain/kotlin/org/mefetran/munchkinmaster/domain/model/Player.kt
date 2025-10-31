package org.mefetran.munchkinmaster.domain.model

data class Player(
    val id: Long = 0,
    val name: String,
    val sex: Sex,
    val level: Int,
    val power: Int,
    val avatar: Avatar,
)

fun Player.totalStrength() = level + power