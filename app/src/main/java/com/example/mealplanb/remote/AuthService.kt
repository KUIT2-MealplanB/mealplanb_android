package com.example.mealplanb.remote

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.util.Log
import android.widget.Toast
import com.example.mealplanb.ApplicationClass
import com.example.mealplanb.ApplicationClass.Companion.mSharedPreferences
import com.example.mealplanb.MainActivity
import com.example.mealplanb.local.getJwt
import com.example.mealplanb.local.saveJwt
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class AuthService(private val context: Context) {
    private val authService = ApplicationClass.retrofit.create(Retrofitinterface::class.java)

    fun login(email: String,pw: String){
        val request = LoginRequest(email,pw)
        authService.login(request).enqueue(object: Callback<BaseResponse<LoginResponse>>{
            override fun onResponse(
                call: Call<BaseResponse<LoginResponse>>,
                response: Response<BaseResponse<LoginResponse>>
            ) {
                Log.d("Login response",response.toString())
                if(response.isSuccessful) { // response의 성공 여부를 확인
                    Toast.makeText(context, "로그인 성공", Toast.LENGTH_SHORT).show()
                    Log.d("로그인 정보",response.body().toString())

                    //token 저장
                    val token = response.body()?.result?.jwt.toString()
                    saveJwt(token)
//                    val token2 = getJwt() ?: ""
//                    Log.d("getJwt 확인",token2)

                    // 성공적으로 로그인이 되었을 경우, 다른 액티비티로 이동
                    val intent = Intent(context, MainActivity::class.java)
                    context.startActivity(intent)

                } else {
                    // 서버에서는 응답을 했지만, 로그인 실패와 같은 이유로 성공적인 응답이 아닌 경우, Toast 메시지
                    Toast.makeText(context, "로그인 실패", Toast.LENGTH_SHORT).show()
                    Log.d("로그인 실패 정보",response.body().toString())
                }
            }

            override fun onFailure(call: Call<BaseResponse<LoginResponse>>, t: Throwable) {
                Log.d("Login Failed",t.toString())
                // 로그인 요청 자체가 실패한 경우, Toast 메시지
                Toast.makeText(context, "로그인 오류", Toast.LENGTH_SHORT).show()
            }

        })
    }

}