package com.example.mealplanb

import android.app.Application
import android.content.Context
import android.content.SharedPreferences
import com.example.mealplanb.remote.XAccessTokenInterceptor
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

class ApplicationClass : Application() {

    companion object {
        const val X_ACCESS_TOKEN : String = "x-access-token"
        const val DEV_URL : String = "https://www.kuit2mealplanb.shop" //개발용 URL
        const val PROD_URL : String = "" //배포용 URL

        const val BASE_URL : String = DEV_URL // 상황에 따라 DEV와 PROD로 바뀐는 작업용 URL

        lateinit var retrofit : Retrofit
        lateinit var mSharedPreferences : SharedPreferences
    }

    val client: OkHttpClient = OkHttpClient.Builder()
        .readTimeout(30000, TimeUnit.MILLISECONDS)
        .connectTimeout(30000,TimeUnit.MILLISECONDS)
        .addNetworkInterceptor(XAccessTokenInterceptor()) //JWT 헤더 자동전송
        .build()

    override fun onCreate() {
        super.onCreate()

        //retrofit build, GsonConverterFactory는 Gson을 json으로 사용할 수 있게 함
        retrofit = Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(client)
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        mSharedPreferences = applicationContext.getSharedPreferences("My App Spf",Context.MODE_PRIVATE)
    }
}