package com.vlascitchii.presentation_player.screen_player.orientation

import android.content.Context
import android.content.pm.ActivityInfo
import android.provider.Settings

const val ACCELEROMETER_PORTRAIT_ORIENTATION = 0
const val ACCELEROMETER_LANDSCAPE_ORIENTATION = 270
const val ACCELEROMETER_LANDSCAPE_REVERSE_ORIENTATION = 90

abstract class OrientationHandler {

    protected val landscapeOrientation = ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE // 6
    protected val reverseLandscapeOrientation = ActivityInfo.SCREEN_ORIENTATION_REVERSE_LANDSCAPE // 8
    protected val portraitOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT // 1
    protected val unspecifiedOrientation = ActivityInfo.SCREEN_ORIENTATION_UNSPECIFIED

    protected abstract fun changeToPortraitOrientation()
    protected abstract fun changeToLandscapeOrientation()
    protected abstract fun changeToReverseLandscapeOrientation()
    abstract fun outFromFullScreenSetStaticPortraitOrientation()
    abstract fun setSensorOrientation()
    abstract fun rotateWhenScreenAutoRotateSettingIsEnabled(orientation: Int)
    abstract fun rotateWhenAutoRotationSettingIsDisabled(currentSystemOrientation: Int)

    protected fun getAutoRotationIsEnabledState(context: Context): Boolean {
        return try {
            Settings.System.getInt(
                context.contentResolver,
                Settings.System.ACCELEROMETER_ROTATION
            ) == 1
        } catch (ex: Settings.SettingNotFoundException) {
            ex.printStackTrace()
            false
        }
    }

    protected fun setScreenOrientationInAutorotateModeOn(orientation: Int) {
        when(orientation) {
            ACCELEROMETER_PORTRAIT_ORIENTATION -> changeToPortraitOrientation()
            ACCELEROMETER_LANDSCAPE_ORIENTATION -> changeToLandscapeOrientation()
            ACCELEROMETER_LANDSCAPE_REVERSE_ORIENTATION -> changeToReverseLandscapeOrientation()
            else -> {}
        }
    }

    protected fun setScreenOrientationInAutorotateModeOff(orientation: Int) {
        when {
            portraitOrientation == orientation -> changeToPortraitOrientation()
            landscapeOrientation == orientation -> changeToLandscapeOrientation()
            reverseLandscapeOrientation == orientation ->changeToReverseLandscapeOrientation()
            else -> {}
        }
    }
}
