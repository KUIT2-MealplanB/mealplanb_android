package com.example.mealplanb.remote

//import com.example.mealplanb.ApplicationClass.Companion.X_ACCESS_TOKEN
import android.util.Log
import com.example.mealplanb.local.getJwt
import okhttp3.Interceptor
import okhttp3.Request
import okhttp3.Response

class XAccessTokenInterceptor: Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val builder: Request.Builder = chain.request().newBuilder()

        val jwtToken: String? = getJwt()

        jwtToken?.let {
            builder.addHeader("Authorization","Bearer $jwtToken")
            Log.d("token heder", jwtToken)
        }

        return chain.proceed(builder.build())
    }
}