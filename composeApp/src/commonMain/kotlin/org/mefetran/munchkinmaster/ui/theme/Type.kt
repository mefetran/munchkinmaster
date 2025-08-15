package org.mefetran.munchkinmaster.ui.theme

import androidx.compose.material3.Typography
import androidx.compose.runtime.Composable
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import munchkinmaster.composeapp.generated.resources.Res
import munchkinmaster.composeapp.generated.resources.henny_penny
import munchkinmaster.composeapp.generated.resources.inter_bold
import munchkinmaster.composeapp.generated.resources.inter_medium
import munchkinmaster.composeapp.generated.resources.inter_regular
import org.jetbrains.compose.resources.Font

val Inter @Composable get() = FontFamily(
    Font(
        resource = Res.font.inter_regular,
        weight = FontWeight.Normal
    ),
    Font(
        resource = Res.font.inter_medium,
        weight = FontWeight.Medium
    ),
    Font(
        resource = Res.font.inter_bold,
        weight = FontWeight.Bold
    ),
)

val HennyPenny @Composable get() =  FontFamily(
    Font(
        resource = Res.font.henny_penny,
        weight = FontWeight.Normal
    )
)

val baseline = Typography()

val AppTypography @Composable get() =  Typography(
    displayLarge = baseline.displayLarge.copy(fontFamily = Inter),
    displayMedium = baseline.displayMedium.copy(fontFamily = Inter),
    displaySmall = baseline.displaySmall.copy(fontFamily = Inter),
    headlineLarge = baseline.headlineLarge.copy(fontFamily = Inter),
    headlineMedium = baseline.headlineMedium.copy(fontFamily = Inter),
    headlineSmall = baseline.headlineSmall.copy(fontFamily = Inter),
    titleLarge = baseline.titleLarge.copy(fontFamily = Inter),
    titleMedium = baseline.titleMedium.copy(fontFamily = Inter),
    titleSmall = baseline.titleSmall.copy(fontFamily = Inter),
    bodyLarge = baseline.bodyLarge.copy(fontFamily = Inter),
    bodyMedium = baseline.bodyMedium.copy(fontFamily = Inter),
    bodySmall = baseline.bodySmall.copy(fontFamily = Inter),
    labelLarge = baseline.labelLarge.copy(fontFamily = Inter),
    labelMedium = baseline.labelMedium.copy(fontFamily = Inter),
    labelSmall = baseline.labelSmall.copy(fontFamily = Inter),
)

val hennyPennyTextStyle @Composable get() = baseline.titleMedium.copy(
    fontFamily = HennyPenny,
    fontWeight = FontWeight.Normal,
)