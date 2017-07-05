package com.bread.room.net

import android.content.Context
import android.graphics.Canvas
import android.graphics.Path
import android.net.Uri
import android.os.Build
import android.util.AttributeSet
import android.view.View
import com.facebook.drawee.view.SimpleDraweeView

/**
 * Created by zenghao on 2017/6/19.
 */

class CircleGifDraweeView: SimpleDraweeView{
    //是否是gif
    var isGif:Boolean = false
    //裁切路径
    private lateinit var path: Path

    //直径
    private var circle:Int = 0
    //当前图片uri
     lateinit var currentUri:Uri

    //圆形裁切上次直径
    private var lastCircle:Int = 0

    constructor(context: Context?) : super(context)
    constructor(context: Context?, attrs: AttributeSet?) : super(context, attrs)
    constructor(context: Context?, attrs: AttributeSet?, defStyle: Int) : super(context, attrs, defStyle)


    private fun getCirclePath():Path{
        if(path == null){
            path = Path()
            if(Build.VERSION.SDK_INT < Build.VERSION_CODES.JELLY_BEAN_MR2
                    && Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB)
            //关掉系统硬件加速
                setLayerType(View.LAYER_TYPE_SOFTWARE, null);
        }
        circle = Math.min(width,height)
        if(circle != lastCircle){
            lastCircle = circle
            path.reset()
            path.addCircle((width/2).toFloat(), (height/2).toFloat(), (circle/2).toFloat(), Path.Direction.CW)

        }
        return path
    }


    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        if(isGif){
            path = getCirclePath()
            canvas.save()
            try {
                canvas.clipPath(path)
            }catch (e:UnsupportedOperationException){

            }
            super.onDraw(canvas)
            canvas.restore()
        }else{
            super.onDraw(canvas)
        }
    }
}