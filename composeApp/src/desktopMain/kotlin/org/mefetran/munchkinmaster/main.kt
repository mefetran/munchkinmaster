package org.mefetran.munchkinmaster

import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application
import org.mefetran.munchkinmaster.di.initKoin

fun main() {
    initKoin()
    application {
        Window(
            onCloseRequest = ::exitApplication,
            title = "MunchkinMaster",
        ) {
            App()
        }
    }
}