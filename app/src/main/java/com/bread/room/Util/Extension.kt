package com.test.kotlin

import android.content.Context
import android.content.res.Resources
import android.support.annotation.Nullable
import android.widget.Toast
import com.bread.room.BreadApplication

/**
 * Created by zenghao on 2017/6/5.
 */


 fun Context.toast(message: String, duration: Int = Toast.LENGTH_SHORT) {
    Toast.makeText(this, message, duration).show()
}

fun Any.getPackageName():String{
    return BreadApplication.instance.packageName
}

fun Any.getResource():Resources{
    return BreadApplication.instance.getResource()
}

