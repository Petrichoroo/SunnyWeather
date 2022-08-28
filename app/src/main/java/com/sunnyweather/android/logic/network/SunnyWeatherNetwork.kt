package com.sunnyweather.android.logic.network

import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.lang.RuntimeException
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException
import kotlin.coroutines.suspendCoroutine

/**
 * 定义一个统一的网络数据源访问入口，对所有网络请求的API进行封装*/

object SunnyWeatherNetwork {
    //创建一个动态代理对象，然后可以通过它随意调用接口中定义的所有方法
    private val placeService = ServiceCreator.create<PlaceService>()
    private val weatherService = ServiceCreator.create(WeatherService::class.java)

    //未来的天气信息
    suspend fun getDailyWeather(lng: String, lat: String) =
        weatherService.getDailyWeather(lng, lat).await()

    //实时天气
    suspend fun getRealtimeWeather(lng: String, lat: String) =
        weatherService.getRealtimeWeather(lng, lat).await()

    /**发起搜索城市数据请求
     * 当外部调用searchPlaces()函数时，Retrofit就会立即发起网络请求，同时当前的协程也会被阻塞住
     * 直到服务器响应我们的请求之后，await()函数会将解析出来的数据模型对象取出并返回 同时恢复当前协程的执行
     * searchPlaces()函数在得到await()函数的返回值后会将该数据再返回到上一层
     */

    suspend fun searchPlaces(query: String) =
        placeService.searchPlaces(query).await()

    /**
     * 借助suspendCoroutine函数简化传统回调机制的写法：
     * suspendCoroutine函数必须在协程作用域或挂起函数中才能调用，它接收一个Lambda表达式参数
     * 主要作用是将当前协程立即挂起，然后在一个普通的线程中执行Lambda表达式中的代码
     * Lambda表达式的参数列表上会传入一个Continuation参数，调用它的resume()方法或resumeWithException()可以让协程恢复执行

     * 不同的Service接口返回的数据类型也不同，因此使用泛型的方式定义一个await()函数
     * await()函数仍然是一个挂起函数，给它声明了一个泛型T，并将await()函数定义成了Call<T>的扩展函数
     * 这样所有返回值是Call类型的Retrofit网络请求接口就都可以直接调用await()函数*/

    private suspend fun <T> Call<T>.await(): T {
        //使用suspendCoroutine函数来挂起当前协程
        return suspendCoroutine { continuation ->
            //由于扩展函数的原因，这里拥有Call对象的上下文，可以直接调用enqueue()方法让Retrofit发起网络请求
            enqueue(object : Callback<T> {
                override fun onResponse(call: Call<T>, response: Response<T>) {
                    val body = response.body()
                    if (body != null)   //调用body()方法解析出来的对象可能为空
                        continuation.resume(body)
                    else    //请求失败 恢复协程并传入具体的异常原因
                        continuation.resumeWithException(RuntimeException("response body is null"))
                }

                override fun onFailure(call: Call<T>, t: Throwable) {
                    continuation.resumeWithException(t)
                }
            })
        }
    }
}