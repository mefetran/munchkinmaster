package org.mefetran.munchkinmaster.presentation.ui.uikit.util

import android.view.WindowManager
import androidx.activity.compose.LocalActivity
import androidx.compose.runtime.Composable

@Composable
actual fun KeepScreenOn(isOn: Boolean) {
    val activity = LocalActivity.current

    activity?.let {
        if (isOn) {
            activity.window.addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON)
        } else {
            activity.window.clearFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON)
        }
    }
}
