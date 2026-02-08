package com.finscroll.app

import android.app.Application
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class FinScrollApp : Application() {
    
    override fun onCreate() {
        super.onCreate()
        
        // Initialize any required libraries here
        // Example: Analytics, Crash reporting, etc.
    }
}
