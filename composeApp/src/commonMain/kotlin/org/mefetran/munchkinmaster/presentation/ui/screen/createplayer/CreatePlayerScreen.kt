package org.mefetran.munchkinmaster.presentation.ui.screen.createplayer

import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.calculateEndPadding
import androidx.compose.foundation.layout.calculateStartPadding
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawing
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
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
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.DialogProperties
import androidx.navigationevent.NavigationEventInfo
import androidx.navigationevent.compose.NavigationEventHandler
import androidx.navigationevent.compose.rememberNavigationEventState
import com.arkivanov.decompose.extensions.compose.subscribeAsState
import munchkinmaster.composeapp.generated.resources.Res
import munchkinmaster.composeapp.generated.resources.create
import munchkinmaster.composeapp.generated.resources.enter_name
import munchkinmaster.composeapp.generated.resources.name
import munchkinmaster.composeapp.generated.resources.sex
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource
import org.mefetran.munchkinmaster.domain.model.Sex
import org.mefetran.munchkinmaster.presentation.ui.screen.avatar.AvatarModalBottomSheet
import org.mefetran.munchkinmaster.presentation.ui.uikit.dialog.ErrorDialog
import org.mefetran.munchkinmaster.presentation.ui.uikit.textfield.TextField
import org.mefetran.munchkinmaster.presentation.ui.uikit.util.WindowSizeClass
import org.mefetran.munchkinmaster.presentation.ui.uikit.util.conditional
import org.mefetran.munchkinmaster.presentation.ui.uikit.util.currentWindowSizeClass
import org.mefetran.munchkinmaster.presentation.util.PlatformVerticalScrollbar
import org.mefetran.munchkinmaster.presentation.util.getDrawableResource

const val MaxNameLength = 40

@Composable
fun CreatePlayerScreen(
    component: CreatePlayerComponent,
    modifier: Modifier = Modifier,
) {
    val windowSizeClass = currentWindowSizeClass()
    val isWide by remember(windowSizeClass) { mutableStateOf(windowSizeClass.isWidthAtLeastBreakpoint(WindowSizeClass.WIDTH_DP_EXPANDED_LOWER_BOUND)) }
    val scrollState = rememberScrollState()
    val state by component.state.subscribeAsState()
    val keyboardController = LocalSoftwareKeyboardController.current
    val selectAvatarSlot by component.selectAvatarSlot.subscribeAsState()
    val layoutDirection = LocalLayoutDirection.current

    NavigationEventHandler(
        state = rememberNavigationEventState(NavigationEventInfo.None),
        onBackCompleted = {
            keyboardController?.hide()
            component.onBackClick()
        }
    )

    Surface(
        color = MaterialTheme.colorScheme.surface,
        modifier = modifier.fillMaxSize()
    ) {
        Box(
            contentAlignment = if (isWide) Alignment.Center else Alignment.TopStart,
            modifier = Modifier
                .fillMaxSize()
                .statusBarsPadding()
                .navigationBarsPadding()
        ) {
            Column(
                modifier = Modifier
                    .padding(top = 56.dp)
                    .fillMaxWidth()
                    .verticalScroll(scrollState),
            ) {
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
                Column(
                    modifier = Modifier
                        .padding(
                            start = WindowInsets
                                .safeDrawing
                                .asPaddingValues()
                                .calculateStartPadding(layoutDirection),
                            end = WindowInsets
                                .safeDrawing
                                .asPaddingValues()
                                .calculateEndPadding(layoutDirection)
                        )
                        .conditional(
                            condition = isWide,
                            ifTrue = {
                                width(WindowSizeClass.WIDTH_DP_EXPANDED_LOWER_BOUND.dp)
                                    .align(Alignment.CenterHorizontally)
                            }
                        )
                ) {
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
                        textStyle = MaterialTheme.typography.titleMedium.copy(color = MaterialTheme.colorScheme.onSurface),
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
                    SexBlock(
                        sex = state.sex,
                        modifier = Modifier.padding(horizontal = 16.dp),
                        onSexChange = component::onSexChange
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
            PlatformVerticalScrollbar(
                scrollState = scrollState,
                modifier = Modifier.align(Alignment.CenterEnd).fillMaxHeight()
            )
            IconButton(
                onClick = {
                    keyboardController?.hide()
                    component.onBackClick()
                },
                modifier = Modifier.align(Alignment.TopStart)
            ) {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.primary,
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

@Composable
private fun SexBlock(
    sex: Sex,
    modifier: Modifier = Modifier,
    onSexChange: () -> Unit,
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = modifier
    ) {
        Text(
            text = stringResource(Res.string.sex) + ":",
            style = MaterialTheme.typography.titleMedium
        )
        Icon(
            imageVector = if (sex == Sex.male) Icons.Default.Male else Icons.Default.Female,
            contentDescription = "Gender",
            tint = if (sex == Sex.male) Color.Blue else Color.Magenta,
            modifier = Modifier.padding(start = 8.dp).size(24.dp)
                .clip(RoundedCornerShape(8.dp))
                .clickable(
                    interactionSource = remember { MutableInteractionSource() },
                    indication = ripple(bounded = true),
                    onClick = onSexChange
                )
        )
    }
}

@Preview
@Composable
fun CreatePlayerScreenPreview() {
    CreatePlayerScreen(
        component = FakeCreatePlayerComponent()
    )
}
