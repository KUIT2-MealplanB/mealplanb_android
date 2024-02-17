package com.example.mealplanb.remote

import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.PATCH
import retrofit2.http.POST

interface RetroInterface {

    //회원가입
    @POST("user/signup")
    fun signup(
        @Body request: SignupRequest //내부에 포함할 데이터
    ) : Call<BaseResponse<SignupResponse>> //반환할 데이터

    //로그인
    @POST("user/login")
    fun login(
        @Body request: LoginRequest
    ): Call<BaseResponse<LoginResponse>>

    //아바타 정보 조회
    @GET("user/avatar")
    fun avatarcheck(): Call<BaseResponse<AvatarCheckResponse>>
    //아바타 정보 수정
    @PATCH("user/avatar")
    fun avatarupdate(
        @Body request: AvatarData
    ):Call<BaseResponse<AvatarUpdateResponse>>

    //Weight
    @GET("weight")
    fun weightcheck(): Call<BaseResponse<Weight>>
    @POST("weight")
    fun weightpost(
        @Body request : Weight
    ):Call<BaseResponse<Weight>>
}