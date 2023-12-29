package com.example.mealplanb

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.mealplanb.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    lateinit var binding : ActivityMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)

        binding.mainDaymeal1Cv.setOnClickListener {
            val intent = Intent(applicationContext,SearchMealActivity::class.java)
            startActivity(intent)
        }

        setContentView(binding.root)
    }
}