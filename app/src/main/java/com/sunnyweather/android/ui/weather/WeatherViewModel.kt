package com.sunnyweather.android.ui.weather

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import com.sunnyweather.android.logic.Repository
import com.sunnyweather.android.logic.model.Location

class WeatherViewModel : ViewModel() {
    private val locationLiveData = MutableLiveData<Location>()

    /**
     * 和界面相关的数据，放到ViewModel中可以保证它们在手机屏幕发生旋转的时候不会丢失*/
    var locationLng = ""
    var locationLat = ""
    var placeName = ""

    /**
     * 调用仓库层中定义的refreshWeather()方法
     * 这样仓库层返回的LiveData对象就可以转换成一个可供Activity观察的LiveData对象
     * 观察locationLiveData对象*/

    val weatherLiveData = Transformations.switchMap(locationLiveData) {
        Repository.refreshWeather(it.lng, it.lat)
    }

    //刷新天气信息，并将传入的经纬度参数封装成一个Location对象后赋值给locationLiveData对象
    fun refreshWeather(lng: String, lat: String) {
        locationLiveData.value = Location(lng, lat)
    }
}