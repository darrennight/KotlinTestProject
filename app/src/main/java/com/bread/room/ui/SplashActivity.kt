package com.bread.room.ui

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import com.bread.room.R
import com.bread.room.net.FrescoManager
import kotlinx.android.synthetic.main.activity_splash.*

/**
 * Created by zenghao on 2017/6/7.
 */
class SplashActivity :Activity(){

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)
        /*Handler().postDelayed(object :Runnable {
            override fun run() {
                startActivity(Intent(this@SplashActivity,LoginActivity::class.java))
                finish()
            }
        },2000)*/
//        var url = "https://cdn.pixabay.com/photo/2012/11/26/13/58/cat-67345__480.jpg"
        var url = "http://img.mp.itc.cn/upload/20160603/49ee73e7cffc4905b1de934a99e0abae.jpg"
        FrescoManager.loadUrl(url).autoRotate(true).into(sdv_cover)
    }
}