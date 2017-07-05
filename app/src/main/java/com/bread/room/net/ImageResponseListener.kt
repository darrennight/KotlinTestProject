package com.bread.room.net

import android.graphics.Bitmap

/**
 * Created by zenghao on 2017/6/20.
 */
interface ImageResponseListener {
    fun onSuccess(bitmap: Bitmap?)
    fun onFailure();
}