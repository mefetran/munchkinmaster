package org.mefetran.munchkinmaster.presentation.util

import kotlinx.serialization.Serializable
import org.mefetran.munchkinmaster.domain.model.Avatar

@Serializable
data class AvatarConfig(
    val currentAvatar: Avatar,
    val playerId: Long? = null,
)

@Serializable
data object SelectPlayerConfig

@Serializable
data object DiceConfig
