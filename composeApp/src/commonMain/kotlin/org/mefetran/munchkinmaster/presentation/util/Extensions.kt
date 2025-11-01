package org.mefetran.munchkinmaster.presentation.util

import munchkinmaster.composeapp.generated.resources.Res
import munchkinmaster.composeapp.generated.resources.avatar_female_1
import munchkinmaster.composeapp.generated.resources.avatar_female_2
import munchkinmaster.composeapp.generated.resources.avatar_female_3
import munchkinmaster.composeapp.generated.resources.avatar_female_4
import munchkinmaster.composeapp.generated.resources.avatar_male_1
import munchkinmaster.composeapp.generated.resources.avatar_male_2
import munchkinmaster.composeapp.generated.resources.avatar_male_3
import munchkinmaster.composeapp.generated.resources.avatar_male_4
import org.mefetran.munchkinmaster.domain.model.Avatar
import org.mefetran.munchkinmaster.domain.model.Player

fun Avatar.getDrawableResource() = when (this) {
    Avatar.male1 -> Res.drawable.avatar_male_1
    Avatar.male2 -> Res.drawable.avatar_male_2
    Avatar.female1 -> Res.drawable.avatar_female_1
    Avatar.female2 -> Res.drawable.avatar_female_2
    Avatar.male3 -> Res.drawable.avatar_male_3
    Avatar.male4 -> Res.drawable.avatar_male_4
    Avatar.female3 -> Res.drawable.avatar_female_3
    Avatar.female4 -> Res.drawable.avatar_female_4
}

fun Player.totalStrength() = level + power