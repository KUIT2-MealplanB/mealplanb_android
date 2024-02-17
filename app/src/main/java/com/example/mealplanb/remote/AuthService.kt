package com.example.mealplanb.remote

import android.util.Log
import com.example.mealplanb.ApplicationClass
import com.example.mealplanb.local.getJwt
import com.example.mealplanb.local.saveJwt
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

    fun signup(email: String, password: String, sex: String, age: Int, height: Int, initial_weight: Float, target_weight: Float, diet_type: String, avatar_color: String, nickname: String) {
        signupView.SignupLoading()
        val request = SignupRequest(email,password,sex,age,height,initial_weight,
            target_weight,diet_type,avatar_color,nickname)
        authService.signup(request).enqueue(object : Callback<BaseResponse<SignupResponse>>{ //enqueue가 Call에서 사용하는 방식
            override fun onResponse(
                call: Call<BaseResponse<SignupResponse>>,
                response: Response<BaseResponse<SignupResponse>>
            ) {
                val resp = response.body()
                Log.d("Signup response body",resp.toString())
                saveJwt(resp!!.result.jwt)
                when(resp!!.code) {
                    1000 -> signupView.SignupSuccess()
                    else -> signupView.SignupFailure(resp.code,resp.message)
                }

                val token = getJwt()
                if (token == null) {
                    Log.d("Signup response token","getJwt 빔")
                } else {
                    Log.d("Signup response token", token)
                }
            }

            override fun onFailure(call: Call<BaseResponse<SignupResponse>>, t: Throwable) {
                Log.d("Signup Failed",t.toString())
            }

        })
    }

    //weight
    fun weightcheck(){
        signupView.SignupLoading()

        authService.weightcheck().enqueue(object : Callback<BaseResponse<Weight>> {
            override fun onResponse(
                call: Call<BaseResponse<Weight>>,
                response: Response<BaseResponse<Weight>>
            ) {
                val resp = response.body()
                Log.d("weight get response",resp.toString())
                when (resp?.code) {
                    1000 -> {
                        // 성공 시 원하는 처리
                        val weightCheckResponse =
                            resp.result // WeightCheckResponse 클래스에 따라 result에 응답 데이터가 있을 것이라 가정합니다
                        val weight = weightCheckResponse?.weight ?: 0.0 // 체중 정보 추출
                        val date = weightCheckResponse?.date ?: "" // 날짜 정보 추출

                        // HomeFragment의 SignupSuccess로 정보 전달
                        signupView.WeightcheckSuccess(weight as Float, date)
                        Log.d("weight get  Success", "weight get Success")
                    }

                    else -> if (resp != null) {
                        Log.d("weight get  error", "weight get error")
                    }else{
                        Log.d("weight get  null", "weight get null")
                    }
                }
            }

            override fun onFailure(call: Call<BaseResponse<Weight>>, t: Throwable) {
                Log.d("weight get Failed", t.toString())
            }

        })

    }

    fun weightpost(weight: Float, date: String){
        signupView.SignupLoading()
        val request = Weight(weight, date)
        authService.weightpost(request).enqueue(object : Callback<BaseResponse<Weight>>{
            override fun onResponse(
                call: Call<BaseResponse<Weight>>,
                response: Response<BaseResponse<Weight>>
            ) {
                val resp = response.body()
                when(resp?.code) {
                    1000 -> Log.d("weight post success",resp.toString()) //아직 layout이 구현안되어 있어서 Homefragment에 대신 구현
                    else -> if (resp != null) {
                        signupView.SignupFailure(resp.code,resp.message)
                    }else{
                        Log.d("weight post null",resp.toString())
                    }
                }
            }

            override fun onFailure(call: Call<BaseResponse<Weight>>, t: Throwable) {
                Log.d("weight post Failed", t.toString())
            }


        })
    }

    fun weightpatch(weight: Float, date: String){
        signupView.SignupLoading()
        val request = Weight(weight, date)
        authService.weightpatch(request).enqueue(object : Callback<BaseResponse<Weight>>{
            override fun onResponse(
                call: Call<BaseResponse<Weight>>,
                response: Response<BaseResponse<Weight>>
            ) {
                val resp = response.body()
                when(resp?.code) {
                    1000 -> Log.d("weight patch success",resp.toString()) //아직 layout이 구현안되어 있어서 Homefragment에 대신 구현
                    else -> if (resp != null) {
                        signupView.SignupFailure(resp.code,resp.message)
                    }else{
                        Log.d("weight patch null",resp.toString())
                    }
                }
            }

            override fun onFailure(call: Call<BaseResponse<Weight>>, t: Throwable) {
                Log.d("weight patch Failed", t.toString())
            }


        })
    }
}