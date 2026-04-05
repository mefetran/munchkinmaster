package org.mefetran.munchkinmaster

import androidx.compose.ui.Alignment
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.DpSize
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.WindowPosition
import androidx.compose.ui.window.application
import androidx.compose.ui.window.rememberWindowState
import com.arkivanov.decompose.DefaultComponentContext
import com.arkivanov.decompose.extensions.compose.lifecycle.LifecycleController
import com.arkivanov.essenty.lifecycle.LifecycleRegistry
import org.mefetran.munchkinmaster.di.initKoin
import org.mefetran.munchkinmaster.presentation.App
import org.mefetran.munchkinmaster.presentation.ui.screen.root.DefaultRootComponent
import java.awt.Dimension
import javax.swing.SwingUtilities

const val minWindowWidth = 400
const val minWindowHeight = 600
const val windowWidth = 800
const val windowheight = 600

fun main() {
    initKoin()
    val lifecycle = LifecycleRegistry()
    val rootComponent = runOnUiThread {
        DefaultRootComponent(DefaultComponentContext(lifecycle))
    }

    application {
        val windowState = rememberWindowState(
            size = DpSize(windowWidth.dp, windowheight.dp),
            position = WindowPosition.Aligned(Alignment.Center)
        )

        LifecycleController(
            lifecycleRegistry = lifecycle,
            windowState = windowState,
        )

        Window(
            onCloseRequest = ::exitApplication,
            title = "MunchkinMaster",
            state = windowState
        ) {
            with(LocalDensity.current) {
                window.minimumSize = Dimension(minWindowWidth.dp.roundToPx(), minWindowHeight.dp.roundToPx())
            }

            App(rootComponent = rootComponent)
        }
    }
}

internal fun <T> runOnUiThread(block: () -> T): T {
    if (SwingUtilities.isEventDispatchThread()) {
        return block()
    }

    var error: Throwable? = null
    var result: T? = null

    SwingUtilities.invokeAndWait {
        try {
            result = block()
        } catch (e: Throwable) {
            error = e
        }
    }

    error?.also { throw it }

    @Suppress("UNCHECKED_CAST")
    return result as T
}