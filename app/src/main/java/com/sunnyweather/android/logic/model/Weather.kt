package com.sunnyweather.android.logic.model

//Weather类用于将Realtime和Daily对象封装起来
data class Weather(val realtime: RealtimeResponse.Realtime, val daily: DailyResponse.Daily)