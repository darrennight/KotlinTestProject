package com.test.kotlin.net

import com.bread.room.config.Constant
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

/**
 * Created by zenghao on 2017/6/5.
 */
class ApiService {

    companion object{
        private val builder = Retrofit.Builder().baseUrl(Constant.APIHOST)
                .client(OkHttpManager.getInstance().okHttpClient)
                .addConverterFactory(GsonConverterFactory.create())

        fun <T>createService(clazz: Class<T>): T{
            return builder.build().create(clazz)
        }
    }

}