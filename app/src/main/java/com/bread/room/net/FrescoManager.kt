package com.bread.room.net

import android.content.Context
import android.content.res.Resources
import android.graphics.Bitmap
import android.graphics.PointF
import android.graphics.drawable.Animatable
import android.graphics.drawable.Drawable
import android.net.Uri
import android.support.annotation.DrawableRes
import android.support.annotation.NonNull
import android.view.View
import com.bread.room.BreadApplication
import com.facebook.common.executors.CallerThreadExecutor
import com.facebook.common.references.CloseableReference
import com.facebook.common.util.UriUtil
import com.facebook.datasource.DataSource
import com.facebook.drawee.backends.pipeline.Fresco
import com.facebook.drawee.backends.pipeline.PipelineDraweeController
import com.facebook.drawee.controller.BaseControllerListener
import com.facebook.drawee.drawable.ScalingUtils
import com.facebook.drawee.generic.GenericDraweeHierarchy
import com.facebook.drawee.generic.GenericDraweeHierarchyBuilder
import com.facebook.drawee.generic.RoundingParams
import com.facebook.drawee.interfaces.DraweeHierarchy
import com.facebook.drawee.view.DraweeView
import com.facebook.drawee.view.GenericDraweeView
import com.facebook.drawee.view.SimpleDraweeView
import com.facebook.imagepipeline.backends.okhttp3.OkHttpImagePipelineConfigFactory
import com.facebook.imagepipeline.common.ResizeOptions
import com.facebook.imagepipeline.datasource.BaseBitmapDataSubscriber
import com.facebook.imagepipeline.image.CloseableImage
import com.facebook.imagepipeline.image.ImageInfo
import com.facebook.imagepipeline.request.BasePostprocessor
import com.facebook.imagepipeline.request.ImageRequest
import com.facebook.imagepipeline.request.ImageRequestBuilder
import com.test.kotlin.getPackageName
import com.test.kotlin.getResource
import com.test.kotlin.net.OkHttpManager

/**
 * Created by zenghao on 2017/6/19.
 */
class FrescoManager private constructor(){

    companion object{
        val LOCAL_SCHEME: String by lazy {
            "file://"
        }

        val RES_SCHEME: String by lazy {
            "res://"
        }

        val packageName:String by lazy {
            getPackageName()
        }
        /**
         * frescohttp初始化在application中调用
         */
        fun initFrescoWithOkHttpPieline(context: Context){
           val okhttp = OkHttpManager.initOkhttpForFresco()
            val config = OkHttpImagePipelineConfigFactory.newBuilder(context,okhttp).build()
            Fresco.initialize(context,config)

        }
        /**
         * 获取本地文件uri
         * @param filePath 文件地址
         * @return Uri
         */
        private fun getLocalUri( filePath:String): Uri {
            var uri:String = filePath
            if(!filePath.startsWith(LOCAL_SCHEME)){
                uri = StringBuilder().append(LOCAL_SCHEME).append(filePath).toString()
            }
           return Uri.parse(uri)
        }
        /**
         * 加载uri
         *
         * @param uri uri
         * @return FrescoCreator
         */
        fun load(uri:Uri):Builder{
            return Builder(uri)
        }
        /**
         * 加载网络图片
         *
         * @param url 图片地址
         * @return FrescoCreator
         */
        fun loadUrl(url:String): Builder{
            return load(Uri.parse(url))
        }
        /**
         * 加载本地文件
         *
         * @param filePath 文件路径
         * @return FrescoCreator
         */
        fun loadFilePath(filePath: String):Builder{
            return load(getLocalUri(filePath))
        }


        fun loadDrawableRes(@DrawableRes resId: Int): Builder{
            val res = StringBuilder().append(RES_SCHEME).append(packageName).append("/").append(resId).toString()
            return load(Uri.parse(res))
        }


        fun clearMemoryCache(uri:Uri){
            Fresco.getImagePipeline().evictFromMemoryCache(uri)
        }
    }


    class Builder (private var uri: Uri){

        //是否自动旋转
        private var autoRotate:Boolean = false
        fun autoRotate(auto:Boolean):Builder{
            this.autoRotate = auto
            return this
        }

        private var scaleWidth = 0


        private var scaleHeight = 0

        //缩放高/缩放宽
        fun resize(width:Int,height:Int):Builder{
            this.scaleWidth = width
            this.scaleHeight = height
            return this
        }

        //默认占位图
        private  var placeHolderImage:Drawable? = null
        fun placeHolderImage(@DrawableRes resId:Int):Builder{
            try {
                this.placeHolderImage =  getResource().getDrawable(resId)
            }catch (e:Resources.NotFoundException){

            }
            return this
        }

        //加载失败占位图
        private  var failureImage:Drawable? = null
        fun failureImage(@DrawableRes resId: Int):Builder{
            try {
                this.failureImage =  getResource().getDrawable(resId)
            }catch (e:Resources.NotFoundException){

            }
            return this
        }
        //点击重新加载
        private var tapToRetry:Boolean = false
        fun tapToRetry(retry:Boolean):Builder{
            this.tapToRetry = retry
            return this
        }

