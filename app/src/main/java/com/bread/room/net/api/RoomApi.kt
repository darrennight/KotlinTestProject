package com.bread.room.net.api

import com.bread.room.net.HttpBaseModel
import com.bread.room.net.bean.Room
import com.bread.room.net.bean.RoomData
import retrofit2.Call
import retrofit2.http.*

/**
 * Created by zenghao on 2017/6/7.
 */
interface RoomApi {

    @GET("/rooms/list/")
    fun getRoomLit(@Query("page")page:Int,@Query("count")count:Int):Call<HttpBaseModel<RoomData>>

    @GET
    fun getRoomLit2(@Url url:String):Call<HttpBaseModel<RoomData>>

    @GET
    fun getRoomInfo(@Url url:String):Call<Any>
}