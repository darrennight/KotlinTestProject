package com.bread.room

import android.app.Application
import com.bread.room.net.FrescoManager
import kotlin.properties.Delegates

/**
 * Created by zenghao on 2017/6/7.
 */
class BreadApplication :Application(){

    init {
        instance = this
    }
    companion object {
         var instance: BreadApplication by Delegates.notNull()

    }

    override fun onCreate() {
        super.onCreate()
        FrescoManager.initFrescoWithOkHttpPieline(instance)
    }
}