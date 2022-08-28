package com.sunnyweather.android

import android.annotation.SuppressLint
import android.app.Application
import android.content.Context

/**
 * 全局获取Context 由于从ViewModel层开始就不再持有Activity的引用*/
class SunnyWeatherApplication : Application() {
    companion object {
        const val TOKEN = "jsVUw1UHT0oXqrcI"

        @SuppressLint("StaticFieldLeak")
        lateinit var context: Context
    }

    override fun onCreate() {
        super.onCreate()
        context = applicationContext
    }
}