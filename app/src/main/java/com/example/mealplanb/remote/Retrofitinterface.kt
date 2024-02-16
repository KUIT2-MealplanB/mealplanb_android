package com.example.mealplanb.remote

import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST

interface Retrofitinterface {
    //로그인
    @POST("user/login")
    fun login(
        @Body request: LoginRequest
    ): Call<BaseResponse<LoginResponse>>

    //로그아웃
    @POST("user/logout")
    fun logout(@Header("Authorization") token: String): Call<BaseResponse<Unit>> //Unit = Void
}