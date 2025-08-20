package org.mefetran.munchkinmaster.model

import munchkinmaster.composeapp.generated.resources.Res
import munchkinmaster.composeapp.generated.resources.avatar_male_1
import munchkinmaster.composeapp.generated.resources.avatar_female_1
import munchkinmaster.composeapp.generated.resources.avatar_male_2
import munchkinmaster.composeapp.generated.resources.avatar_female_2
import munchkinmaster.composeapp.generated.resources.avatar_female_3
import munchkinmaster.composeapp.generated.resources.avatar_female_4
import munchkinmaster.composeapp.generated.resources.avatar_male_3
import munchkinmaster.composeapp.generated.resources.avatar_male_4

enum class Avatar {
    male1,
    male2,
    female1,
    female2,
    male3,
    male4,
    female3,
    female4;
}

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