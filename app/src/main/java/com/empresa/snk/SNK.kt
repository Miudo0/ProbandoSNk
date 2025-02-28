package com.empresa.snk

import android.app.Application
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class SNK : Application() {

    override fun onCreate() {
        super.onCreate()
    }
}