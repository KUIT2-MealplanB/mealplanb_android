package com.example.mealplanb

import android.app.Application
import android.content.Context
import android.content.SharedPreferences
import android.util.Log
import com.example.mealplanb.remote.XAccessTokenInterceptor
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

class ApplicationClass : Application(){

    companion object{
        const val X_ACCESS_TOKEN : String = "x-access-token"
        const val DEV_URL : String = "https://www.kuit2mealplanb.shop" // 개발용
        const val PROD_URL : String = "" // 배포용

        const val BASE_URL : String = DEV_URL
        var mSharedPreferences : SharedPreferences ? =null

        val client: OkHttpClient = OkHttpClient.Builder()
            .readTimeout(30000, TimeUnit.MILLISECONDS)
            .connectTimeout(30000,TimeUnit.MILLISECONDS)
            .addNetworkInterceptor(XAccessTokenInterceptor()) //JWT 헤더 자동전송
            .build()

        val retrofit: Retrofit = Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
   //         .client(client)
            .build()
    }

    override fun onCreate() {
        super.onCreate()
        mSharedPreferences = applicationContext.getSharedPreferences("My App Spf", Context.MODE_PRIVATE)
        Log.d("Signup Response","sharedpreference 초기화 확인")
    }
}