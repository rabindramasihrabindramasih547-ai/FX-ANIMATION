package com.example

import android.app.Application
import com.example.util.NotificationHelper

class ChargeFxApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        NotificationHelper.createNotificationChannel(this)
    }
}
