package com.sunnyweather.android.logic.model

import com.google.gson.annotations.SerializedName

/** 定义数据模型
 * 查询全球绝大多数城市的数据信息
 * 访问相关接口后服务器会返回一段JSON格式的数据，大致内容如下
    {"status":"ok","query":"北京",
        "places":[
            {"name":"北京市","location":{"lat":39.9041999,"lng":116.4073963},
            "formatted_address":"中国北京市"},
            {"name":"北京西站","location":{"lat":39.89491,"lng":116.322056},
            "formatted_address":"中国 北京市 丰台区 莲花池东路118号"},
            {"name":"北京南站","location":{"lat":39.865195,"lng":116.378545},
            "formatted_address":"中国 北京市 丰台区 永外大街车站路12号"},
            {"name":"北京站(地铁站)","location":{"lat":39.904983,"lng":116.427287},
            "formatted_address":"中国 北京市 东城区 2号线"}
        ]
    }
* status代表请求的状态，ok表示成功
* places是一个JSON数组，会包含几个与我们查询的关键字关系度比较高的地区信息
* 其中name表示该地区的名字，location表示该地区的经纬度
* formatted_address表示该地区的地址
 */

data class PlaceResponse(val status: String, val places: List<Place>)

data class Place(
    val name: String, val location: Location,
    @SerializedName("formatted_address") val address: String
)   //使用@SerializedName注解方式让JSON字段和Kotlin字段之间建立映射关系

data class Location(val lng: String, val lat: String)   //经纬度