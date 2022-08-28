package com.sunnyweather.android.logic.network

import com.sunnyweather.android.SunnyWeatherApplication
import com.sunnyweather.android.logic.model.DailyResponse
import com.sunnyweather.android.logic.model.RealtimeResponse
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path

/**获取指定地区实时天气信息API的Retrofit接口
 * 接口地址 https://api.caiyunapp.com/v2.5/{token}/116.4073963,39.9041999/realtime.json
 * token部分传入令牌值，紧接着传入一个经纬度坐标，经度和纬度之间要用逗号隔开
 * 这样服务器就会把该地区的实时天气信息以JSON格式返回
 *
 * 获取未来几天的天气信息，还要借助另外一个API接口：
 * 接口地址 https://api.caiyunapp.com/v2.5/{token}/116.4073963,39.9041999/daily.json
 * 只需要将接口最后的realtime.json改成了daily.json即可
 * */
interface WeatherService {
    //获取实时天气信息 @Path注解来向请求接口中动态传入经纬度的坐标
    @GET("v2.5/${SunnyWeatherApplication.TOKEN}/{lng},{lat}/realtime.json")
    fun getRealtimeWeather(
        @Path("lng") lng: String,
        @Path("lat") lat: String
    ): Call<RealtimeResponse>

    //获取未来几天的天气信息
    @GET("v2.5/${SunnyWeatherApplication.TOKEN}/{lng},{lat}/daily.json")
    fun getDailyWeather(
        @Path("lng") lng: String,
        @Path("lat") lat: String
    ): Call<DailyResponse>
}