        //缩放类型
        private  var scaleType: ScalingUtils.ScaleType? = null
        fun scaleType(scale:ScalingUtils.ScaleType):Builder{
            this.scaleType = scale
            return this
        }

        //圆形
        private var roundAsCircle:Boolean = false
        fun roundAsCircle(circle:Boolean):Builder{
            this.roundAsCircle = circle
            return this
        }

        //圆角
        private var cornersRadius:Float = 0f;
        fun cornersRadius(radius:Float):Builder{
            this.cornersRadius = radius
            return this
        }

        //gif自动播放
        private var autoPlayAnimations:Boolean = true
        fun autoPlayAnimations(auto:Boolean):Builder{
            this.autoPlayAnimations = auto
            return this
        }

        //本地缩略图预览
        private var localThumbnailPreviews:Boolean = false
        fun localThumbnailPreviews(local:Boolean):Builder{
            this.localThumbnailPreviews = local
            return this
        }

        //宽高比
        private var aspectRatio:Float = 0f
        fun aspectRatio(ratio:Float):Builder{
            this.aspectRatio = ratio
            return this
        }

        //下载事件监听
        private  var baseControllerListener: BaseControllerListener<ImageInfo>? = null
        fun baseControllerListener(listener:BaseControllerListener<ImageInfo>):Builder{
            this.baseControllerListener = listener
            return this
        }

        //图片后处理器
        private  var basePostprocessor: BasePostprocessor? = null
        fun basePostprocessor(processor:BasePostprocessor):Builder{
            this.basePostprocessor = processor
            return this
        }
        //是否允许渐进式加载
        private var progressiveRenderingEnabled:Boolean = false
        fun progressiveRenderingEnabled(enable:Boolean):Builder{
            this.progressiveRenderingEnabled = enable
            return this
        }
        //进度图片
        private  var progressBarDrawable:Drawable? = null
        fun progressBarDrawable(drawable: Drawable):Builder{
            this.progressBarDrawable = drawable
            return this
        }
        //强制刷新
        private var forceRefresh:Boolean = false
        fun forceRefresh(force:Boolean):Builder{
            this.forceRefresh = forceRefresh
            return this
        }
        //焦点裁切点
        private  var focusPoint:PointF? = null
        fun focusPoint(focus:PointF):Builder{
            this.focusPoint = focus
            return this
        }
        fun isCircleGifDraweeView(draweeView: GenericDraweeView):Boolean{
            return draweeView is CircleGifDraweeView
        }

        private fun initDraweeHierarchy(draweeView: GenericDraweeView): GenericDraweeHierarchy {
            var hierarchy = draweeView.hierarchy
            if (hierarchy != null){
                var resource:Resources
                if(draweeView.resources != null){
                    resource = draweeView.resources
                }else{
                    resource = getResource()
                }
                var builder = GenericDraweeHierarchyBuilder(resource)
                hierarchy = builder.build()
            }
            if (placeHolderImage != null){
                hierarchy.setPlaceholderImage(placeHolderImage)
            }
            if (failureImage != null ){
                hierarchy.setFailureImage(failureImage)
            }
            if(scaleType != null){
                hierarchy.setActualImageScaleType(scaleType)
            }
            if(progressBarDrawable != null ){
                hierarchy.setProgressBarImage(progressBarDrawable)
            }
            if(focusPoint != null){
                hierarchy.setActualImageFocusPoint(focusPoint)
            }
            return hierarchy
        }

       private fun initRoundingParams(draweeView: GenericDraweeView){
            if (draweeView is SimpleDraweeView && (roundAsCircle || cornersRadius > 0)){
                var roundParams = draweeView.hierarchy.roundingParams
                if(roundParams == null){
                    roundParams = RoundingParams()
                }
                roundParams.roundAsCircle = roundAsCircle
                if(cornersRadius > 0){
                    roundParams.setCornersRadius(cornersRadius)
                }
                draweeView.hierarchy.roundingParams = roundParams
            }
        }

        private fun initImageRequest():ImageRequest{
            if(UriUtil.isLocalFileUri(uri)){
                var requestBuilder = LocalImageRequestBuilder.newBuilderWithSouce(uri)
                if (scaleWidth > 0 && scaleHeight > 0){
                    requestBuilder.resizeOptions = ResizeOptions(scaleWidth,scaleHeight)
                }
                if(autoRotate){
                    requestBuilder.autoRotateEnable = autoRotate
                }
                if(localThumbnailPreviews){
                    requestBuilder.localThumbnailPreviewsEnabled = localThumbnailPreviews
                }
                if(basePostprocessor != null){
                    requestBuilder.postprocessor = basePostprocessor
                }
                if (progressiveRenderingEnabled){
                    requestBuilder.progressiveRenderingEnabled = progressiveRenderingEnabled
                }
                return requestBuilder.build()
            }else{
                var requestBuilder = ImageRequestBuilder.newBuilderWithSource(uri)
                if (scaleWidth > 0 && scaleHeight > 0){
                    requestBuilder.resizeOptions = ResizeOptions(scaleWidth,scaleHeight)
                }
                if(autoRotate){
                    requestBuilder.setAutoRotateEnabled(autoRotate)
                }

                if(basePostprocessor != null){
                    requestBuilder.postprocessor = basePostprocessor
                }
                if (progressiveRenderingEnabled){
                    requestBuilder.isProgressiveRenderingEnabled = progressiveRenderingEnabled
                }
                return requestBuilder.build()

            }
        }

