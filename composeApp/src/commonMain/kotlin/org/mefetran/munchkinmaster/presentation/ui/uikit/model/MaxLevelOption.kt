package org.mefetran.munchkinmaster.presentation.ui.uikit.model

import munchkinmaster.composeapp.generated.resources.Res
import munchkinmaster.composeapp.generated.resources.epic_munchkin
import munchkinmaster.composeapp.generated.resources.standard
import munchkinmaster.composeapp.generated.resources.unlimited
import org.jetbrains.compose.resources.StringResource
import org.mefetran.munchkinmaster.domain.model.MaxLevel

sealed interface MaxLevelOption : LocalizedName{
    val maxLevel: MaxLevel
    data object Standard : MaxLevelOption {
        override val localizedName: StringResource = Res.string.standard
        override val maxLevel: MaxLevel = MaxLevel.Standard
    }

    data object EpicMunchkin : MaxLevelOption {
        override val localizedName: StringResource = Res.string.epic_munchkin
        override val maxLevel: MaxLevel = MaxLevel.EpicMunchkin
    }

    data object Unlimited : MaxLevelOption {
        override val localizedName: StringResource = Res.string.unlimited
        override val maxLevel: MaxLevel = MaxLevel.Unlimited
    }
}
