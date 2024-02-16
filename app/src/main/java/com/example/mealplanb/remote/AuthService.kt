package com.example.mealplanb.remote

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.util.Log
import android.widget.Toast
import com.example.mealplanb.ApplicationClass
import com.example.mealplanb.ApplicationClass.Companion.mSharedPreferences
import com.example.mealplanb.LoginPageActivity
import com.example.mealplanb.MainActivity
import com.example.mealplanb.local.getJwt
import com.example.mealplanb.local.saveJwt
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
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
                    val gson = Gson()
                    val type = object : TypeToken<BaseResponse<LoginResponse>>() {}.type
                    val errorResponse: BaseResponse<LoginResponse>? = gson.fromJson(response.errorBody()?.charStream(), type)
                    Log.d("로그인 실패 정보", errorResponse.toString())

                    if(errorResponse?.code.toString() == "5006"){
                        Toast.makeText(context, "존재하지 않는 이메일입니다.", Toast.LENGTH_SHORT).show()
                    }else{
                        Toast.makeText(context, "비밀번호가 일치하지 않습니다.", Toast.LENGTH_SHORT).show()
                    }
                    Log.d("로그인 실패 코드",errorResponse?.code.toString())
                }
            }

            override fun onFailure(call: Call<BaseResponse<LoginResponse>>, t: Throwable) {
                Log.d("Login Failed",t.toString())
                // 로그인 요청 자체가 실패한 경우, Toast 메시지
                Toast.makeText(context, "로그인 오류", Toast.LENGTH_SHORT).show()
            }

        })
    }

    fun logout() {
        val token = getJwt() ?: return
        authService.logout(token).enqueue(object: Callback<BaseResponse<Unit>> {
            override fun onResponse(call: Call<BaseResponse<Unit>>, response: Response<BaseResponse<Unit>>) {
                if (response.errorBody() != null) {
                    Log.d("Logout response", response.errorBody()!!.string())
                } else {
                    Log.d("Logout response", "errorBody is null")
                }
                if (response.isSuccessful) { // response의 성공 여부를 확인
                    Toast.makeText(context, "로그아웃에 성공하였습니다.", Toast.LENGTH_SHORT).show()
                    Log.d("로그아웃 정보",response.body().toString())

                    // 토큰 삭제
                    saveJwt("")

                    // 성공적으로 로그아웃이 되었을 경우, 로그인 화면 등으로 이동
                    val intent = Intent(context, LoginPageActivity::class.java) // LoginActivity로 변경해야 합니다.
                    context.startActivity(intent)
                } else {
                    // 서버에서는 응답을 했지만, 로그아웃 실패와 같은 이유로 성공적인 응답이 아닌 경우, Toast 메시지
                    Toast.makeText(context, "로그아웃에 실패하였습니다.", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<BaseResponse<Unit>>, t: Throwable) {
                Log.d("Logout Failed", t.toString())
                // 로그아웃 요청 자체가 실패한 경우, Toast 메시지
                Toast.makeText(context, "로그아웃 오류", Toast.LENGTH_SHORT).show()
            }
        })
    }


}