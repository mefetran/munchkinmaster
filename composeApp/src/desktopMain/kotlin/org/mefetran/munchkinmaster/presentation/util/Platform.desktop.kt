package org.mefetran.munchkinmaster.presentation.util

import androidx.compose.foundation.LocalScrollbarStyle
import androidx.compose.foundation.ScrollState
import androidx.compose.foundation.ScrollbarStyle
import androidx.compose.foundation.VerticalScrollbar
import androidx.compose.foundation.lazy.grid.LazyGridState
import androidx.compose.foundation.rememberScrollbarAdapter
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

actual val platformType: PlatformType = PlatformType.Desktop

@Composable
actual fun PlatformVerticalScrollbar(
    scrollState: ScrollState,
    modifier: Modifier
) {
    VerticalScrollbar(
        modifier = modifier,
        adapter = rememberScrollbarAdapter(scrollState)
    )
}

@Composable
actual fun PlatformVerticalScrollbar(
    scrollState: LazyGridState,
    modifier: Modifier
) {
    VerticalScrollbar(
        modifier = modifier,
        adapter = rememberScrollbarAdapter(scrollState)
    )
}

@Composable
actual fun PlatformScrollbarStyleProvider(
    colorScheme: ColorScheme,
    content: @Composable (() -> Unit)
) {
    CompositionLocalProvider(
        LocalScrollbarStyle provides scrollbarStyle(colorScheme)
    ) {
        content()
    }
}

fun scrollbarStyle(colorScheme: ColorScheme) = ScrollbarStyle(
    minimalHeight = 16.dp,
    thickness = 8.dp,
    shape = RoundedCornerShape(4.dp),
    hoverDurationMillis = 300,
    unhoverColor = colorScheme.primary.copy(alpha = 0.12f),
    hoverColor = colorScheme.primary.copy(alpha = 0.50f)
)