package com.example.mealplanb.remote

import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.PATCH
import retrofit2.http.POST
import retrofit2.http.Path
import retrofit2.http.Query

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

    // 특정 식사 정보 조회
    @GET("food/{foodId}")
    fun checkFoodDetail(@Path("foodId") foodId: Int): Call<BaseResponse<GetFoodResponse>>

    //나의 식단 등록
    @POST("/my-meal")
    fun mymealupdate(
        @Body request: mymealData
    ):Call<BaseResponse<Unit>>

    //Weight
    @GET("weight")
    fun weightcheck(): Call<BaseResponse<Weight>>
    @POST("weight")
    fun weightpost(
        @Body request : Weight
    ):Call<BaseResponse<Weight>>

    //favorite food
    @GET("favorite-food")
    fun favoriteFoodGET():Call<BaseResponse<FavoriteFoodResponse>>

    @POST("favorite-food")
    fun favoriteFoodPost(
        @Body request : FavoriteFoodRequest
    ):Call<BaseResponse<Unit>>

    @PATCH("favorite-food/{foodId}")
    fun favoriteFoodPatch(
        @Path("foodId") foodId: Int,
    ):Call<BaseResponse<Unit>>

    @GET("food/auto-complete")
    fun searchfood(
        @Query("query") query: String?,
        @Query("page") page: Int = 0
    ):Call<BaseResponse<FoodSearchResponse>>
}