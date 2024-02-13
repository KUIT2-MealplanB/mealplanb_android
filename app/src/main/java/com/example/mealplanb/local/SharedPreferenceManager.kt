package com.example.mealplanb.local

import android.util.Log
import com.example.mealplanb.ApplicationClass


fun getJwt() : String? { //서버에서 가져오기
    return ApplicationClass.mSharedPreferences?.getString("token",null)
}

fun removeJwt() { //회원탈퇴 = 삭제
    ApplicationClass.mSharedPreferences?.edit()?.clear()?.apply()
}

fun saveJwt(token: String) { //회원가입 = 저장
    ApplicationClass.mSharedPreferences?.edit()?.putString("token",token)?.apply()
}