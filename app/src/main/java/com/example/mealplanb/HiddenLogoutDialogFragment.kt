package com.example.mealplanb

import android.app.Dialog
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window

class HiddenLogoutDialogFragment(context: Context) : Dialog(context) {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        requestWindowFeature(Window.FEATURE_NO_TITLE)

        window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT)) // 투명 배경
        setContentView(R.layout.fragment_hidden_logout_dialog)



    }
}