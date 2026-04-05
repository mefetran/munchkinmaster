package org.mefetran.munchkinmaster.presentation.ui.uikit.util

import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.DpSize
import androidx.compose.ui.unit.dp
import org.mefetran.munchkinmaster.presentation.ui.uikit.util.WindowSizeClass.Companion.HEIGHT_DP_EXPANDED_LOWER_BOUND
import org.mefetran.munchkinmaster.presentation.ui.uikit.util.WindowSizeClass.Companion.HEIGHT_DP_MEDIUM_LOWER_BOUND
import org.mefetran.munchkinmaster.presentation.ui.uikit.util.WindowSizeClass.Companion.WIDTH_DP_EXPANDED_LOWER_BOUND
import org.mefetran.munchkinmaster.presentation.ui.uikit.util.WindowSizeClass.Companion.WIDTH_DP_EXTRA_LARGE_LOWER_BOUND
import org.mefetran.munchkinmaster.presentation.ui.uikit.util.WindowSizeClass.Companion.WIDTH_DP_LARGE_LOWER_BOUND
import org.mefetran.munchkinmaster.presentation.ui.uikit.util.WindowSizeClass.Companion.WIDTH_DP_MEDIUM_LOWER_BOUND

private object DpWidthSizeClasses {
    val Compact = 0.dp

    val Medium = WIDTH_DP_MEDIUM_LOWER_BOUND.dp

    val Expanded = WIDTH_DP_EXPANDED_LOWER_BOUND.dp

    val Large = WIDTH_DP_LARGE_LOWER_BOUND.dp

    val ExtraLarge = WIDTH_DP_EXTRA_LARGE_LOWER_BOUND.dp

    val Default = setOf(Compact, Medium, Expanded)

    val DefaultV2 = setOf(Compact, Medium, Expanded, Large, ExtraLarge)
}

private object DpHeightSizeClasses {
    val Compact = 0.dp

    val Medium = HEIGHT_DP_MEDIUM_LOWER_BOUND.dp

    val Expanded = HEIGHT_DP_EXPANDED_LOWER_BOUND.dp

    val Default = setOf(Compact, Medium, Expanded)
}

fun WindowSizeClass.Companion.computeFromDpSize(
    windowSize: DpSize,
    supportedWidthSizeClasses: Set<Dp> = DpWidthSizeClasses.Default,
    supportedHeightSizeClasses: Set<Dp> = DpHeightSizeClasses.Default,
) =
    WindowSizeClass(
        supportedWidthSizeClasses.filter { windowSize.width >= it }.maxOf { it.value },
        supportedHeightSizeClasses.filter { windowSize.height >= it }.maxOf { it.value },
    )

fun WindowSizeClass.Companion.computeFromDpSizeV2(
    windowSize: DpSize,
    supportedWidthSizeClasses: Set<Dp> = DpWidthSizeClasses.DefaultV2,
    supportedHeightSizeClasses: Set<Dp> = DpHeightSizeClasses.Default,
) = computeFromDpSize(windowSize, supportedWidthSizeClasses, supportedHeightSizeClasses)
