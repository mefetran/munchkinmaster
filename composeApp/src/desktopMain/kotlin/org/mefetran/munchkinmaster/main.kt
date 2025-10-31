package org.mefetran.munchkinmaster

import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application
import androidx.compose.ui.window.rememberWindowState
import com.arkivanov.decompose.DefaultComponentContext
import com.arkivanov.decompose.extensions.compose.lifecycle.LifecycleController
import com.arkivanov.essenty.lifecycle.LifecycleRegistry
import org.mefetran.munchkinmaster.di.initKoin
import org.mefetran.munchkinmaster.presentation.App
import org.mefetran.munchkinmaster.presentation.ui.screen.root.DefaultRootComponent
import javax.swing.SwingUtilities

fun main() {
    initKoin()
    val lifecycle = LifecycleRegistry()
    val rootComponent = runOnUiThread {
        DefaultRootComponent(DefaultComponentContext(lifecycle))
    }

    application {
        val windowState = rememberWindowState()

        LifecycleController(
            lifecycleRegistry = lifecycle,
            windowState = windowState,
        )

        Window(
            onCloseRequest = ::exitApplication,
            title = "MunchkinMaster",
            state = windowState
        ) {
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