        private fun initDraweeController(draweeView:GenericDraweeView,imageRequest:ImageRequest):PipelineDraweeController{
            var controllerBuilder = Fresco.newDraweeControllerBuilder()
            controllerBuilder.oldController = draweeView.controller
            controllerBuilder.imageRequest = imageRequest
            if (tapToRetry){
                controllerBuilder.tapToRetryEnabled = tapToRetry
            }
            if(draweeView is CircleGifDraweeView){
                //默认播放
                controllerBuilder.autoPlayAnimations = true
                // 代理监听，在gif加载完成后，保持圆角,其他回调，如果原始有监听，保持监听
                controllerBuilder.controllerListener = object: BaseControllerListener<ImageInfo>(){

                    override fun onFinalImageSet(id: String?, imageInfo: ImageInfo?, animatable: Animatable?) {
                        super.onFinalImageSet(id, imageInfo, animatable)
                        if(animatable != null ){
                            draweeView.isGif = true
                        }
                        if(baseControllerListener != null){
                            baseControllerListener!!.onFinalImageSet(id,imageInfo,animatable)
                        }
                    }

                    override fun onSubmit(id: String?, callerContext: Any?) {
                        super.onSubmit(id, callerContext)
                        if(baseControllerListener != null){
                            baseControllerListener!!.onSubmit(id,callerContext)
                        }
                    }

                    override fun onIntermediateImageSet(id: String?, imageInfo: ImageInfo?) {
                        super.onIntermediateImageSet(id, imageInfo)
                        if(baseControllerListener != null){
                            baseControllerListener!!.onIntermediateImageSet(id,imageInfo)
                        }
                    }

                    override fun onIntermediateImageFailed(id: String?, throwable: Throwable?) {
                        super.onIntermediateImageFailed(id, throwable)
                        if(baseControllerListener != null){
                            baseControllerListener!!.onIntermediateImageFailed(id,throwable)
                        }
                    }

                    override fun onFailure(id: String?, throwable: Throwable?) {
                        super.onFailure(id, throwable)
                        if(baseControllerListener != null){
                            baseControllerListener!!.onFailure(id,throwable)
                        }
                    }

                    override fun onRelease(id: String?) {
                        super.onRelease(id)
                        if(baseControllerListener != null){
                            baseControllerListener!!.onRelease(id)
                        }
                    }
                }

            }else{
                //非圆角gif
                if(autoPlayAnimations){
                    controllerBuilder.autoPlayAnimations = autoPlayAnimations
                }
                if(baseControllerListener != null){
                    controllerBuilder.controllerListener = baseControllerListener
                }
            }

            return controllerBuilder.build() as PipelineDraweeController
        }

        //加载图片到控件
        fun into(@NonNull draweeView: GenericDraweeView){
            if(forceRefresh){
                clearMemoryCache(uri)
            }
            if(draweeView is CircleGifDraweeView){

                if (!forceRefresh && uri.equals(draweeView.currentUri)){
                    // 优化gif加载，如果是同一个gif，不去重复请求
                    return
                }else{
                    draweeView.currentUri = uri
                }

            }

            if (draweeView is SimpleDraweeView){
                if(aspectRatio > 0){
                    draweeView.aspectRatio = aspectRatio
                }
            }
            var hierarchy = initDraweeHierarchy(draweeView)
            if (hierarchy != null ){
                draweeView.hierarchy = hierarchy
            }

            initRoundingParams(draweeView)
            var imageRequest = initImageRequest()
            var controller = initDraweeController(draweeView,imageRequest)
            draweeView.controller = controller

        }

        fun into(context: Context,listener:ImageResponseListener){
            progressiveRenderingEnabled = true
            var imageRequest = initImageRequest()
            var imagePipeline = Fresco.getImagePipeline()
            var dataSource = imagePipeline.fetchDecodedImage(imageRequest,context)
            dataSource.subscribe(object : BaseBitmapDataSubscriber(){
                override fun onNewResultImpl(bitmap: Bitmap?) {
                    listener.onSuccess(bitmap)
                }

                override fun onFailureImpl(p0: DataSource<CloseableReference<CloseableImage>>?) {
                    listener.onFailure()
                }
            }, CallerThreadExecutor.getInstance())
        }

    }




}