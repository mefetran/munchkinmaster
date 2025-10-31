package org.mefetran.munchkinmaster.data.model

import org.mefetran.munchkinmaster.domain.model.Avatar
import org.mefetran.munchkinmaster.domain.model.Player
import org.mefetran.munchkinmaster.domain.model.Sex

data class PlayerEntity(
    val id: Long,
    val name: String,
    val sex: Int,
    val level: Int,
    val power: Int,
    val avatar: Int,
)

fun PlayerEntity.toPlayer() = Player(
    id = this.id,
    name = this.name,
    sex = Sex.entries[this.sex],
    level = this.level,
    power = this.power,
    avatar = Avatar.entries[this.avatar],
)

fun Player.toPlayerEntity() = PlayerEntity(
    id = this.id,
    name = this.name,
    sex = this.sex.ordinal,
    level = this.level,
    power = this.power,
    avatar = this.avatar.ordinal,
)
