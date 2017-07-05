package com.bread.room.net

import android.net.Uri
import android.text.TextUtils
import com.facebook.imagepipeline.common.ImageDecodeOptions
import com.facebook.imagepipeline.common.Priority
import com.facebook.imagepipeline.common.ResizeOptions
import com.facebook.imagepipeline.request.ImageRequest
import com.facebook.imagepipeline.request.Postprocessor
import com.facebook.common.util.UriUtil
import com.facebook.imagepipeline.request.ImageRequestBuilder
import java.io.File


/**
 * Created by zenghao on 2017/6/20.
 */
class LocalImageRequestBuilder private constructor(){


    lateinit var resizeOptions:ResizeOptions
     var autoRotateEnable:Boolean = false
     var localThumbnailPreviewsEnabled:Boolean = false
     var postprocessor:Postprocessor? = null
     var progressiveRenderingEnabled:Boolean = false


    private lateinit var uri:Uri
    private  val lowestPermittedRequestLevel:ImageRequest.RequestLevel by lazy { ImageRequest.RequestLevel.FULL_FETCH }
    private  val imageDecodeOptions: ImageDecodeOptions by lazy { ImageDecodeOptions.defaults() }
    private  val cacheChoice:ImageRequest.CacheChoice by lazy { ImageRequest.CacheChoice.DEFAULT }
    private  val requestPriority:Priority by lazy { Priority.HIGH }


    private constructor(uri: Uri):this(){
        this.uri = uri
    }

    companion object{
        fun newBuilderWithSouce(uri:Uri):LocalImageRequestBuilder{
            return LocalImageRequestBuilder(uri)
        }


    }

   private class BuilderException : RuntimeException{
        constructor(message: String?) : super(message)
    }

    private class LocalImageRequest : ImageRequest{
        constructor(builder: ImageRequestBuilder?) : super(builder)

        override fun getSourceFile(): File {
            var file = super.getSourceFile()
            if(file == null || !file.exists()){
                file = File(sourceUri.encodedPath)
            }
            return file
        }
    }

    fun build():ImageRequest{
        this.validate()
        var builder = ImageRequestBuilder.newBuilderWithSource(uri)
                .setCacheChoice(cacheChoice)
                .setProgressiveRenderingEnabled(progressiveRenderingEnabled)
                .setLocalThumbnailPreviewsEnabled(localThumbnailPreviewsEnabled)
                .setImageDecodeOptions(imageDecodeOptions)
                .setResizeOptions(resizeOptions)
                .setAutoRotateEnabled(autoRotateEnable)
                .setRequestPriority(requestPriority)
                .setLowestPermittedRequestLevel(lowestPermittedRequestLevel)
                .setPostprocessor(postprocessor)
        return LocalImageRequest(builder)
    }

    private fun validate(){
        if (this.uri == null){
            throw LocalImageRequestBuilder.BuilderException("Source must be set!")
        }else{
            if (UriUtil.isLocalResourceUri(this.uri)) {
                if (!this.uri.isAbsolute) {
                    throw LocalImageRequestBuilder.BuilderException("Resource URI path must be absolute.")
                }

                if (TextUtils.isEmpty(uri.path) ) {
                    throw LocalImageRequestBuilder.BuilderException("Resource URI must not be empty")
                }
                try {
                    Integer.parseInt(this.uri.path.substring(1))
                } catch (var2: NumberFormatException) {
                    throw LocalImageRequestBuilder.BuilderException("Resource URI path must be a resource id.")
                }

            }

            if (UriUtil.isLocalAssetUri(this.uri) && !this.uri.isAbsolute) {
                throw LocalImageRequestBuilder.BuilderException("Asset URI path must be absolute.")
            }
        }
    }

}