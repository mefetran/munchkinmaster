package org.mefetran.munchkinmaster.presentation.ui.screen.createplayer

import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawing
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.text.input.InputTransformation
import androidx.compose.foundation.text.input.maxLength
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Female
import androidx.compose.material.icons.filled.Male
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.ripple
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.backhandler.BackHandler
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.DialogProperties
import com.arkivanov.decompose.extensions.compose.subscribeAsState
import munchkinmaster.composeapp.generated.resources.Res
import munchkinmaster.composeapp.generated.resources.create
import munchkinmaster.composeapp.generated.resources.enter_name
import munchkinmaster.composeapp.generated.resources.name
import munchkinmaster.composeapp.generated.resources.sex
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource
import org.jetbrains.compose.ui.tooling.preview.Preview
import org.mefetran.munchkinmaster.domain.model.Sex
import org.mefetran.munchkinmaster.presentation.ui.screen.avatar.AvatarModalBottomSheet
import org.mefetran.munchkinmaster.presentation.ui.uikit.dialog.ErrorDialog
import org.mefetran.munchkinmaster.presentation.ui.uikit.textfield.TextField
import org.mefetran.munchkinmaster.presentation.util.getDrawableResource

const val MaxNameLength = 40

@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun CreatePlayerScreen(
    component: CreatePlayerComponent,
    modifier: Modifier = Modifier,
) {
    val state by component.state.subscribeAsState()
    val keyboardController = LocalSoftwareKeyboardController.current
    val selectAvatarSlot by component.selectAvatarSlot.subscribeAsState()

    BackHandler {
        keyboardController?.hide()
        component.onBackClick()
    }

    Surface(
        color = MaterialTheme.colorScheme.surface,
        modifier = modifier.fillMaxSize()
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState()),
        ) {
            IconButton(
                onClick = {
                    keyboardController?.hide()
                    component.onBackClick()
                },
                modifier = Modifier.padding(
                    top = WindowInsets.safeDrawing.asPaddingValues().calculateTopPadding()
                ).align(Alignment.Start)
            ) {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.primary,
                )
            }
            Image(
                painter = painterResource(state.selectedAvatar.getDrawableResource()),
                contentDescription = null,
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .size(120.dp)
                    .clip(CircleShape)
                    .border(
                        width = 3.dp,
                        color = MaterialTheme.colorScheme.primary,
                        shape = CircleShape
                    )
                    .clickable(
                        interactionSource = remember { MutableInteractionSource() },
                        indication = ripple(bounded = false),
                        onClick = {
                            component.onAvatarClick()
                        }
                    )
                    .align(Alignment.CenterHorizontally)
            )
            Text(
                text = stringResource(Res.string.name) + ":",
                style = MaterialTheme.typography.titleMedium,
                modifier = Modifier.padding(horizontal = 16.dp)
            )
            TextField(
                state = component.nameState,
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Text,
                    imeAction = ImeAction.Done,
                ),
                onKeyboardAction = { action ->
                    action()
                    keyboardController?.hide()
                },
                inputTransformation = InputTransformation.maxLength(MaxNameLength),
                placeholder = {
                    Text(text = stringResource(Res.string.enter_name))
                },
                modifier = Modifier.padding(horizontal = 16.dp).padding(bottom = 16.dp, top = 8.dp)
                    .fillMaxWidth(1f),
            )
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.padding(horizontal = 16.dp)
            ) {
                Text(
                    text = stringResource(Res.string.sex) + ":",
                    style = MaterialTheme.typography.titleMedium
                )
                Icon(
                    imageVector = if (state.sex == Sex.male) Icons.Default.Male else Icons.Default.Female,
                    contentDescription = "Gender",
                    tint = if (state.sex == Sex.male) Color.Blue else Color.Magenta,
                    modifier = Modifier.padding(start = 8.dp).size(24.dp)
                        .clip(RoundedCornerShape(8.dp))
                        .clickable(
                            interactionSource = remember { MutableInteractionSource() },
                            indication = ripple(bounded = true),
                            onClick = component::onSexChange
                        )
                )
            }
            TextButton(
                onClick = component::onCreatePlayerClick,
                modifier = Modifier.padding(top = 8.dp, end = 16.dp).align(Alignment.End)
            ) {
                Text(
                    text = stringResource(Res.string.create)
                )
            }
        }

        state.errorMessageResId?.let { errorMessageResId ->
            ErrorDialog(
                errorMessageResId = errorMessageResId,
                onDismissRequest = component::hideErrorMessage,
                onOkClick = component::hideErrorMessage,
                properties = DialogProperties(dismissOnClickOutside = false)
            )
        }

        selectAvatarSlot.child?.let { child ->
            AvatarModalBottomSheet(
                component = child.instance,
            )
        }
    }
}

@Preview
@Composable
fun CreatePlayerScreenPreview() {
    CreatePlayerScreen(
        component = FakeCreatePlayerComponent()
    )
}
