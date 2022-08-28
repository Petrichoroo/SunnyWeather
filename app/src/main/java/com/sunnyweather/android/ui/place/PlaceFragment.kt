package com.sunnyweather.android.ui.place

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.sunnyweather.android.R
import com.sunnyweather.android.ui.weather.WeatherActivity
import kotlinx.android.synthetic.main.fragment_place.*

class PlaceFragment : Fragment() {

    /**
     * by lazy函数这种懒加载技术来获取PlaceViewModel的实例
     * 允许我们在整个类中随时使用viewModel这个变量，而完全不用关心它何时初始化、是否为空等前提条件*/
    val viewModel by lazy { ViewModelProvider(this).get(PlaceViewModel::class.java) }

    private lateinit var adapter: PlaceAdapter

    //加载fragment_place布局
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_place, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val layoutManager = LinearLayoutManager(activity)
        recyclerView.layoutManager = layoutManager
        //设置适配器，使用PlaceViewModel中的placeList集合作为数据源
        adapter = PlaceAdapter(this, viewModel.placeList)
        recyclerView.adapter = adapter
        searchPlaceEdit.addTextChangedListener { editable ->    //实时监听搜索框内容的变化情况
            val content = editable.toString()   //获取新内容
            if (content.isNotEmpty()) {
                viewModel.searchPlaces(content) //发起搜索城市数据的网络请求
            } else {
                recyclerView.visibility = View.GONE //隐藏RecyclerView
                bgImageView.visibility = View.VISIBLE   //展示仅用于美观用途的背景图
                viewModel.placeList.clear() //清空数据
                adapter.notifyDataSetChanged()  //刷新界面
            }
        }
        /**获取到服务器响应的数据
         * 对placeLiveData对象进行观察，当有任何数据变化时，就会回调到传入的Observer接口实现中*/
        viewModel.placeLiveData.observe(viewLifecycleOwner, Observer {
            val places = it.getOrNull() //拿不到数据就返回null
            if (places != null) {
                recyclerView.visibility = View.VISIBLE  //显示界面 展现城市数据
                bgImageView.visibility = View.GONE  //隐藏背景图
                viewModel.placeList.clear()
                viewModel.placeList.addAll(places)  //将数据添加到集合中
                adapter.notifyDataSetChanged()  //刷新界面
            } else {    //数据为空，则说明发生了异常
                Toast.makeText(activity, "未能查询到任何地点", Toast.LENGTH_SHORT).show()
                it.exceptionOrNull()?.printStackTrace() //打印具体的异常原因
            }
        })
        /**
         * 若当前已有存储的城市数据，那么就获取已存储的数据并解析成Place对象
         * 然后使用它的经纬度坐标和城市名直接跳转并传递给WeatherActivity
         * 这样用户就不需要每次都重新搜索并选择城市了。*/
        if (viewModel.isPlaceSaved()) {
            val place = viewModel.getSavedPlace()
            val intent = Intent(context, WeatherActivity::class.java).apply {
                putExtra("location_lng", place.location.lng)
                putExtra("location_lat", place.location.lat)
                putExtra("place_name", place.name)
            }
            startActivity(intent)
            activity?.finish()
            return
        }

    }
}