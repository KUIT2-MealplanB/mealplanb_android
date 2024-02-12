package com.example.mealplanb.remote

import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.POST

interface RetroInterface {
    @POST("user/signup")
    fun signup(
        @Body request: SignupRequest
    ) : Call<BaseResponse<SignupResponse>>
}