package org.mefetran.munchkinmaster.presentation.ui.uikit.card

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.CopyAll
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import munchkinmaster.composeapp.generated.resources.Res
import munchkinmaster.composeapp.generated.resources.level
import munchkinmaster.composeapp.generated.resources.modificator
import munchkinmaster.composeapp.generated.resources.monster
import org.jetbrains.compose.resources.stringResource
import org.jetbrains.compose.ui.tooling.preview.Preview
import org.mefetran.munchkinmaster.domain.model.Monster

@Composable
fun BattleMonsterCard(
    name: String,
    monster: Monster,
    modifier: Modifier = Modifier,
    onLevelChange: (Int) -> Unit,
    onModificatorChange: (Int) -> Unit,
    onDeleteClick: (() -> Unit)? = null,
    onCloneMonsterClick: () -> Unit,
) {
    Card(
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.primaryContainer,
        ),
        modifier = modifier,
    ) {
        Box(
            modifier = Modifier
                .padding(top = 12.dp)
                .padding(horizontal = 12.dp)
                .fillMaxWidth(),
        ) {
            Text(
                text = name,
                style = MaterialTheme.typography.headlineSmall,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(vertical = 8.dp).align(Alignment.Center)
            )
            onDeleteClick?.let {
                IconButton(
                    onClick = it,
                    modifier = Modifier.align(Alignment.CenterEnd)
                ) {
                    Icon(
                        imageVector = Icons.Default.Close,
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.onPrimaryContainer
                    )
                }
            }
            IconButton(
                onClick = onCloneMonsterClick,
                modifier = Modifier.align(Alignment.CenterStart),
            ) {
                Icon(
                    imageVector = Icons.Default.CopyAll,
                    contentDescription = null,
                )
            }
        }

        Row(
            horizontalArrangement = Arrangement.SpaceAround,
            modifier = Modifier.padding(12.dp).fillMaxWidth()
        ) {
            StatusCard(
                title = stringResource(Res.string.level),
                value = monster.level,
                onValueChange = onLevelChange
            )
            StatusCard(
                title = stringResource(Res.string.modificator),
                value = monster.modificator,
                onValueChange = onModificatorChange
            )
        }
    }
}

@Preview(showBackground = true, backgroundColor = 0xFFFFFFFF, locale = "en")
@Composable
fun BattleMonsterCardPreview() {
    val monsterState = remember { mutableStateOf(Monster(id = "some_id", level = 4, modificator = -2)) }

    BattleMonsterCard(
        name = stringResource(Res.string.monster) + " 1",
        monster = monsterState.value,
        modifier = Modifier.padding(16.dp),
        onLevelChange = { newLevel ->
            monsterState.value = monsterState.value.copy(level = newLevel)
        },
        onModificatorChange = { newModificator ->
            monsterState.value = monsterState.value.copy(modificator = newModificator)
        },
        onDeleteClick = {},
        onCloneMonsterClick = {},
    )
}
