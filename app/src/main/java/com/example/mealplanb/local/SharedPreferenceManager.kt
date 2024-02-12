package com.example.mealplanb.local

import com.example.mealplanb.ApplicationClass

fun getJwt() : String? {
    return ApplicationClass.mSharedPreferences.getString("token",null)
}


fun removeJwt() {
    ApplicationClass.mSharedPreferences.edit().clear().apply()
}


fun saveJwt(token: String) {
    ApplicationClass.mSharedPreferences.edit().putString("token",token).apply()
}