package org.mefetran.munchkinmaster.presentation.ui.screen.avatar

import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.SheetState
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.material3.ripple
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import com.arkivanov.decompose.extensions.compose.subscribeAsState
import kotlinx.coroutines.launch
import munchkinmaster.composeapp.generated.resources.Res
import munchkinmaster.composeapp.generated.resources.close
import munchkinmaster.composeapp.generated.resources.select_avatar_title
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource
import org.jetbrains.compose.ui.tooling.preview.Preview
import org.mefetran.munchkinmaster.domain.model.Avatar
import org.mefetran.munchkinmaster.domain.model.getDrawableResource

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AvatarModalBottomSheet(
    component: AvatarComponent,
    modifier: Modifier = Modifier,
) {
    val state by component.state.subscribeAsState()
    val sheetState: SheetState = rememberModalBottomSheetState()
    val scope = rememberCoroutineScope()

    ModalBottomSheet(
        onDismissRequest = {
            component.onClose()
        },
        sheetState = sheetState,
        dragHandle = {},
        containerColor = MaterialTheme.colorScheme.surfaceContainer,
        modifier = modifier,
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.padding(all = 16.dp).fillMaxWidth(),
        ) {
            Text(
                text = stringResource(Res.string.select_avatar_title),
                style = MaterialTheme.typography.headlineSmall
            )
            FlowRow(
                maxItemsInEachRow = 4,
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp),
                modifier = Modifier.padding(top = 16.dp)
            ) {
                Avatar.entries.forEach { avatar ->
                    Image(
                        painter = painterResource(avatar.getDrawableResource()),
                        contentDescription = null,
                        contentScale = ContentScale.Crop,
                        modifier = Modifier
                            .size(72.dp)
                            .clip(CircleShape)
                            .border(
                                width = 3.dp,
                                color = if (state.selectedAvatar == avatar) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurface,
                                shape = CircleShape
                            )
                            .clickable(
                                interactionSource = remember { MutableInteractionSource() },
                                indication = ripple(bounded = false),
                                onClick = {
                                    component.onAvatarClick(avatar)
                                }
                            )
                    )
                }
            }
            Button(
                onClick = {
                    scope.launch {
                        sheetState.hide()
                    }.invokeOnCompletion {
                        component.onClose()
                    }
                },
                modifier = Modifier.padding(top = 16.dp)
            ) {
                Text(
                    text = stringResource(Res.string.close),
                    style = MaterialTheme.typography.labelMedium,
                    color = MaterialTheme.colorScheme.onPrimary,
                )
                Icon(
                    imageVector = Icons.Filled.Close,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.onPrimary,
                    modifier = Modifier.padding(start = 8.dp).size(24.dp)
                )
            }
        }
    }
}

@Preview
@Composable
fun AvatarModalBottomSheetPreview() {
    var showBottomSheet by remember { mutableStateOf(true) }

    Box(
        modifier = Modifier.fillMaxSize()
    ) {
        Button(
            onClick = {
                showBottomSheet = !showBottomSheet
            },
            modifier = Modifier.align(Alignment.Center)
        ) {
            Text(text = "Hello, there!")
        }
    }
    if (showBottomSheet) {
        AvatarModalBottomSheet(
            component = FakeAvatarComponent(
                avatar = Avatar.female1,
                onFinished = {
                    showBottomSheet = false
                }
            ),
        )
    }
}