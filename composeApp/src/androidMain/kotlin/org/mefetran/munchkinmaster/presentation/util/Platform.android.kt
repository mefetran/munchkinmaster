package org.mefetran.munchkinmaster.presentation.util

import androidx.compose.foundation.ScrollState
import androidx.compose.foundation.lazy.grid.LazyGridState
import androidx.compose.material3.ColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

actual val platformType: PlatformType = PlatformType.Android

@Composable
actual fun PlatformVerticalScrollbar(
    scrollState: ScrollState,
    modifier: Modifier
) {

}

@Composable
actual fun PlatformVerticalScrollbar(
    scrollState: LazyGridState,
    modifier: Modifier
) {
}

@Composable
actual fun PlatformScrollbarStyleProvider(
    colorScheme: ColorScheme,
    content: @Composable (() -> Unit)
) {
    content()
}