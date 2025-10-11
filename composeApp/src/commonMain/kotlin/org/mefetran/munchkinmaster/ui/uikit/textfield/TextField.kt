package org.mefetran.munchkinmaster.ui.uikit.textfield

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.ScrollState
import androidx.compose.foundation.border
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.text.input.InputTransformation
import androidx.compose.foundation.text.input.KeyboardActionHandler
import androidx.compose.foundation.text.input.OutputTransformation
import androidx.compose.foundation.text.input.TextFieldLineLimits
import androidx.compose.foundation.text.input.TextFieldState
import androidx.compose.foundation.text.input.rememberTextFieldState
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ProvideTextStyle
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.text.TextLayoutResult
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.Density
import androidx.compose.ui.unit.dp
import org.jetbrains.compose.ui.tooling.preview.Preview

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TextField(
    state: TextFieldState,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    readOnly: Boolean = false,
    inputTransformation: InputTransformation? = null,
    textStyle: TextStyle = MaterialTheme.typography.titleMedium,
    placeholder: (@Composable () -> Unit)? = null,
    keyboardOptions: KeyboardOptions = KeyboardOptions.Default,
    onKeyboardAction: KeyboardActionHandler? = null,
    lineLimits: TextFieldLineLimits = TextFieldLineLimits.SingleLine,
    onTextLayout: (Density.(() -> TextLayoutResult?) -> Unit)? = null,
    interactionSource: MutableInteractionSource? = null,
    cursorBrush: Brush = SolidColor(MaterialTheme.colorScheme.onSurface),
    outputTransformation: OutputTransformation? = null,
    scrollState: ScrollState = rememberScrollState(),
) {
    val isFocused = remember { mutableStateOf(false) }

    BasicTextField(
        state = state,
        modifier = modifier.onFocusChanged { focusState ->
            isFocused.value = focusState.isFocused
        },
        enabled = enabled,
        readOnly = readOnly,
        inputTransformation = inputTransformation,
        textStyle = textStyle,
        keyboardOptions = keyboardOptions,
        onKeyboardAction = onKeyboardAction,
        lineLimits = lineLimits,
        onTextLayout = onTextLayout,
        interactionSource = interactionSource,
        cursorBrush = cursorBrush,
        outputTransformation = outputTransformation,
        scrollState = scrollState,
        decorator = { innerTextField ->
            ProvideTextStyle(textStyle) {
                Box(
                    contentAlignment = Alignment.CenterStart,
                    modifier = Modifier
                        .border(
                            border = BorderStroke(
                                width = 1.dp,
                                color = MaterialTheme.colorScheme.primary,
                            ),
                            shape = RoundedCornerShape(8.dp)
                        )
                        .padding(12.dp)
                        .fillMaxWidth()
                ) {
                    innerTextField()
                    if (placeholder != null && state.text.isBlank() && !isFocused.value) {
                        placeholder.invoke()
                    }
                }
            }
        }
    )
}

@Preview(showBackground = true, backgroundColor = 0xFFFFFFFF, locale = "en")
@Composable
fun TextFieldPreview() {
    val textState = rememberTextFieldState()

    TextField(
        state = textState,
        modifier = Modifier.padding(16.dp),
        placeholder = {
            Text(text = "Hello, there!")
        }
    )
}