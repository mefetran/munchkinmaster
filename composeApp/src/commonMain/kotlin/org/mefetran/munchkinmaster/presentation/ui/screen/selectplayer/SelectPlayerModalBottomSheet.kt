package org.mefetran.munchkinmaster.presentation.ui.screen.selectplayer

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
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
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.launch
import munchkinmaster.composeapp.generated.resources.Res
import munchkinmaster.composeapp.generated.resources.close
import munchkinmaster.composeapp.generated.resources.select_player_title
import org.jetbrains.compose.resources.stringResource
import org.jetbrains.compose.ui.tooling.preview.Preview
import org.mefetran.munchkinmaster.presentation.ui.uikit.card.PlayerItem

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SelectPlayerModalBottomSheet(
    component: SelectPlayerComponent,
    modifier: Modifier = Modifier,
) {
    val sheetState: SheetState = rememberModalBottomSheetState()
    val scope = rememberCoroutineScope()
    val playersState = component.playerListState

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
                text = stringResource(Res.string.select_player_title),
                style = MaterialTheme.typography.headlineSmall
            )
            LazyColumn(
                verticalArrangement = Arrangement.spacedBy(8.dp),
                modifier = Modifier.padding(top = 8.dp)
            ) {
                items(playersState.value) { player ->
                    PlayerItem(
                        player = player,
                        onClick = {
                            component.onPlayerClick(player)
                        },
                        onLongClick = {}
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

@Preview(showBackground = true, backgroundColor = 0xFFFFFFFF, locale = "en")
@Composable
fun SelectPlayerModalBottomSheetPreview() {
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
        SelectPlayerModalBottomSheet(
            component = FakeSelectPlayerComponent(onFinished = {
                showBottomSheet = false
            }),
        )
    }
}