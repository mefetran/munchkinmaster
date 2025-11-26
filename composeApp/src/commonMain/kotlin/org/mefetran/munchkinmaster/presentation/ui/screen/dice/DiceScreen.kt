package org.mefetran.munchkinmaster.presentation.ui.screen.dice

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.arkivanov.decompose.extensions.compose.subscribeAsState
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.ui.tooling.preview.Preview
import org.mefetran.munchkinmaster.presentation.util.getDrawableResource

@Composable
fun DiceScreen(
    component: DiceComponent,
    modifier: Modifier = Modifier,
) {
    val state = component.state.subscribeAsState()

    Dialog(
        properties = DialogProperties(),
        onDismissRequest = {
            component.dismiss()
        }
    ) {
        Box(modifier = modifier) {
            AnimatedContent(
                targetState = state.value.rollId to state.value.currentDice,
                transitionSpec = {
                    fadeIn() + scaleIn() togetherWith fadeOut() + scaleOut()
                }
            ) { (_, dice) ->
                Image(
                    painter = painterResource(dice.getDrawableResource()),
                    contentDescription = null,
                    modifier = Modifier
                        .size(136.dp)
                        .clip(shape = RoundedCornerShape(16.dp))
                        .clickable(
                            enabled = !state.value.isDicing,
                            onClick = component::dice
                        )
                )
            }
        }
    }
}

@Preview(showBackground = true, backgroundColor = 0xFFFFFFFF, locale = "ru")
@Composable
fun DiceScreenPreview() {
    Surface(
        modifier = Modifier.fillMaxSize()
    ) {
        DiceScreen(FakeDiceComponent())
    }
}
