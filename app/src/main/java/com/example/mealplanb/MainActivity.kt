package com.example.mealplanb
import android.app.DatePickerDialog
import android.content.Context
import android.content.Intent
import android.opengl.Visibility
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.DatePicker
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.mealplanb.databinding.ActivityMainBinding
import com.google.android.material.bottomnavigation.BottomNavigationView
import java.util.Calendar
import java.util.Date
import java.util.concurrent.TimeUnit

class MainActivity : AppCompatActivity(), OnBackPressedListener {
    lateinit var binding : ActivityMainBinding
    private var onBackPressedListener: OnBackPressedListener? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)

        binding.mainNavigation.itemIconTintList = null

        setContentView(binding.root)

        binding.mainNavigation.setOnItemSelectedListener {
            when(it.itemId){
                R.id.menu_mypage -> {
                    supportFragmentManager.beginTransaction().replace(R.id.main_frm, HomeFragment()).commit()
                    return@setOnItemSelectedListener true
                }
                R.id.menu_food -> {
                    supportFragmentManager.beginTransaction().replace(R.id.main_frm, MenuRecommendFragment()).commit()
                    return@setOnItemSelectedListener true
                }
                R.id.menu_stats -> {
                    supportFragmentManager.beginTransaction().replace(R.id.main_frm, StatFragment()).commit()
                    return@setOnItemSelectedListener true
                }
                else -> {
                    return@setOnItemSelectedListener true
                }
            }
        }
        binding.mainNavigation.selectedItemId = R.id.menu_mypage
    }

    fun changeBottomNavigationSelectedItem(itemId: Int) {
        val bottomNavigationView = findViewById<BottomNavigationView>(R.id.main_navigation)
        bottomNavigationView?.selectedItemId = itemId
    }

    //back 버튼 관련 이벤트 설정
    fun setOnBackPressedListener(listener: OnBackPressedListener) {
        onBackPressedListener = listener
    }

    // 백 버튼이 눌렸을 때 호출되는 메서드
    override fun onBackPressed() {
        if (onBackPressedListener != null) {
            onBackPressedListener?.onBackPressed()
        } else {
            super.onBackPressed()
        }
    }
}