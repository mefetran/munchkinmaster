package org.mefetran.munchkinmaster.presentation.ui.uikit.util

import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.safeDrawing
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.platform.LocalWindowInfo
import androidx.compose.ui.unit.Dp

@Composable
fun rememberScreenWidth(): Dp {
    val density = LocalDensity.current
    val rawScreenWidth = LocalWindowInfo.current.containerSize.width
    val layoutDirection = LocalLayoutDirection.current
    val rawLeftPadding = WindowInsets.safeDrawing.getLeft(density, layoutDirection)
    val rawRightPadding = WindowInsets.safeDrawing.getRight(density, layoutDirection)
    val screenWidth =
        remember(density, rawScreenWidth, layoutDirection, rawLeftPadding, rawRightPadding) {
            with(density) {
                rawScreenWidth.toDp() - rawLeftPadding.toDp() - rawRightPadding.toDp()
            }
        }

    return screenWidth
}

@Composable
fun rememberScreenHeight(): Dp {
    val density = LocalDensity.current
    val rawScreenHeight = LocalWindowInfo.current.containerSize.height
    val rawTopPadding = WindowInsets.safeDrawing.getTop(density)
    val rawBottomPadding = WindowInsets.safeDrawing.getBottom(density)
    val screenHeight =
        remember(density, rawScreenHeight) {
            with(density) {
                rawScreenHeight.toDp() - rawTopPadding.toDp() - rawBottomPadding.toDp()
            }
        }

    return screenHeight
}