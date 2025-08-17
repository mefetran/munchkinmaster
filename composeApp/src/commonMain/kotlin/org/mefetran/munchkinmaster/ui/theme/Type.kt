package org.mefetran.munchkinmaster.ui.theme

import androidx.compose.material3.Typography
import androidx.compose.runtime.Composable
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import munchkinmaster.composeapp.generated.resources.Res
import munchkinmaster.composeapp.generated.resources.cattedrale_regular
import org.jetbrains.compose.resources.Font

val Cattedrale @Composable get() = FontFamily(
    Font(
        resource = Res.font.cattedrale_regular,
        weight = FontWeight.Normal
    ),
)

val baseline = Typography()

val AppTypography @Composable get() =  Typography(
    displayLarge = baseline.displayLarge.copy(fontFamily = Cattedrale),
    displayMedium = baseline.displayMedium.copy(fontFamily = Cattedrale),
    displaySmall = baseline.displaySmall.copy(fontFamily = Cattedrale),
    headlineLarge = baseline.headlineLarge.copy(fontFamily = Cattedrale),
    headlineMedium = baseline.headlineMedium.copy(fontFamily = Cattedrale),
    headlineSmall = baseline.headlineSmall.copy(fontFamily = Cattedrale),
    titleLarge = baseline.titleLarge.copy(fontFamily = Cattedrale),
    titleMedium = baseline.titleMedium.copy(fontFamily = Cattedrale),
    titleSmall = baseline.titleSmall.copy(fontFamily = Cattedrale),
    bodyLarge = baseline.bodyLarge.copy(fontFamily = Cattedrale),
    bodyMedium = baseline.bodyMedium.copy(fontFamily = Cattedrale),
    bodySmall = baseline.bodySmall.copy(fontFamily = Cattedrale),
    labelLarge = baseline.labelLarge.copy(fontFamily = Cattedrale),
    labelMedium = baseline.labelMedium.copy(fontFamily = Cattedrale),
    labelSmall = baseline.labelSmall.copy(fontFamily = Cattedrale),
)