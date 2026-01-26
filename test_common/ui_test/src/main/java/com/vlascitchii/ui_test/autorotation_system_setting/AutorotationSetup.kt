package com.vlascitchii.ui_test.autorotation_system_setting

import android.os.SystemClock
import androidx.test.platform.app.InstrumentationRegistry

fun setAutorotationEnabledValue(autorotationValue: Int) {
    InstrumentationRegistry.getInstrumentation().uiAutomation
        .executeShellCommand(
            "settings put system accelerometer_rotation $autorotationValue"
        )
        .close()

    SystemClock.sleep(100)
}