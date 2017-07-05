package com.bread.room.ui

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.util.Log
import com.bread.room.R
import com.bread.room.net.api.RoomApi
import com.test.kotlin.net.ApiService
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

/**
 * Created by zenghao on 2017/6/8.
 */
class RoomDetailActivity :AppCompatActivity(){

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_room_detail)
        getRoomInfo()
    }

    private fun getRoomInfo(){
        var api = ApiService.createService(RoomApi::class.java)
        var call = api.getRoomInfo("/rooms/82/")
        call.enqueue(object :Callback<Any>{
            override fun onResponse(call: Call<Any>?, response: Response<Any>?) {
                var data = response?.body().toString()
                Log.e("====",data)
            }

            override fun onFailure(call: Call<Any>?, t: Throwable?) {
            }
        })
    }
}