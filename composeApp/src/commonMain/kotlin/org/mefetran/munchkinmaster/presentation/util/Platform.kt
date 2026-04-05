package org.mefetran.munchkinmaster.presentation.util

import androidx.compose.foundation.ScrollState
import androidx.compose.foundation.lazy.grid.LazyGridState
import androidx.compose.material3.ColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

enum class PlatformType {
    Android,
    Desktop,
}

expect val platformType: PlatformType

@Composable
expect fun PlatformVerticalScrollbar(
    scrollState: ScrollState,
    modifier: Modifier = Modifier,
)

@Composable
expect fun PlatformVerticalScrollbar(
    scrollState: LazyGridState,
    modifier: Modifier = Modifier,
)

@Composable
expect fun PlatformScrollbarStyleProvider(
    colorScheme: ColorScheme,
    content: @Composable (() -> Unit)
)