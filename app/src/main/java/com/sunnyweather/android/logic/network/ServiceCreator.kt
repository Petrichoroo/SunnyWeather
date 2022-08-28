package com.sunnyweather.android.logic.network

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

/**
 * 接口地址 https://api.caiyunapp.com/v2/place?query=...&token={token}&lang=zh_CN
 * 在单例类中创建一个Retrofit构建器用于使用PlaceService接口
 * */
object ServiceCreator {
    private const val BASE_URL = "https://api.caiyunapp.com"    //根路径

    private val retrofit = Retrofit.Builder()
        .baseUrl(BASE_URL)
        .addConverterFactory(GsonConverterFactory.create()) //指定Retrofit在解析数据时所使用的转换库
        .build()

    /**
     * 提供了一个外部可见的create()方法，并接收一个Class类型的参数
     * 外部调用create()时，实际上就是调用Retrofit对象的create()方法 从而创建出相应Service接口的动态代理对象
     * 创建动态代理对象写法：
     * val appService = ServiceCreator.create(AppService::class.java)*/
    fun <T> create(serviceClass: Class<T>): T = retrofit.create(serviceClass)

    /**
     * 由于泛型实化 可以使用T::class.java这种语法
     * 定义了一个不带参数的create()方法 里面调用上面定义的带有Class参数的create()方法即可
     * 进一步简化获取动态代理对象的写法：
     * val appService = ServiceCreator.create<AppService>()*/
    inline fun <reified T> create(): T = create(T::class.java)
}