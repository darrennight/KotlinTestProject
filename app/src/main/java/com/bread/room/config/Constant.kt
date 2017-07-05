package com.bread.room.config

/**
 * Created by zenghao on 2017/6/7.
 */
class Constant {

    companion object{
        const val APIHOST = "http://domain.com"

        /**登陆*/
        const val  LOGIN = APIHOST + "/accounts/login/"
        /**登出*/
        const  val LOGINOUT = APIHOST + "/accounts/logout/"
    }
}