package com.sunnyweather.android.logic

import androidx.lifecycle.liveData
import com.sunnyweather.android.logic.model.Place
import com.sunnyweather.android.logic.network.SunnyWeatherNetwork
import kotlinx.coroutines.Dispatchers

/**仓库层的主要工作就是判断调用方请求的数据应该是从本地数据源中获取还是从网络数据源中获取，并将获得的数据返回给调用方
 * 而搜索城市数据的请求并没有太多缓存的必要，每次都发起网络请求去获取最新的数据即可
 * Repository单例类作为仓库层的统一封装入口
 * 一般在仓库层中定义的方法，为了能将异步获取的数据以响应式编程的方式通知给上一层，通常会返回一个LiveData对象*/
object Repository {
    /**
     * liveData()函数可以自动构建并返回一个LiveData对象，然后在它的代码块中提供一个挂起函数的上下文
     * 这样就可以在liveData()函数的代码块中调用任意的挂起函数
     * 这里将liveData()函数的线程参数类型指定成了Dispatchers.IO，这样代码块中的所有代码就都运行在子线程中
     * 众所周知，Android是不允许在主线程中进行网络请求的
     * 诸如读写数据库之类的本地数据操作也是不建议在主线程中进行的，因此非常有必要在仓库层进行一次线程转换
     */
    fun searchPlaces(query: String) = liveData(Dispatchers.IO) {
        val result = try {
            val placeResponse = SunnyWeatherNetwork.searchPlaces(query) //搜索城市数据
            if (placeResponse.status == "ok") { //请求成功
                val places = placeResponse.places
                Result.success(places)  //使用Kotlin内置的Result.success()方法来包装获取的城市数据列表
            } else {
                Result.failure(RuntimeException("response status is ${placeResponse.status}"))
            }
        } catch (e: Exception) {
            Result.failure<List<Place>>(e)
        }
        emit(result)    //emit()方法将包装的结果发射出去 类似于调用LiveData的setValue()方法来通知数据变化
    }
}