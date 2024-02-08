package com.example.mealplanb

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.content.Intent
import android.os.Handler
import com.example.mealplanb.databinding.ActivitySplashScreenBinding

class SplashScreenActivity : AppCompatActivity() {
    lateinit var binding: ActivitySplashScreenBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySplashScreenBinding.inflate(layoutInflater)
        setContentView(binding.root)

        //스플래시 화면 3초동안 보이게 설정, 이 후 privacy로 넘어감
        Handler().postDelayed({
            val intent = Intent(this, LoginPageActivity::class.java)
            startActivity(intent)
            finish()
        }, 3000) //3000ms=3초
    }
}