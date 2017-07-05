package com.bread.room.net.api

import com.bread.room.config.Constant
import com.bread.room.net.HttpBaseModel
import com.bread.room.net.bean.User
import retrofit2.Call
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.POST

/**
 * Created by zenghao on 2017/6/7.
 */
interface UserApi {

    @FormUrlEncoded
    @POST("/accounts/login/")
    fun accountLogin(@Field("username")name:String, @Field("password")password:String): Call<HttpBaseModel<User>>



    @FormUrlEncoded
    @POST("/accounts/login/")
    fun accountLoginTest(@Field("username")name:String, @Field("password")password:String): Call<Any>

}