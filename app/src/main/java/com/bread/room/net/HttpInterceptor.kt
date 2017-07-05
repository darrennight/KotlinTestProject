package com.bread.room.net

import android.os.Build
import android.webkit.CookieManager
import android.webkit.CookieSyncManager
import com.bread.room.BreadApplication
import okhttp3.*

/**
 * Created by zenghao on 2017/6/7.
 */
class HttpInterceptor :Interceptor{

    companion object{
        val USER_AGENT = "User-Agent"
        val SIGN = "sign"
    }

    override fun intercept(chain: Interceptor.Chain): Response {
        var original = chain.request()

        var httpUrl = original.url().newBuilder()
                //添加加密参数
                //.addEncodedQueryParameter(SIGN,"加密参数")
                .build()

        var request = original.newBuilder()
                .addHeader(USER_AGENT,"")
                .url(httpUrl)
                .method(original.method(),original.body())
                .build()
        var response = chain.proceed(request)
        syncCookiesToWebView(request,response)
        return response
    }

    /***
     * 同步cookie到webview
     */
    private fun syncCookiesToWebView(request: Request,response: Response){
        var headers = response.headers()
        var cookies = Cookie.parseAll(request.url(),headers)
        if (cookies.isEmpty()){
            return
        }

        if(Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP){
            CookieSyncManager.createInstance(BreadApplication.instance)
        }

        var cookieManager = CookieManager.getInstance()
        cookieManager.setAcceptCookie(true)
        for (cookie in cookies){
            cookieManager.setCookie(cookie.domain(),cookie.toString())
        }
    }
}