package org.mefetran.munchkinmaster.ui.screen.root

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.arkivanov.decompose.extensions.compose.stack.Children
import com.arkivanov.decompose.extensions.compose.stack.animation.slide
import com.arkivanov.decompose.extensions.compose.stack.animation.stackAnimation
import org.mefetran.munchkinmaster.ui.screen.player.PlayerScreen
import org.mefetran.munchkinmaster.ui.screen.playerlist.PlayerListScreen

@Composable
fun RootContent(
    component: RootComponent,
    modifier: Modifier = Modifier,
) {
    Children(
        stack = component.stack,
        modifier = modifier,
        animation = stackAnimation(slide())
    ) {
        when (val child = it.instance) {
            is RootComponent.Child.Player -> PlayerScreen(component = child.component)
            is RootComponent.Child.PlayerList -> PlayerListScreen(component = child.component)
        }
    }
}