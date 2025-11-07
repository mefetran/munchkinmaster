package org.mefetran.munchkinmaster.presentation.ui.uikit.util

import android.content.Context
import android.os.VibrationEffect
import android.os.Vibrator
import androidx.core.content.getSystemService

private const val VibrationEffectMill = 50L

actual fun performHapticFeedback(context: Any?) {
    if (context !is Context) return

    val vibrator = context.getSystemService<Vibrator>()
    vibrator?.vibrate(VibrationEffect.createOneShot(VibrationEffectMill, VibrationEffect.DEFAULT_AMPLITUDE))
}
