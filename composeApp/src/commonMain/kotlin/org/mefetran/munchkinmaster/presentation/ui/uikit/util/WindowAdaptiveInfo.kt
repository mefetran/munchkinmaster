package org.mefetran.munchkinmaster.presentation.ui.uikit.util

import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.LocalWindowInfo
import androidx.compose.ui.unit.toSize

@Composable
fun currentWindowSizeClass(supportLargeAndXLargeWidth: Boolean = false): WindowSizeClass {
    val windowSize =
        with(LocalDensity.current) { LocalWindowInfo.current.containerSize.toSize().toDpSize() }
    return if (supportLargeAndXLargeWidth) {
        WindowSizeClass.computeFromDpSizeV2(windowSize)
    } else {
        WindowSizeClass.computeFromDpSize(windowSize)
    }
}
