package com.example.mealplanb.remote

import android.util.Log
import com.example.mealplanb.ApplicationClass
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

//retrofit에 service를 요청하고 응답받는 역할을 하는 class
class AuthService {
    private val authService = ApplicationClass.retrofit.create(RetroInterface::class.java)

    private lateinit var signupView : SignupView

    fun setSignupView(signupView: SignupView) {
        this.signupView = signupView
    }

    fun signup(email: String, password: String, provider: String, sex: String, age: Int,
               height: Int, initial_weight: Float, target_weight: Float, diet_type: String,
               avatar_color: String, nickname: String) {
        signupView.SignupLoading()
        val request = SignupRequest(email,password,provider,sex,age,height,initial_weight,target_weight,diet_type,avatar_color,nickname)
        authService.signup(request).enqueue(object : Callback<BaseResponse<SignupResponse>>{ //enqueue가 Call에서 사용하는 방식
            override fun onResponse(
                call: Call<BaseResponse<SignupResponse>>,
                response: Response<BaseResponse<SignupResponse>>
            ) {
                val resp = response.body()
                Log.d("Signup response",resp.toString())
                when(resp!!.code) {
                    1000 -> signupView.SignupSuccess()
                    else -> signupView.SignupFailure(resp.code,resp.message)
                }
            }

            override fun onFailure(call: Call<BaseResponse<SignupResponse>>, t: Throwable) {
                Log.d("Signup Failed",t.toString())
            }

        })
    }
}