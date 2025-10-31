package org.mefetran.munchkinmaster.presentation.ui.uikit.utils

import android.content.Context
import android.os.VibrationEffect
import android.os.Vibrator
import androidx.core.content.getSystemService

actual fun performHapticFeedback(context: Any?) {
    (context as Context?)?.let { currentContext ->
        val vibrator = context.getSystemService<Vibrator>()
        vibrator?.vibrate(VibrationEffect.createOneShot(50, VibrationEffect.DEFAULT_AMPLITUDE))
    }
}