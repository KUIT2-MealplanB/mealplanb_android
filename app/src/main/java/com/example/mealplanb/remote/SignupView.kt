package com.example.mealplanb.remote

import android.health.connect.datatypes.CervicalMucusRecord.CervicalMucusAppearance

interface SignupView {
    fun SignupLoading()
    fun SignupSuccess()
    fun SignupFailure(code: Int,msg: String)
    fun WeightcheckSuccess(weight:Float, date:String)
}