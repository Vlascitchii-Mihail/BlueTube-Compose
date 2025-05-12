package com.vlascitchii.presentation_player.screen_player.utils

import android.content.Context
import android.provider.Settings

fun getIsAutoRotationEnabled(context: Context): Boolean {
    return try {
        Settings.System.getInt(
            context.contentResolver,
            Settings.System.ACCELEROMETER_ROTATION
        ) == 1
    } catch (ex: Settings.SettingNotFoundException) {
        false
    }
}