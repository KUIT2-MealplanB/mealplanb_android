package com.example.mealplanb.local

import android.util.Log
import com.example.mealplanb.ApplicationClass

fun getJwt() : String? {
    return ApplicationClass.mSharedPreferences?.getString("token",null)
}


fun removeJwt() {
    ApplicationClass.mSharedPreferences?.edit()?.clear()?.apply()
}


fun saveJwt(token: String) {
    if(ApplicationClass.mSharedPreferences == null) {
        Log.d("Signup response","sharedpreference 빔")
    } else {
        Log.d("Signup Response","Sharedpreference 안 빔")
    }

    ApplicationClass.mSharedPreferences?.edit()?.putString("token",token)?.apply()
}