package com.sunnyweather.android.logic.network

import com.sunnyweather.android.SunnyWeatherApplication
import com.sunnyweather.android.logic.model.PlaceResponse
import retrofit2.http.GET
import retrofit2.http.Query

/**网络层相关代码
 * 定义一个用于访问城市搜索API的Retrofit接口
 * 接口地址 https://api.caiyunapp.com/v2/place?query=...&token={token}&lang=zh_CN
 * 搜索城市数据的API中只有query这个参数是需要动态指定的，使用@Query注解的方式来进行实现
 * Retrofit会将服务器返回的JSON数据自动解析成PlaceResponse对象
 */
interface PlaceService {
    /**当调用searchPlaces()方法时 Retrofit会自动发起一条GET请求，去访问@GET注解中配置的地址
     * 方法的返回值必须声明成Retrofit中内置的Call类型，并通过泛型来指定服务器响应的数据应该转换成什么对象*/

    @GET("v2/place?token=${SunnyWeatherApplication.TOKEN}&lang=zh_CN")  //相对路径
    fun searchPlaces(@Query("query") query: String): retrofit2.Call<PlaceResponse>
}