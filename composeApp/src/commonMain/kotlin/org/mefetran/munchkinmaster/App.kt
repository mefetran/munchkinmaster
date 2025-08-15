package org.mefetran.munchkinmaster

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import org.jetbrains.compose.ui.tooling.preview.Preview
import org.mefetran.munchkinmaster.ui.screen.root.RootComponent
import org.mefetran.munchkinmaster.ui.screen.root.RootContent
import org.mefetran.munchkinmaster.ui.theme.MunchkinMasterTheme

@Composable
@Preview
fun App(rootComponent: RootComponent) {
    MunchkinMasterTheme {
        RootContent(
            component = rootComponent,
            modifier = Modifier.fillMaxSize()
        )
    }
}