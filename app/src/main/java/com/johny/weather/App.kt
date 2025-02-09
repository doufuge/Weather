package com.johny.weather

import android.app.Application
import com.johny.weather.utils.DensityUtil
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class App : Application() {

    override fun onCreate() {
        super.onCreate()
        DensityUtil.config(this)
    }

}