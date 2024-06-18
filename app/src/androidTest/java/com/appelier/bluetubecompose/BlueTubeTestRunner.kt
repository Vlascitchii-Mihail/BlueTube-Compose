package com.appelier.bluetubecompose

import android.app.Application
import android.content.Context
import android.os.Bundle
import android.os.StrictMode
import androidx.test.runner.AndroidJUnitRunner
import dagger.hilt.android.testing.HiltTestApplication

class BlueTubeTestRunner: AndroidJUnitRunner() {

    override fun newApplication(
        cl: ClassLoader?,
        className: String?,
        context: Context?
    ): Application {
        return super.newApplication(cl, HiltTestApplication::class.java.name, context)
    }

    override fun onCreate(arguments: Bundle) {

        //Inside this method, the code sets a custom thread policy using StrictMode.
        // Specifically, it permits all network and disk operations on the main thread.
        //The purpose of this is to relax the strict thread policy during testing,
        // allowing network operations (which are normally disallowed on the main thread)
        // to proceed without exceptions.
        StrictMode.setThreadPolicy(StrictMode.ThreadPolicy.Builder().permitAll().build())
        super.onCreate(arguments)
    }
}