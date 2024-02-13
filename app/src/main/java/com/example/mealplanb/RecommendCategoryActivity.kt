package com.example.mealplanb

import android.content.Context
import android.content.Intent
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.cardview.widget.CardView
import com.example.mealplanb.databinding.ActivityRecommendCategoryBinding

class RecommendCategoryActivity : AppCompatActivity() {
    private lateinit var binding : ActivityRecommendCategoryBinding

    private var selectedCategory: View? = null
    private var selectedCategoryNumber: Int = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRecommendCategoryBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val categoryClickListener = View.OnClickListener { view ->
            if (selectedCategory == view) {
                deselectCategory()
            } else {
                selectCategory(view)
            }
        }

        binding.recommendCategory1Cv.setOnClickListener(categoryClickListener)
        binding.recommendCategory2Cv.setOnClickListener(categoryClickListener)
        binding.recommendCategory3Cv.setOnClickListener(categoryClickListener)
        binding.recommendCategory4Cv.setOnClickListener(categoryClickListener)
        binding.recommendCategory5Cv.setOnClickListener(categoryClickListener)

        //다음 버튼
        binding.privacyCompleteCv.setOnClickListener {
            if (selectedCategory != null) {
                var selectedCategoryString : String
                when(selectedCategoryNumber) {
                    1 -> selectedCategoryString = "일반"
                    2 -> selectedCategoryString = "운동"
                    3 -> selectedCategoryString = "키토"
                    4 -> selectedCategoryString = "비건"
                    else -> selectedCategoryString = "당뇨"
                }

                // 카테고리 번호를 SharedPreferences에 저장
                val sharedPref = getSharedPreferences("myPreferences", Context.MODE_PRIVATE)
                val editor = sharedPref.edit()
                editor.putInt("selectedCategory", selectedCategoryNumber)
                editor.putString("userSelectedCategory",selectedCategoryString)
                editor.apply()

                val intent = Intent(this, StartAvatarActivity::class.java)
                startActivity(intent)
            } else {
                Toast.makeText(this, "1개 이상의 카테고리를 선택해주세요.", Toast.LENGTH_SHORT).show()
            }
        }

        //뒤로가기
        binding.recommendBackIv.setOnClickListener {
            finish()
        }
    }

    //선택
    private fun selectCategory(view: View) {
        // 이전에 선택된 카테고리가 있다면, 기본 배경으로 변경
        selectedCategory?.setBackgroundResource(android.R.color.white)

        selectedCategory = view
        view.setBackgroundResource(R.drawable.selected_tab)
        binding.privacyCompleteCv.setCardBackgroundColor(Color.parseColor("#7C5CF8"))

        // 카테고리 번호를 selectedCategoryNumber에 저장
        when (view) {
            binding.recommendCategory1Cv -> selectedCategoryNumber = 1
            binding.recommendCategory2Cv -> selectedCategoryNumber = 2
            binding.recommendCategory3Cv -> selectedCategoryNumber = 3
            binding.recommendCategory4Cv -> selectedCategoryNumber = 4
            binding.recommendCategory5Cv -> selectedCategoryNumber = 5
        }
    }

    //취소
    private fun deselectCategory() {
        selectedCategory?.setBackgroundResource(android.R.color.white) // 기본 배경으로 변경
        selectedCategory = null
        binding.privacyCompleteCv.setCardBackgroundColor(Color.parseColor("#D7D7D7"))
    }

}