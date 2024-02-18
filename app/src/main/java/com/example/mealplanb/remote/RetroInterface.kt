package com.example.mealplanb.remote

import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.PATCH
import retrofit2.http.POST
import retrofit2.http.Query

interface RetroInterface {
    @POST("user/signup")
    fun signup(
        @Body request: SignupRequest //내부에 포함할 데이터
    ) : Call<BaseResponse<SignupResponse>> //반환할 데이터

    @POST("user/login")
    fun login(
        @Body request: LoginRequest
    ): Call<BaseResponse<LoginResponse>>

    @GET("user/plan")
    fun plancheck(): Call<BaseResponse<Plan>>
    @PATCH("user/plan")
    fun planupdate(
        @Body request: PlanUpdateRequest
    ): Call<BaseResponse<Plan>>
    @GET("user/plan/diet-type")
    fun planDiettypeCheck(
        //@Body request: PlanDietTypeRequest
        @Query("type") type: String
    ): Call<BaseResponse<PlanDietTypeResponse>>

    @GET("user/profile")
    fun userProfileCheck(
        @Query("mealDate") mealDate: String
    ): Call<BaseResponse<UserProfileResponse>>

    //Weight
    @GET("weight")
    fun weightcheck(): Call<BaseResponse<Weight>>
    @POST("weight")
    fun weightpost(
        @Body request : Weight
    ):Call<BaseResponse<Weight>>

    @GET("chat/my-favorite")
    fun myfavoriteMealCheck(): Call<BaseResponse<ChatRecommendMeal>>
}