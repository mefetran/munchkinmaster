package org.mefetran.munchkinmaster.presentation.ui.screen.root

import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.router.stack.ChildStack
import com.arkivanov.decompose.router.stack.StackNavigation
import com.arkivanov.decompose.router.stack.childStack
import com.arkivanov.decompose.router.stack.pop
import com.arkivanov.decompose.router.stack.pushNew
import com.arkivanov.decompose.value.Value
import kotlinx.serialization.Serializable
import org.mefetran.munchkinmaster.presentation.ui.screen.createplayer.CreatePlayerComponent
import org.mefetran.munchkinmaster.presentation.ui.screen.createplayer.DefaultCreatePlayerComponent
import org.mefetran.munchkinmaster.presentation.ui.screen.player.DefaultPlayerComponent
import org.mefetran.munchkinmaster.presentation.ui.screen.player.PlayerComponent
import org.mefetran.munchkinmaster.presentation.ui.screen.playerlist.DefaultPlayerListComponent
import org.mefetran.munchkinmaster.presentation.ui.screen.playerlist.PlayerListComponent

interface RootComponent {
    val stack: Value<ChildStack<*, Child>>

    sealed interface Child {
        class PlayerList(val component: PlayerListComponent) : Child
        class Player(val component: PlayerComponent) : Child
        class CreatePlayer(val component: CreatePlayerComponent) : Child
    }
}

class DefaultRootComponent(
    componentContext: ComponentContext,
) : RootComponent, ComponentContext by componentContext {
    private val navigation = StackNavigation<Config>()

    override val stack: Value<ChildStack<*, RootComponent.Child>> = childStack(
        source = navigation,
        serializer = Config.serializer(),
        initialConfiguration = Config.PlayerList,
        handleBackButton = true,
        childFactory = ::child
    )

    private fun child(
        config: Config,
        componentContext: ComponentContext
    ): RootComponent.Child = when (config) {
        is Config.Player -> RootComponent.Child.Player(
            DefaultPlayerComponent(
                componentContext = componentContext,
                playerId = config.playerId,
                onFinished = {
                    navigation.pop()
                }
            )
        )
        Config.PlayerList -> RootComponent.Child.PlayerList(
            DefaultPlayerListComponent(
                componentContext = componentContext,
                openPlayer = { playerId ->
                    navigation.pushNew(Config.Player(playerId))
                },
                openCreatePlayer = {
                    navigation.pushNew(Config.CreatePlayer)
                },
            )
        )

        Config.CreatePlayer -> RootComponent.Child.CreatePlayer(
            DefaultCreatePlayerComponent(
                componentContext = componentContext,
                onFinished = {
                    navigation.pop()
                }
            )
        )
    }

    @Serializable
    private sealed interface Config {
        @Serializable
        data object PlayerList : Config
        @Serializable
        data class Player(val playerId: Long) : Config
        @Serializable
        data object CreatePlayer : Config
    }
}